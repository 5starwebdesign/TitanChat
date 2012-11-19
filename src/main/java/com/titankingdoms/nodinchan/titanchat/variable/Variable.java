package com.titankingdoms.nodinchan.titanchat.variable;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.loading.Loadable;

public abstract class Variable extends Loadable {
	
	public Variable(String name) {
		super(name);
	}
	
	public String getBracket() {
		String bracket = plugin.getConfig().getString("formatting.variables." + getVariableTag());
		return (bracket != null) ? bracket : plugin.getConfig().getString("formatting.bracket", "%var%");
	}
	
	public abstract String getVariable(CommandSender sender);
	
	public abstract String getVariableTag();
}