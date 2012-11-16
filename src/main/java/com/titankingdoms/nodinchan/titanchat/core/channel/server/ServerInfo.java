package com.titankingdoms.nodinchan.titanchat.core.channel.server;

import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelInfo;

public class ServerInfo extends ChannelInfo {
	
	public ServerInfo(ServerChannel channel) {
		super(channel);
	}
	
	@Override
	public String[] getAliases() {
		return new String[0];
	}
	
	@Override
	public String getColour() {
		return plugin.getConfig().getString("channels.server.display-colour", "");
	}
	
	@Override
	public String getFormat() {
		return plugin.getConfig().getString("formatting.format", "%tag %prefix%player%suffix: %message");
	}
	
	@Override
	public String getPassword() {
		return "";
	}
	
	@Override
	public String getTag() {
		return plugin.getConfig().getString("channels.server.tag", "");
	}
	
	@Override
	public String getTopic() {
		return plugin.getConfig().getString("channels.server.topic", "");
	}
	
	@Override
	public void setColour(String colour) {
		plugin.getConfig().set("channels.server.display-colour", colour);
	}
	
	@Override
	public void setFormat(String format) {
		plugin.getConfig().set("formatting.format", format);
	}
	
	@Override
	public void setPassword(String password) {}
	
	@Override
	public void setTag(String tag) {
		plugin.getConfig().set("channels.server.tag", tag);
	}
	
	@Override
	public void setTopic(String topic) {
		plugin.getConfig().set("channels.server.topic", topic);
	}
}
