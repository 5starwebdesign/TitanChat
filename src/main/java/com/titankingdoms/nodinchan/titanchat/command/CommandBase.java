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

package com.titankingdoms.nodinchan.titanchat.command;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import com.nodinchan.ncbukkit.loader.Loadable;
import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.addon.Addon;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;

/**
 * CommandBase - Command base
 * 
 * @author NodinChan
 *
 */
public class CommandBase extends Loadable implements Listener {

	protected final TitanChat plugin;
	
	protected static final Debugger db = new Debugger(3);
	
	protected static final MessageLevel INFO = MessageLevel.INFO;
	protected static final MessageLevel NONE = MessageLevel.NONE;
	protected static final MessageLevel PLUGIN = MessageLevel.PLUGIN;
	protected static final MessageLevel WARNING = MessageLevel.WARNING;
	
	public CommandBase() {
		super("");
		this.plugin = TitanChat.getInstance();
	}
	
	/**
	 * Gets the display name of the offline player
	 * 
	 * @param player The player to get from
	 * 
	 * @return The display name of the player
	 */
	public final String getDisplayName(OfflinePlayer player) {
		if (player.isOnline())
			return player.getPlayer().getDisplayName();
		
		//TODO: When display name support is back, use the display name from database
		
		return player.getName();
	}
	
	/**
	 * Gets the display name of the command sender
	 * 
	 * @param sender The command sender to get from
	 * 
	 * @return If the sender is a player, the display name of the player, otherwise "CONSOLE"
	 */
	public final String getDisplay(CommandSender sender) {
		if (sender instanceof Player)
			return ((Player) sender).getDisplayName();
		
		return sender.getName();
	}
	
	/**
	 * Checks if the sender has the specified permission
	 * 
	 * @param sender The sender to check
	 * 
	 * @param permission The permission to check with
	 * 
	 * @return True if the sender has permission
	 */
	public final boolean hasPermission(CommandSender sender, String permission) {
		if (!(sender instanceof Player))
			return true;
		
		return sender.hasPermission(permission);
	}
	
	/**
	 * Informs the command sender that the argument length is invalid
	 * 
	 * @param sender The sender to inform
	 * 
	 * @param name The name of the command
	 */
	public final void invalidArgLength(CommandSender sender, String name) {
		plugin.send(MessageLevel.WARNING, sender, "Invalid Argument Length");
		usage(sender, name);
	}
	
	/**
	 * Checks if the player is offline
	 * 
	 * @param sender The command sender to inform if the player is offline
	 * 
	 * @param player The player name
	 * 
	 * @return True if the player is offline
	 */
	public final boolean isOffline(CommandSender sender, String player) {
		OfflinePlayer offPlayer = plugin.getOfflinePlayer(player);
		
		if (!offPlayer.isOnline())
			plugin.send(MessageLevel.WARNING, sender, getDisplayName(offPlayer) + " is offline");
		
		return !offPlayer.isOnline();
	}
	
	/**
	 * Registers the addon
	 * 
	 * @param addon The addon to register
	 */
	public final void register(Addon addon) {
		plugin.getManager().getAddonManager().register(addon);
	}
	
	/**
	 * Registers the channel
	 * 
	 * @param channel The channel to register
	 */
	public final void register(Channel channel) {
		plugin.getManager().getChannelManager().register(channel);
	}
	
	/**
	 * Registers the listener
	 * 
	 * @param listener The listener to register
	 */
	public final void register(Listener listener) {
		plugin.register(listener);
	}
	
	/**
	 * Sends the command sender the usage of the command
	 * 
	 * @param sender The command sender to send to
	 * 
	 * @param name The name of the command
	 */
	public final void usage(CommandSender sender, String name) {
		Executor executor = plugin.getManager().getCommandManager().getCommandExecutor(name);
		
		if (!executor.getUsage().equals(""))
			plugin.send(MessageLevel.INFO, sender, "Usage: /titanchat <@><channel> " + executor.getUsage());
	}
}