package com.titankingdoms.nodinchan.titanchat.core.channel.standard;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChatHandler;
import com.titankingdoms.nodinchan.titanchat.core.channel.Range;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;

public final class StandardChannel extends Channel {
	
	private final ChannelLoader loader;
	private final ChatHandler chatHandler;
	
	public StandardChannel(String name, Type type, StandardLoader loader) {
		super(name, type);
		this.loader = loader;
		this.chatHandler = new StandardChatHandler(this);
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
		return getSetting("format", "");
	}
	
	@Override
	public String getPassword() {
		return getSetting("password", "");
	}
	
	@Override
	public Range getRange() {
		Range range = Range.fromName(getSetting("range", "channel"));
		return (range != null) ? range : Range.CHANNEL;
	}
	
	@Override
	public String getTag() {
		return getSetting("tag", "");
	}
	
	@Override
	public void reload() {
		reloadConfig();
		getConfig().set("admins", getAdmins());
		getConfig().set("blacklist", getBlacklist());
		getConfig().set("whitelist", getWhitelist());
		saveConfig();
	}
}