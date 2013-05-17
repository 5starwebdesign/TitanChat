package com.titankingdoms.dev.titanchat.core.channel.irc;

import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;

public abstract class IRCChannelLoader extends ChannelLoader {
	
	public IRCChannelLoader(String name) {
		super(name);
	}
	
	@Override
	public abstract IRCChannel construct(String name);
	
	@Override
	public abstract IRCChannel load(String name, Status status, FileConfiguration config);
}