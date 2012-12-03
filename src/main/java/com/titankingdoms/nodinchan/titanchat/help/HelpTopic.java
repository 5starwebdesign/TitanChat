package com.titankingdoms.nodinchan.titanchat.help;

import org.bukkit.command.CommandSender;

public interface HelpTopic {
	
	public boolean canView(CommandSender sender);
	
	public String getBriefDescription();
	
	public String[] getFullDescription();
	
	public String getName();
}