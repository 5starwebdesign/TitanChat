package com.titankingdoms.nodinchan.titanchat.core.channel;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.enumeration.Type;
import com.titankingdoms.nodinchan.titanchat.loading.Loadable;

public abstract class ChannelLoader extends Loadable {
	
	public ChannelLoader(String name) {
		super(name);
	}
	
	public abstract Channel create(CommandSender sender, String name, Type type);
	
	public abstract Channel load(String name, Type type);
	
	public abstract void reload();
}