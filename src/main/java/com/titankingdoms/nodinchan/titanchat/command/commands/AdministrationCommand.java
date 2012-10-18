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

package com.titankingdoms.nodinchan.titanchat.command.commands;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel.Option;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelManager;
import com.titankingdoms.nodinchan.titanchat.channel.util.Participant;
import com.titankingdoms.nodinchan.titanchat.command.CommandBase;
import com.titankingdoms.nodinchan.titanchat.command.info.*;

/**
 * AdministrationCommand - Commands for Channel administration
 * 
 * @author NodinChan
 *
 */
public class AdministrationCommand extends CommandBase {

	private ChannelManager cm;
	
	public AdministrationCommand() {
		this.cm = plugin.getManager().getChannelManager();
	}
	
	/**
	 * Ban Command - Bans the player from the channel
	 */
	@Command
	@CommandOption(requireChannel = true)
	@Aliases("b")
	@Description("Bans the player from the channel")
	@Usage("ban [player]")
	public void ban(CommandSender sender, Channel channel, String[] args) {
		if (channel.getCommand("") != null) {
			channel.getCommand("").onCommand(sender, args);
			return;
		}
		
		try {
			OfflinePlayer targetPlayer = plugin.getPlayer(args[0]);
			
			if (isOffline(sender, args[0]))
				targetPlayer = plugin.getOfflinePlayer(args[0]);
			
			if (!channel.getOption().equals(Option.NONE)) {
				plugin.send(WARNING, sender, "Command disabled for default and staff channels");
				return;
			}
			
			if (channel.isAdmin(sender.getName()) || hasPermission(sender, "TitanChat.ban." + channel.getName())) {
				if (!channel.isBlacklisted(sender.getName())) {
					channel.getBlacklist().add(targetPlayer.getName());
					channel.getAdmins().remove(targetPlayer.getName());
					channel.save();
					
					if (channel.isParticipating(targetPlayer.getName()))
						channel.leave(targetPlayer.getName());
					
					if (targetPlayer.isOnline())
						plugin.send(WARNING, targetPlayer.getPlayer(), "You have been banned from " + channel.getName());
					
					if (!channel.isParticipating(sender.getName()))
						plugin.send(INFO, sender, getDisplayName(targetPlayer) + " has been banned");
					
					plugin.send(INFO, channel, getDisplayName(targetPlayer) + " has been banned");
					
				} else { plugin.send(WARNING, sender, getDisplayName(targetPlayer) + " has already been banned"); }
				
			} else { plugin.send(WARNING, sender, "You do not have permission"); }
			
		} catch (IndexOutOfBoundsException e) { invalidArgLength(sender, "ban"); }
	}
	
	/**
	 * Force Command - Forces the player to join the channel
	 */
	@Command
	@CommandOption(requireChannel = true)
	@Description("Forces the player to join the channel")
	@Usage("force [player]")
	public void force(CommandSender sender, Channel channel, String[] args) {
		if (channel.getCommand("") != null) {
			channel.getCommand("").onCommand(sender, args);
			return;
		}
		
		try {
			Player targetPlayer = plugin.getPlayer(args[0]);
			
			if (isOffline(sender, args[0]))
				return;
			
			if (hasPermission(sender, "TitanChat.force")) {
				if (!channel.isParticipating(targetPlayer.getName())) {
					channel.join(targetPlayer);
					plugin.send(INFO, targetPlayer, "You have been forced to join " + channel.getName());
					plugin.send(INFO, sender, getDisplayName(targetPlayer) + " has been forced to join the channel");
					
				} else { plugin.send(WARNING, sender, getDisplayName(targetPlayer) + " is already in the channel"); }
				
			} else { plugin.send(WARNING, sender, "You do not have permission"); }
			
		} catch (IndexOutOfBoundsException e) { invalidArgLength(sender, "force"); }
	}
	
	/**
	 * Kick Command - Kicks the player from the channel
	 */
	@Command
	@CommandOption(requireChannel = true)
	@Aliases("k")
	@Description("Kicks the player from the channel")
	@Usage("kick [player]")
	public void kick(CommandSender sender, Channel channel, String[] args) {
		if (channel.getCommand("") != null) {
			channel.getCommand("").onCommand(sender, args);
			return;
		}
		
		try {
			OfflinePlayer targetPlayer = plugin.getPlayer(args[0]);
			
			if (isOffline(sender, args[0]))
				targetPlayer = plugin.getOfflinePlayer(args[0]);
			
			if (channel.isAdmin(sender.getName()) || hasPermission(sender, "TitanChat.kick." + channel.getName())) {
				if (channel.isParticipating(targetPlayer.getName())) {
					channel.leave(targetPlayer.getName());
					
					if (targetPlayer.isOnline())
						plugin.send(WARNING, targetPlayer.getPlayer(), "You have been kicked from " + channel.getName());
					
					if (!channel.isParticipating(sender.getName()))
						plugin.send(INFO, sender, getDisplayName(targetPlayer) + " has been kicked");
					
					plugin.send(INFO, channel, getDisplayName(targetPlayer) + " has been kicked");
					
				} else { plugin.send(WARNING, sender, getDisplayName(targetPlayer) + " is not in the channel"); }
				
			} else { plugin.send(WARNING, sender, "You do not have permission"); }
			
		} catch (IndexOutOfBoundsException e) { invalidArgLength(sender, "kick"); }
	}
	
	/**
	 * Mute Command - Mutes the player on the channel
	 */
	@Command
	@CommandOption(requireChannel = true)
	@Description("Mutes the player on the channel")
	@Usage("mute [player]")
	public void mute(CommandSender sender, Channel channel, String[] args) {
		if (channel.getCommand("") != null) {
			channel.getCommand("").onCommand(sender, args);
			return;
		}
		
		try {
			OfflinePlayer targetPlayer = plugin.getPlayer(args[0]);
			
			if (isOffline(sender, args[0]))
				targetPlayer = plugin.getOfflinePlayer(args[0]);
			
			ChannelParticipant participant = cm.getParticipant(targetPlayer.getName());
			
			if (participant == null && targetPlayer.isOnline())
				participant = cm.loadParticipant(targetPlayer.getPlayer());
			else
				return;
			
			if (channel.isAdmin(sender.getName()) || hasPermission(sender, "TitanChat.mute." + channel.getName())) {
				if (!participant.isMuted(channel)) {
					participant.mute(channel, true);
					
					if (targetPlayer.isOnline())
						plugin.send(WARNING, targetPlayer.getPlayer(), "You have been muted in " + channel.getName());
					
					if (!channel.isParticipating(sender.getName()))
						plugin.send(INFO, sender, getDisplayName(targetPlayer) + " has been muted");
					
					plugin.send(INFO, channel, getDisplayName(targetPlayer) + " has been muted");
					
				} else { plugin.send(WARNING, sender, getDisplayName(targetPlayer) + " has already been muted"); }
				
			} else { plugin.send(WARNING, sender, "You do not have permission"); }
			
		} catch (IndexOutOfBoundsException e) { invalidArgLength(sender, "mute"); }
	}
	
	/**
	 * Unban Command - Unbans the player from the channel
	 */
	@Command
	@CommandOption(requireChannel = true)
	@Aliases("ub")
	@Description("Unbans the player from the channel")
	@Usage("unban [player]")
	public void unban(CommandSender sender, Channel channel, String[] args) {
		if (channel.getCommand("") != null) {
			channel.getCommand("").onCommand(sender, args);
			return;
		}
		
		try {
			OfflinePlayer targetPlayer = plugin.getPlayer(args[0]);
			
			if (isOffline(sender, args[0]))
				targetPlayer = plugin.getOfflinePlayer(args[0]);
			
			if (!channel.getOption().equals(Option.NONE)) {
				plugin.send(WARNING, sender, "Command disabled for default and staff channels");
				return;
			}
			
			if (channel.isAdmin(sender.getName()) || hasPermission(sender, "TitanChat.ban." + channel.getName())) {
				if (channel.isBlacklisted(targetPlayer.getName())) {
					channel.getBlacklist().remove(targetPlayer.getName());
					channel.save();
					
					if (targetPlayer.isOnline())
						plugin.send(INFO, targetPlayer.getPlayer(), "You have been unbanned from " + channel.getName());
					
					if (sender instanceof Player && !channel.isParticipating(sender.getName()))
						plugin.send(INFO, sender, getDisplayName(targetPlayer) + " has been unbanned");
					
					plugin.send(INFO, channel, getDisplayName(targetPlayer) + " has been unbanned");
					
				} else { plugin.send(WARNING, sender, getDisplayName(targetPlayer) + " is not banned"); }
				
			} else { plugin.send(WARNING, sender, "You do not have permission"); }
			
		} catch (IndexOutOfBoundsException e) { invalidArgLength(sender, "unban"); }
	}
	
	/**
	 * Unmute Command - Unmutes the player on the channel
	 */
	@Command
	@CommandOption(requireChannel = true)
	@Description("Unmutes the player on the channel")
	@Usage("unmute [player]")
	public void unmute(CommandSender sender, Channel channel, String[] args) {
		if (channel.getCommand("") != null) {
			channel.getCommand("").onCommand(sender, args);
			return;
		}
		
		try {
			OfflinePlayer targetPlayer = plugin.getPlayer(args[0]);
			
			if (isOffline(sender, args[0]))
				targetPlayer = plugin.getOfflinePlayer(args[0]);
			
			ChannelParticipant participant = cm.getParticipant(targetPlayer.getName());
			
			if (participant == null && targetPlayer.isOnline())
				participant = cm.loadParticipant(targetPlayer.getPlayer());
			else
				return;
			
			if (channel.isAdmin(sender.getName()) || hasPermission(sender, "TitanChat.mute." + channel.getName())) {
				if (!participant.isMuted(channel)) {
					participant.mute(channel, false);
					
					if (targetPlayer.isOnline())
						plugin.send(WARNING, targetPlayer.getPlayer(), "You have been muted in " + channel.getName());
					
					if (!channel.isParticipating(sender.getName()))
						plugin.send(INFO, sender, getDisplayName(targetPlayer) + " has been muted");
					
					plugin.send(INFO, channel, getDisplayName(targetPlayer) + " has been muted");
					
				} else { plugin.send(WARNING, sender, getDisplayName(targetPlayer) + " has already been muted"); }
				
			} else { plugin.send(WARNING, sender, "You do not have permission"); }
			
		} catch (IndexOutOfBoundsException e) { invalidArgLength(sender, "unmute"); }
	}
}