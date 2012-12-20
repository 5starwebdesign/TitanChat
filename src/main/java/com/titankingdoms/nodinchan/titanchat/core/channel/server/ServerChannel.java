package com.titankingdoms.nodinchan.titanchat.core.channel.server;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChatHandler;
import com.titankingdoms.nodinchan.titanchat.core.channel.Range;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;

public final class ServerChannel extends Channel {
	
	private final ChannelLoader loader;
	private final ChatHandler chatHandler;
	
	public ServerChannel() {
		super("Server", Type.DEFAULT);
		this.loader = new ServerLoader(this);
		this.chatHandler = new ServerChatHandler(this);
	}
	
	@Override
	public String[] getAliases() {
		return getConfig().getStringList("aliases").toArray(new String[0]);
	}
	
	@Override
	public ChannelLoader getChannelLoader() {
		return loader;
	}
	
	@Override
	public ChatHandler getChatHandler() {
		return chatHandler;
	}
	
	@Override
	public String getFormat() {
		return plugin.getFormatHandler().getFormat();
	}
	
	@Override
	public String getPassword() {
		return "";
	}
	
	@Override
	public Range getRange() {
		return Range.GLOBAL;
	}
	
	@Override
	public String getTag() {
		return "";
	}
	
	@Override
	public void reload() {}
}