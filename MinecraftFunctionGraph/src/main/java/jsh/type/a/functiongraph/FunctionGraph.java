package jsh.type.a.functiongraph;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Queue;
import java.util.Stack;

public final class FunctionGraph extends JavaPlugin implements Listener
{
	// static 변수로 선언해 어디서든 접근할 수 있도록 함
	public static String function = "( x )";
	private static HashMap<Character, Byte> operatorPrecedence = new HashMap<>();

	// 플러그인이 시작될때 실행되는 메소드
	@Override
	public void onEnable()
	{
		getCommand("function").setExecutor(new FunctionCommand(this));

		// 우선순위를 map 에 저장한다
		operatorPrecedence.put('+', (byte) 1);
		operatorPrecedence.put('-', (byte) 1);

		operatorPrecedence.put('*', (byte) 2);
		operatorPrecedence.put('/', (byte) 2);

		operatorPrecedence.put('^', (byte) 3);

		operatorPrecedence.put('(', (byte) 4);
		operatorPrecedence.put(')', (byte) 4);
		operatorPrecedence.put('|', (byte) 4);
	}

	@Override
	public void onDisable()
	{

	}

	// 플레이어 좌표에 x측 y축을 출력하고 문자열 function 의 x를 숫자로 바꾸어 calculate 함수를 실행시켜 계산하고 플레이어 위치 + 계산값 위치에 출력
	public static void showFunction(Player player, double size)
	{
		PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
		size = Math.abs(size);
		for(double i = -size; i < size;i += 0.1)
		{
			// 플레이어 위치를 가져와
			Location loc = player.getLocation();
			// 플레이어 파티클을 띄움
			PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float)(loc.getX() + i), (float)loc.getY(), (float)(loc.getBlockZ() - 0.2),
					(float)0, (float)0, (float)0, 0, 1);
			connection.sendPacket(particles);
		}
		for(double i = -size; i < size;i += 0.1)
		{
			// 플레이어 위치를 가져와
			Location loc = player.getLocation();
			// 플레이어 파티클을 띄움
			PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float)loc.getX(), (float)(loc.getY() + i), (float)(loc.getBlockZ() - 0.2),
					(float)0, (float)0, (float)0, 0, 1);
			connection.sendPacket(particles);
		}
		for(double i = -size; i < size;i += 0.01)
		{
			Location loc = player.getLocation();
			{
				// 'x' 로 쪼개
				String[] str = function.split("x");
				// StringBuffer 로 중간에 값을 넣어 합쳐준다
				StringBuffer sb = new StringBuffer();
				sb.append(str.length > 0 ? str[0] : "");
				for(int j = 1; j < str.length; j++)
				{
					sb.append(i);
					sb.append(str[j]);
				}

				//test code
				//Bukkit.getConsoleSender().sendMessage(sb.toString());

				loc.add(i, calculate(sb.toString()), 0);
				PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.VILLAGER_HAPPY, true, (float)loc.getX(), (float)loc.getY(), (float)loc.getBlockZ(),
						(float)0, (float)0, (float)0, 0, 1);
				connection.sendPacket(particles);
			}
		}
	}

	// 문자열로 주어진 수식을 계산하는 역할
	private static Double calculate(String formula)
	{
		return calculate(formula.split(" "));
	}
	private static Double calculate(String[] formula)
	{
		Stack<Double> numbers = new Stack<>();
		Stack<Character> operatorType = new Stack<>();
		boolean abs = false;

		for(int i = 0; i < formula.length; i++)
		{
			// test code
			//System.out.print(formula[i] + "  ");

			Double num = isNumber(formula[i]);
			Byte oper = isOperator(formula[i]);
			if(num != null)
			{
				numbers.push(num);
				continue;
			}
			else if(oper != null)
			{
				if(operatorType.isEmpty())
				{
					operatorType.push(formula[i].charAt(0));
					continue;
				}
				if(formula[i].charAt(0) == ')')
				{
					calculate_(numbers, operatorType);
					continue;
				}
				if(formula[i].charAt(0) == '|')
				{
					if(abs)
					{
						calculate_(numbers, operatorType);
						abs = false;
						continue;
					}
					abs = true;
				}
				byte n = isOperator(operatorType.peek());
				if(n > oper)
				{
					calculate_(numbers, operatorType);
				}
				operatorType.push(formula[i].charAt(0));
				continue;
			}
		}

		calculate_(numbers, operatorType);

		return numbers.pop();
	}

	// stack에 들어가 있는 값들을 순차적으로 계산함함
	private static void calculate_(Stack<Double> numbers, Stack<Character> operatorType)
	{
		// numbers 가 null 인 경우 isEmpty 를 실행시킬 수 없음으로 null 체크를 먼저 함
		if(numbers == null || numbers.isEmpty())
		{
			return;
		}
		while(true)
		{
			// 2개의 값을 빼서
			Double num_2 = numbers.pop();
			if(numbers.isEmpty())
			{
				// 값이 하나밖에 없으면 루프를 탈출함
				numbers.push(num_2);
				break;
			}
			Double num_1 = numbers.pop();

			char operator = operatorType.pop();
			switch (operator)
			{
				// 두 값을 계산하고 값을 계산하여 하나의 값을 넣음
				case '(':
					numbers.push(num_1);
					numbers.push(num_2);
					return;
				case '|':
					numbers.push(num_1);
					numbers.push(Math.abs(num_2));
					return;
				case '+':
					numbers.push(num_1 + num_2);
					continue;
				case '-':
					numbers.push(num_1 - num_2);
					continue;
				case '*':
					numbers.push(num_1 * num_2);
					continue;
				case '/':
					numbers.push(num_1 / num_2);
					continue;
				case '^':
				{
					double num__1 = num_1;
					int num__2 = ((int)(double)num_2);
					if(num_2 == 0)
					{
						numbers.push( 1.0);
					}
					if(num_2 > 0)
					{
						for(int i = 1; i < num__2; i++)
						{
							num_1 *= num__1;
						}
						numbers.push(num_1);
						continue;
					}
				}
			}
		}
	}


	// 연산자인지 확인하고 우선순위 반환
	private static Byte isOperator(String str)
	{
		return isOperator(str.charAt(0));
	}
	private static Byte isOperator(char c)
	{
		return operatorPrecedence.get(c);
	}
	// 숫자인지 확인하고 숫자 반환
	private static Double isNumber(String str)
	{
		int index = str.indexOf("--");
		if(index != -1)
		{
			StringBuffer sb = new StringBuffer(str);
			str = sb.substring(index+2, sb.length());
		}
		Double num;
		try {
			num = Double.parseDouble(str);
		} catch (Exception e) {
			return null;
		}
		return num;
	}
}
