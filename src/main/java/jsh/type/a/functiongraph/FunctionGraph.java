package jsh.type.a.functiongraph;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Stack;

public final class FunctionGraph extends JavaPlugin implements Listener
{
	// static 변수로 선언해 어디서든 접근할 수 있도록 함
	private static String function = "x";
	private static HashMap<Character, Byte>  operatorPrecedence = new HashMap<>();

	@Override
	public void onEnable()
	{
		getCommand("FunctionGraph").setExecutor(new FunctionCommand(this));

		operatorPrecedence.put('+', (byte) 1);
		operatorPrecedence.put('-', (byte) 1);

		operatorPrecedence.put('*', (byte) 2);
		operatorPrecedence.put('/', (byte) 2);

		operatorPrecedence.put('^', (byte) 3);
	}

	@Override
	public void onDisable()
	{

	}

	public void setFunction(String function)
	{
		this.function = function;
	}
	public void showFunction(Location loc)
	{
		// thread 이용 필요
	}
	private int getY(int x)
	{
		String str[];
		str = function.split(" ");

		Stack<Integer> numbers;
		Stack<Character> operatorType;

		for(int i = 0; i < str.length; i++)
		{
			Integer num;
			if((num = isNumber(str[i])) != null)
			{
				
			}
		}

		return 0;
	}
	private Byte isOperator(String str)
	{
		return operatorPrecedence.get(str.charAt(0));
	}
	private Integer isNumber(String str)
	{
		int num;
		try {
			num = Integer.parseInt(str);
		} catch (Exception e) {
			return null;
		}
		return num;
	}
}
