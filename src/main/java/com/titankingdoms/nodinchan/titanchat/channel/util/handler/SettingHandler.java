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

package com.titankingdoms.nodinchan.titanchat.channel.util.handler;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.util.handler.Handler.HandlerInfo;

//TODO: Remove the package and write something better

/**
 * SettingHandler - Abstract setting handler for changing settings in channels
 * 
 * @author NodinChan
 *
 */
public abstract class SettingHandler {
	
	protected final TitanChat plugin;
	
	protected final Channel channel;
	
	private final String setting;
	
	private final HandlerInfo info;
	
	public SettingHandler(Channel channel, String setting, HandlerInfo info) {
		this.plugin = TitanChat.getInstance();
		this.channel = channel;
		this.setting = setting;
		this.info = info;
	}
	
	/**
	 * Gets the handler info
	 * 
	 * @return The info of the handler
	 */
	public final HandlerInfo getInfo() {
		return info;
	}
	
	/**
	 * Gets the setting to change
	 * 
	 * @return The setting to change
	 */
	public final String getSetting() {
		return setting;
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
	 */
	public final void invalidArgLength(CommandSender sender) {
		plugin.send(MessageLevel.WARNING, sender, "Invalid Argument Length");
		usage(sender);
	}
	
	public abstract void set(CommandSender sender, String[] args);
	
	/**
	 * Sends the usage of the command
	 * 
	 * @param sender The sender to send to
	 */
	public final void usage(CommandSender sender) {
		if (info.getUsage().isEmpty())
			plugin.send(MessageLevel.WARNING, sender, "Usage: /titanchat <@><channel> set [setting] <arguments>");
		else
			plugin.send(MessageLevel.WARNING, sender, "Usage: /titanchat <@><channel> set " + info.getUsage());
	}
}