package com.titankingdoms.dev.titanchat.channel.standard;

import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.channel.Channel;
import com.titankingdoms.dev.titanchat.channel.Factory;

public final class StandardFactory extends Factory {
	
	public StandardFactory() {
		super("Standard");
	}
	
	@Override
	public Channel loadChannel(FileConfiguration config) {
		return null;
	}
	
	@Override
	public void saveChannel(Channel channel) {
		
	}
}