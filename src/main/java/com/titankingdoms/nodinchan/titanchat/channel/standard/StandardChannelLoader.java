package com.titankingdoms.nodinchan.titanchat.channel.standard;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.channel.Channel.Option;

public final class StandardChannelLoader implements ChannelLoader {
	
	public Channel create(CommandSender sender, String name, Option option) {
		StandardChannel channel = new StandardChannel(name, option, this);
		
		if (sender instanceof Player)
			channel.getAdmins().add(sender.getName());
		
		return channel;
	}
	
	public String getName() {
		return "Standard";
	}
	
	public Channel load(String name, Option option) {
		StandardChannel channel = new StandardChannel(name, option, this);
		
		if (channel.getConfig().get("admins") != null)
			channel.getAdmins().addAll(channel.getConfig().getStringList("admins"));
		
		if (channel.getConfig().get("blacklist") != null)
			channel.getBlacklist().addAll(channel.getConfig().getStringList("blacklist"));
		
		if (channel.getConfig().get("followers") != null)
			channel.getFollowers().addAll(channel.getConfig().getStringList("followers"));
		
		if (channel.getConfig().get("whitelist") != null)
			channel.getWhitelist().addAll(channel.getConfig().getStringList("whitelist"));
		
		return channel;
	}
}