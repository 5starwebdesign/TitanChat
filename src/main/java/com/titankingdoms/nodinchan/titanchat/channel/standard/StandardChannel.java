package com.titankingdoms.nodinchan.titanchat.channel.standard;

import java.util.List;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelInfo;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Access;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Range;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Type;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public final class StandardChannel extends Channel {
	
	private final StandardLoader loader;
	private final StandardInfo info;
	
	public StandardChannel(String name, Type type, StandardLoader loader) {
		super(name, type);
		this.loader = loader;
		this.info = new StandardInfo(this);
	}

	@Override
	public ChannelLoader getChannelLoader() {
		return loader;
	}

	@Override
	public ChannelInfo getInfo() {
		return info;
	}
	
	@Override
	public Range getRange() {
		Range range = Range.fromName(getInfo().getSetting("range", "channel"));
		return (range != null) ? range : Range.CHANNEL;
	}
	
	@Override
	public boolean hasAccess(Player player, Access access) {
		if (player == null)
			return true;
		
		switch (access) {
		
		case BAN:
		case UNBAN:
			return player.hasPermission("TitanChat.ban." + getName()) || isAdmin(player);
			
		case DEMOTE:
		case PROMOTE:
		case UNWHITELIST:
		case WHITELIST:
			return player.hasPermission("TitanChat.rank." + getName()) || isAdmin(player);
			
		case JOIN:
			if (getType().equals(Type.STAFF) && !plugin.isStaff(player))
				return false;
			
			if (isBlacklisted(player))
				return false;
			
			if (getInfo().getSetting("whitelist", false) && !isWhitelisted(player))
				return false;
			
			return player.hasPermission("TitanChat.join." + getName());
			
		case KICK:
			return player.hasPermission("TitanChat.kick." + getName()) || isAdmin(player);
			
		case LEAVE:
			return player.hasPermission("TitanChat.leave." + getName());
			
		case MUTE:
		case UNMUTE:
			return player.hasPermission("TitanChat.mute." + getName()) || isAdmin(player);
			
		case SPEAK:
			return player.hasPermission("TitanChat.speak." + getName());
		}
		
		return false;
	}

	@Override
	public void reload() {
		reloadConfig();
		getConfig().set("admins", getAdmins());
		getConfig().set("blacklist", getBlacklist());
		getConfig().set("whitelist", getWhitelist());
		getConfig().set("followers", getFollowers());
		saveConfig();
	}
	
	@Override
	public List<Participant> selectRecipants(Player sender, String message) {
		return getParticipants();
	}
}