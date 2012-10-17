package com.titankingdoms.nodinchan.titanchat.channel.utility;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Type;

public abstract class UtilityChannel extends Channel {
	
	private final UtilityLoader loader;
	
	public UtilityChannel(String name) {
		super(name, Type.UTILITY);
		this.loader = new UtilityLoader(this);
	}
	
	@Override
	public ChannelLoader getChannelLoader() {
		return loader;
	}
	
	public abstract void onCommand(CommandSender sender, String[] args);
	
	@Override
	public void reload() {}
	
	@Override
	public List<Player> selectRecipants(Player sender, String message) {
		return new ArrayList<Player>();
	}
	
	public final class UtilityLoader extends ChannelLoader {
		
		private final UtilityChannel channel;
		
		public UtilityLoader(UtilityChannel channel) {
			super("Utility:" + channel.getName());
			this.channel = channel;
		}
		
		@Override
		public Channel create(CommandSender sender, String name, Type type) {
			return channel;
		}
		
		@Override
		public Channel load(String name, Type type) {
			return channel;
		}
		
		@Override
		public void reload() {}
	}
}