package com.titankingdoms.dev.titanchat.core.channel;

import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.loading.Loadable;

public abstract class ChannelLoader extends Loadable {
	
	public ChannelLoader(String name) {
		super(name);
	}
	
	public abstract Channel load(String name, Status status, FileConfiguration config);
}