package com.titankingdoms.dev.titanchat.core.channel.standard;

import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;

public final class StandardLoader extends ChannelLoader {
	
	public StandardLoader() {
		super("Standard");
	}

	@Override
	public Channel load(String name, Status status, FileConfiguration config) {
		return new StandardChannel(name, status);
	}
}