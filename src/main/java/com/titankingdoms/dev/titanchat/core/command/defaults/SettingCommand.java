package com.titankingdoms.dev.titanchat.core.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.setting.Setting;
import com.titankingdoms.dev.titanchat.core.command.Command;
import com.titankingdoms.dev.titanchat.util.C;

public final class SettingCommand extends Command {
	
	public SettingCommand() {
		super("Setting");
		setAliases("set");
		setArgumentRange(1, 1024);
		setBriefDescription("Sets the setting");
		setFullDescription(
				"Description: Sets the setting of the specified channel\n" +
				"Aliases: 'setting', 'set'\n" +
				"Usage: /titanchat <@[channel]> set [setting] <arguments>"
		);
		setUsage("[setting] <arguments>");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (channel == null) {
			msg(sender, C.RED + "Please specify or join a channel to use the command");
			return;
		}
		
		if (channel.getSettingModifier().existingSetting(args[0])) {
			Setting setting = channel.getSettingModifier().getSetting(args[0]);
			
			if (args.length < setting.getMinArguments() || args.length > setting.getMaxArguments()) {
				msg(sender, C.RED + "Invalid Argument Length");
				
				String usage = "/titanchat <@[channel]> set " + setting.getName() + " " + setting.getUsage();
				msg(sender, C.GOLD + usage);
				return;
			}
			
			if (!setting.permissionCheck(sender, channel)) {
				msg(sender, C.RED + "You do not have permission");
				return;
			}
			
			setting.execute(sender, args);
			return;
		}
		
		msg(sender, C.RED + "Invalid Command");
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return channel.isAdmin(sender.getName());
	}
}