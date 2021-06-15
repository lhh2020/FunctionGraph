package jsh.type.a.functiongraph;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Queue;
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
	private Integer getY(int x, String function)
	{
		return getY(x, function.split(" "));
	}
	private Integer getY(int x, String[] str)
	{

		Stack<Integer> numbers = new Stack<>();
		Stack<Character> operatorType = new Stack<>();

		for(int i = 0; i < str.length; i++)
		{
			Integer num = isNumber(str[i]);
			Byte oper = isOperator(str[i]);
			if(num != null)
			{
				numbers.push(num);
				continue;
			}
			else if(oper != null)
			{
				byte n = isOperator(operatorType.peek());
				if(n > oper)
				{
					while(true)
					{
						Integer num_1 = numbers.pop();
						Integer num_2 = numbers.pop();
						if(num_2 == null)
						{
							numbers.push(num_2);
							break;
						}
						char operator = operatorType.pop();
						switch (operator)
						{
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
						}
					}
				}
				operatorType.push(str[i].charAt(0));
			}
			else if(str[i].equals("("))
			{
				int index_1 = i;
				int index_2 = -1;
				int count = 0;
				int count_2 = 0;
				for(; i < str.length; ++i)
				{
					if(str[i].equals("("))
					{
						count++;
						continue;
					}
					if(str[i].equals(")"))
					{
						if(count > count_2)
						{
							count_2++;
							continue;
						}
						index_2 = i;
						break;
					}
				}

				if(index_2 == -1)
				{
					return null;
				}

				String s[] = new String[index_2 - index_1 - 1];
				for(int j = 0; j < s.length; j++)
				{
					s[j] = str[j+index_1+1];
				}
				numbers.push(getY(x, s));
			}

		}

		return 0;
	}
	private Byte isOperator(String str)
	{
		return isOperator(str.charAt(0));
	}
	private Byte isOperator(char c)
	{
		return operatorPrecedence.get(c);
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
