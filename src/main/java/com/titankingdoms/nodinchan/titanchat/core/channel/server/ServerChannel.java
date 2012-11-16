package com.titankingdoms.nodinchan.titanchat.core.channel.server;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelInfo;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.core.channel.Range;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public class ServerChannel extends Channel {
	
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
	public ChannelInfo getInfo() {
		return info;
	}
	
	@Override
	public Range getRange() {
		return Range.GLOBAL;
	}
	
	@Override
	public void reload() {}
	
	@Override
	public List<Participant> selectRecipants(Player sender, String message) {
		return new ArrayList<Participant>();
	}
}