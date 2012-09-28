package com.titankingdoms.nodinchan.titanchat.channel.standard;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel.Option;
import com.titankingdoms.nodinchan.titanchat.channel.Channel.ChannelLoader;

public final class ServerChannelLoader implements ChannelLoader {
	
	private final ServerChannel channel;
	
	public ServerChannelLoader() {
		this.channel = new ServerChannel();
	}

	public Channel create(CommandSender sender, String name, Option option) {
		return channel;
	}

	public String getName() {
		return "Server";
	}

	public Channel load(String name, Option option) {
		return channel;
	}
}