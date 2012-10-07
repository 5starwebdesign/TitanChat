package com.titankingdoms.nodinchan.titanchat.channel.standard;

import com.titankingdoms.nodinchan.titanchat.channel.ChannelInfo;

public final class StandardInfo extends ChannelInfo {
	
	public StandardInfo(StandardChannel channel) {
		super(channel);
	}
	
	@Override
	public String getColour() {
		return channel.getConfig().getString("display-colour", "");
	}
	
	@Override
	public String getFormat() {
		String format = channel.getConfig().getString("format", "");
		return (format.isEmpty()) ? plugin.getConfig().getString("formatting.format") : format;
	}
	
	@Override
	public String getPassword() {
		return channel.getConfig().getString("password", "");
	}
	
	@Override
	public String getTag() {
		return channel.getConfig().getString("tag", "");
	}
	
	@Override
	public String getTopic() {
		return channel.getConfig().getString("topic", "");
	}
	
	@Override
	public void setColour(String colour) {
		channel.getConfig().set("display-colour", colour);
	}
	
	@Override
	public void setFormat(String format) {
		channel.getConfig().set("format", format);
	}
	
	@Override
	public void setPassword(String password) {
		channel.getConfig().set("password", password);
	}
	
	@Override
	public void setTag(String tag) {
		channel.getConfig().set("tag", tag);
	}
	
	@Override
	public void setTopic(String topic) {
		channel.getConfig().set("topic", topic);
	}
}