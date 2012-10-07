package com.titankingdoms.nodinchan.titanchat.channel.server;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Type;

public final class ServerLoader extends ChannelLoader {
	
	private final ServerChannel channel;
	
	public ServerLoader() {
		super("Server");
		this.channel = new ServerChannel(this);
	}
	
	@Override
	public Channel create(CommandSender sender, String name, Type type) {
		return channel;
	}
	
	@Override
	public Channel load(String name, Type type) {
		return channel;
	}
	
	@Override
	public void reload() {}
}