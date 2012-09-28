package com.titankingdoms.nodinchan.titanchat.channel.custom;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.channel.Channel.Option;

public class CustomChannelLoader implements ChannelLoader {
	
	private final CustomChannel channel;
	
	public CustomChannelLoader(CustomChannel channel) {
		this.channel = channel;
	}

	public Channel create(CommandSender sender, String name, Option option) {
		return channel;
	}

	public String getName() {
		return "Custom";
	}

	public Channel load(String name, Option option) {
		return channel;
	}
}