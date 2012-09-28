package com.titankingdoms.nodinchan.titanchat.channel.f;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.loading.Loadable;

public abstract class ChannelLoader extends Loadable {
	
	public ChannelLoader(String name) {
		super(name);
	}
	
	public abstract Channel create(CommandSender sender, String name);
	
	public abstract Channel load(String name);
}