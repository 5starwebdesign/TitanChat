/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
 * 
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 * 
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 * 
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.titankingdoms.nodinchan.titanchat.channel.standard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.util.Participant;

/**
 * StandardChannel - Standard channel
 * 
 * @author NodinChan
 *
 */
public final class StandardChannel extends Channel {
	
	private final ChannelLoader creator;
	
	public StandardChannel(String name, Option option, StandardChannelLoader creator) {
		super(name, option);
		this.creator = creator;
	}
	
	@Override
	public boolean access(Player player) {
		if (!player.hasPermission("TitanChat.join." + getName()))
			return false;
		
		if (getOption().equals(Option.STAFF) && !plugin.isStaff(player))
			return false;
		
		if (getBlacklist().contains(player.getName()))
			return false;
		
		if (getInfo().whitelistOnly() && !getWhitelist().contains(player.getName()))
			return false;
		
		return true;
	}
	
	@Override
	public ChannelLoader getLoader() {
		return creator;
	}
	
	@Override
	public String sendMessage(Player sender, String message) {
		List<Player> recipants = new ArrayList<Player>();
		
		switch (getInfo().range()) {
		
		case CHANNEL:
			for (Participant participant : getParticipants())
				if (participant.getPlayer() != null)
					recipants.add(participant.getPlayer());
			break;
			
		case GLOBAL:
			recipants.addAll(Arrays.asList(plugin.getServer().getOnlinePlayers()));
			break;
			
		case LOCAL:
			for (Entity entity : sender.getNearbyEntities(getInfo().radius(), getInfo().radius(), getInfo().radius()))
				if (entity instanceof Player)
					recipants.add((Player) entity);
			break;
			
		case WORLD:
			for (Player recipant : sender.getWorld().getPlayers())
				recipants.add(recipant);
			break;
		}
		
		for (String follower : getFollowers()) {
			Player following = plugin.getPlayer(follower);
			
			if (following != null && !recipants.contains(following))
				recipants.add(following);
		}
		
		return sendMessage(sender, recipants, message);
	}
}