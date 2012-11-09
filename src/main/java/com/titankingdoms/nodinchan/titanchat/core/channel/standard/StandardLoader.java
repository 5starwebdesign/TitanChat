package com.titankingdoms.nodinchan.titanchat.core.channel.standard;

import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;

public class StandardLoader extends ChannelLoader {
	
	public StandardLoader() {
		super("Standard");
	}
	
	@Override
	public Channel create(CommandSender sender, String name, Type type) {
		StandardChannel channel = new StandardChannel(name, type, this);
		
		channel.getConfig().options().copyDefaults(true);
		channel.saveConfig();
		
		return channel;
	}
	
	@Override
	public Channel load(String name, Type type) {
		StandardChannel channel = new StandardChannel(name, type, this);
		
		if (channel.getConfig().get("admins") != null)
			channel.getAdmins().addAll(channel.getConfig().getStringList("admins"));
		
		if (channel.getConfig().get("blacklist") != null)
			channel.getBlacklist().addAll(channel.getConfig().getStringList("blacklist"));
		
		if (channel.getConfig().get("whitelist") != null)
			channel.getWhitelist().addAll(channel.getConfig().getStringList("whitelist"));
		
		return channel;
	}
	
	@Override
	public void reload() {
		// TODO Auto-generated method stub
		
	}
}