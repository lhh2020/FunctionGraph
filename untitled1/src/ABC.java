import java.util.HashMap;
import java.util.Stack;

public class ABC
{
	private static HashMap<Character, Byte> operatorPrecedence = new HashMap<>();

	public static void main(String[] str)
	{
		System.out.println("x".split("x").length);

		ABC abc = new ABC();

		String function = "( 2 + 5 ) ^ 2";

		operatorPrecedence.put('+', (byte) 1);
		operatorPrecedence.put('-', (byte) 1);

		operatorPrecedence.put('*', (byte) 2);
		operatorPrecedence.put('/', (byte) 2);

		operatorPrecedence.put('^', (byte) 3);

		operatorPrecedence.put('(', (byte) 4);
		operatorPrecedence.put(')', (byte) 4);

		System.out.println("\n\n" + abc.calculate(function));

	}
	public Double getY(int x, String function)
	{
		return 0.0;
	}
	private Double calculate(String formula)
	{
		return calculate(formula.split(" "));
	}
	private Double calculate(String[] formula)
	{
		Stack<Double> numbers = new Stack<>();
		Stack<Character> operatorType = new Stack<>();

		for(int i = 0; i < formula.length; i++)
		{
			// test code
			System.out.print(formula[i] + "  ");

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
	private void calculate_(Stack<Double> numbers, Stack<Character> operatorType)
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


	private Byte isOperator(String str)
	{
		return isOperator(str.charAt(0));
	}
	private Byte isOperator(char c)
	{
		return operatorPrecedence.get(c);
	}
	private Double isNumber(String str)
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