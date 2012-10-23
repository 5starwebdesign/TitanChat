package com.titankingdoms.nodinchan.titanchat.core.channel;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

public abstract class ChannelInfo {
	
	protected final TitanChat plugin;
	
	protected final Channel channel;
	
	public ChannelInfo(Channel channel) {
		this.plugin = TitanChat.getInstance();
		this.channel = channel;
	}
	
	public abstract String[] getAliases();
	
	public final Channel getChannel() {
		return channel;
	}
	
	public abstract String getColour();
	
	public abstract String getFormat();
	
	public ChannelLoader getLoader() {
		return channel.getChannelLoader();
	}
	
	public final String getName() {
		return channel.getName();
	}
	
	public abstract String getPassword();
	
	public String getSetting(String node, String def) {
		return channel.getConfig().getString("setting." + node, def);
	}
	
	public int getSetting(String node, int def) {
		return channel.getConfig().getInt("setting." + node, def);
	}
	
	public double getSetting(String node, double def) {
		return channel.getConfig().getDouble("setting." + node, def);
	}
	
	public boolean getSetting(String node, boolean def) {
		return channel.getConfig().getBoolean("setting." + node, def);
	}
	
	public abstract String getTag();
	
	public abstract String getTopic();
	
	public abstract void setColour(String colour);
	
	public abstract void setFormat(String format);
	
	public abstract void setPassword(String password);
	
	public void setSetting(String node, Object value) {
		channel.getConfig().set("setting." + node, value);
	}
	
	public abstract void setTag(String tag);
	
	public abstract void setTopic(String topic);
}