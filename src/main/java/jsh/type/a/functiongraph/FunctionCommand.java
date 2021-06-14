package jsh.type.a.functiongraph;

import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Stack;

public class FunctionCommand implements CommandExecutor
{
	private FunctionGraph core;

	public FunctionCommand(FunctionGraph core)
	{
		this.core = core;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command command, String lable, String[] args)
	{
		// 배열의 크기를 확인함으로 오류를 막음
		if(args.length < 1)
		{
			sender.sendMessage("Please text command");
			return false;
		}

		// switch / case 문 사용
		switch (args[0].toLowerCase())
		{
			case "set":
				// 배열의 크기를 확인함으로 오류를 막음
				if(args.length < 2)
				{
					sender.sendMessage("Please write a function");
					return false;
				}

				// 문자열을 연결하기 위해 StringBuilder 사용
				StringBuilder sb = new StringBuilder();
				for(int i = 1; i < args.length; i++)
				{
					sb.append(args[i]);
				}

				core.setFunction(sb.toString());

				break;
			case "show":

				if(!(sender instanceof Player))
				{
					return false;
				}

				// 인스턴스 형식의 타입을 사용해야 함으로 Interger / Character 사용
				Stack<Integer> number;
				Stack<Character> operatorType;
				Location loc = ((Player) sender).getLocation();


				break;
		}

		return false;
	}
}
