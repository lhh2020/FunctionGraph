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

	@Override
	public void onEnable()
	{
		getCommand("function").setExecutor(new FunctionCommand(this));

		operatorPrecedence.put('+', (byte) 1);
		operatorPrecedence.put('-', (byte) 1);

		operatorPrecedence.put('*', (byte) 2);
		operatorPrecedence.put('/', (byte) 2);

		operatorPrecedence.put('^', (byte) 3);

		operatorPrecedence.put('(', (byte) 4);
		operatorPrecedence.put(')', (byte) 4);
	}

	@Override
	public void onDisable()
	{

	}

	public static void showFunction(Player player, double size)
	{
		PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
		size = Math.abs(size);
		for(double i = -size; i < size;i += 0.1)
		{
			Location loc = player.getLocation();
			PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float)(loc.getX() + i), (float)loc.getY(), (float)(loc.getBlockZ() - 0.2),
					(float)0, (float)0, (float)0, 0, 1);
			connection.sendPacket(particles);
		}
		for(double i = -size; i < size;i += 0.1)
		{
			Location loc = player.getLocation();
			PacketPlayOutWorldParticles particles = new PacketPlayOutWorldParticles(EnumParticle.FLAME, true, (float)loc.getX(), (float)(loc.getY() + i), (float)(loc.getBlockZ() - 0.2),
					(float)0, (float)0, (float)0, 0, 1);
			connection.sendPacket(particles);
		}
		for(double i = -size; i < size;i += 0.01)
		{
			Location loc = player.getLocation();
			{
				String[] str = function.split("x");
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
	private static Double calculate(String formula)
	{
		return calculate(formula.split(" "));
	}
	private static Double calculate(String[] formula)
	{
		Stack<Double> numbers = new Stack<>();
		Stack<Character> operatorType = new Stack<>();

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
	private static void calculate_(Stack<Double> numbers, Stack<Character> operatorType)
	{
		while(true)
		{
			Double num_2 = numbers.pop();
			if(numbers.isEmpty())
			{
				numbers.push(num_2);
				break;
			}
			Double num_1 = numbers.pop();

			char operator = operatorType.pop();
			switch (operator)
			{
				case '(':
					numbers.push(num_1);
					numbers.push(num_2);
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


	private static Byte isOperator(String str)
	{
		return isOperator(str.charAt(0));
	}
	private static Byte isOperator(char c)
	{
		return operatorPrecedence.get(c);
	}
	private static Double isNumber(String str)
	{
		Double num;
		try {
			num = Double.parseDouble(str);
		} catch (Exception e) {
			return null;
		}
		return num;
	}
}
