package com.titankingdoms.dev.titanchat.core.channel.network;

import java.util.Set;

import com.titankingdoms.dev.titanchat.core.channel.Channel;

public final class Network {
	
	private final String name;
	
	public Network(String name) {
		this.name = name;
	}
	
	public Set<Channel> getHubs() {
		return null;
	}
	
	public String getName() {
		return name;
	}
	
	public Channel getNode() {
		return null;
	}
}