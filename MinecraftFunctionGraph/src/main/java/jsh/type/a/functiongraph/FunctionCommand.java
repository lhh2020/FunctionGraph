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
			// 함수를 지정
			case "set":
				// 배열의 크기를 확인함으로 오류를 막음
				if(args.length < 2)
				{
					sender.sendMessage(FunctionGraph.function);
					return false;
				}
				// args 를 하나의 문자열로 합침
				// 문자열을 연결하기 위해 StringBuilder 사용
				StringBuilder sb = new StringBuilder("(");
				for(int i = 1; i < args.length; i++)
				{
					sb.append(' ');
					sb.append(args[i]);
				}
				sb.append(" )");
				FunctionGraph.function = sb.toString();
				return false;

			// 플레이어 위치에 함수를 소환함
			case "show":

				// Player가 CommandSender의 하위 클래스이므로 sender 가 Player 클래스인지 확인한다.
				if(!(sender instanceof Player))
				{
					return false;
				}
				if(args.length < 2)
				{
					sender.sendMessage("Please write down size of graph");
					return false;
				}
				double size;
				// 문자열이 double로 변환될 수 없는경우 exception 발생해서 try-catch 사용
				try {
					size = Double.parseDouble(args[1]);
				} catch (Exception e) {
					sender.sendMessage("Please write down size of graph");
					return false;
				}

				// FunctionGraph 의 showFunction 을 호출
				FunctionGraph.showFunction((Player)sender, size);
				return false;
		}

		return false;
	}
}
