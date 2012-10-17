package com.titankingdoms.nodinchan.titanchat.channel.server;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelInfo;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Access;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Range;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Type;

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
	public boolean hasAccess(Player player, Access access) {
		return false;
	}
	
	@Override
	public void reload() {}
	
	@Override
	public List<Player> selectRecipants(Player sender, String message) {
		return new ArrayList<Player>();
	}
}