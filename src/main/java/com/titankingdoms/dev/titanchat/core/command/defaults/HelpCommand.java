package com.titankingdoms.dev.titanchat.core.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.command.Command;
import com.titankingdoms.dev.titanchat.help.HelpMap;

public final class HelpCommand extends Command {
	
	public HelpCommand() {
		super("Help");
		setAliases("?");
		setArgumentRange(0, 1);
		setBriefDescription("Displays the help menu");
		setFullDescription(
				"Description: Displays the help menu and help topics\n" +
				"Aliases: 'help', '?'\n" +
				"Usage: /titanchat help <index/page/topic>");
		setUsage("<page/topic>");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		HelpMap helpMap = plugin.getHelpMap();
		
		try {
			
		} catch (IndexOutOfBoundsException e) {
			
		} catch (NumberFormatException e) {
			
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}