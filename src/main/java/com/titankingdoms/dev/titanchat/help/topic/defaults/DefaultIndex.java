package com.titankingdoms.dev.titanchat.help.topic.defaults;

import java.util.List;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.help.Help;
import com.titankingdoms.dev.titanchat.help.Index;
import com.titankingdoms.dev.titanchat.help.Topic;

public final class DefaultIndex extends Index {
	
	private final Help help;
	
	public DefaultIndex(Help help) {
		super("TitanChat Help Index");
		this.help = help;
	}
	
	public boolean canView(CommandSender sender) {
		return true;
	}
	
	public String getBriefDescription() {
		return "Help Index of TitanChat";
	}
	
	@Override
	public List<Topic> getTopics() {
		return help.getHelpTopics();
	}
}