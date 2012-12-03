package com.titankingdoms.nodinchan.titanchat.core.channel.utility;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelInfo;
import com.titankingdoms.nodinchan.titanchat.core.channel.Range;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;

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
	
	public final class BroadcastCommand extends Command {
		
		public BroadcastCommand(String name) {
			super("Broadcast");
			setAliases("bc");
			setArgumentRange(1, 10240);
		}
		
		@Override
		public void execute(CommandSender sender, Channel channel, String[] args) {
			
		}
		
		@Override
		public boolean permissionCheck(CommandSender sender, Channel channel) {
			return hasPermission(sender, "TitanChat.broadcast");
		}
	}
}