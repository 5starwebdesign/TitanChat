package com.titankingdoms.nodinchan.titanchat.core.channel.server;

import java.util.HashSet;
import java.util.Set;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelInfo;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.core.channel.Range;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public final class ServerChannel extends Channel {
	
	private final ServerLoader loader;
	private final ServerInfo info;
	
	public ServerChannel() {
		super("Server", Type.DEFAULT);
		this.loader = new ServerLoader(this);
		this.info = new ServerInfo(this);
	}
	
	@Override
	public ChannelLoader getChannelLoader() {
		return loader;
	}
	
	@Override
	public Set<Participant> getChatRecipients(Participant sender, String message) {
		return new HashSet<Participant>();
	}
	
	@Override
	public ChannelInfo getInfo() {
		return info;
	}
	
	@Override
	public Range getRange() {
		return Range.GLOBAL;
	}
	
	@Override
	public void reload() {}
}