package com.titankingdoms.nodinchan.titanchat.channel.utility;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.channel.ChannelInfo;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Access;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Range;

public final class BroadcastChannel extends UtilityChannel {
	
	private final BroadcastInfo info;
	
	public BroadcastChannel() {
		super("Broadcast");
		this.info = new BroadcastInfo(this);
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
		return access.equals(Access.SPEAK);
	}
	
	@Override
	public void onCommand(CommandSender sender, String[] args) {
		
	}
	
	public final class BroadcastInfo extends ChannelInfo {

		public BroadcastInfo(BroadcastChannel channel) {
			super(channel);
		}
		
		@Override
		public String[] getAliases() {
			return new String[0];
		}
		
		@Override
		public String getColour() {
			return "";
		}
		
		@Override
		public String getFormat() {
			return plugin.getConfig().getString("utilities.broadcast.player-format", "[&cBroadcast&f] %player&f: %message");
		}
		
		@Override
		public String getPassword() {
			return "";
		}
		
		@Override
		public String getTag() {
			return "";
		}
		
		@Override
		public String getTopic() {
			return "";
		}
		
		@Override
		public void setColour(String colour) {}
		
		@Override
		public void setFormat(String format) {
			plugin.getConfig().set("utilities.broadcast.player-format", format);
		}
		
		@Override
		public void setPassword(String password) {}
		
		@Override
		public void setTag(String tag) {}
		
		@Override
		public void setTopic(String topic) {}
	}
}