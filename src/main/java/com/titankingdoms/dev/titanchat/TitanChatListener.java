/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.format.Format;
import com.titankingdoms.dev.titanchat.util.Messaging;
import com.titankingdoms.dev.titanchat.util.update.UpdateVerify;

/**
 * {@link TitanChatListener} - The listener of TitanChat
 * 
 * @author NodinChan
 *
 */
public final class TitanChatListener implements Listener {
	
	private final TitanChat plugin;
	
	private final String site = "http://dev.bukkit.org/server-mods/titanchat/";
	private final UpdateVerify update;
	
	public TitanChatListener() {
		this.plugin = TitanChat.getInstance();
		this.update = new UpdateVerify(site + "files.rss", "4.1");
	}
	
	/**
	 * Processes chat
	 * 
	 * @param event {@link AsyncPlayerChatEvent}
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		if (event == null)
			return;
		
		event.setCancelled(true);
		
		Participant participant = plugin.getParticipantManager().getParticipant(event.getPlayer());
		String message = event.getMessage();
		
		if (message.startsWith("@") && message.split(" ").length > 1) {
			Channel channel = plugin.getChannelManager().getChannel(message.split(" ")[0].substring(1));
			
			if (channel == null) {
				Messaging.sendMessage(event.getPlayer(), "&4Channel does not exist");
				return;
			}
			
			participant.chatOut(channel, message.substring(message.indexOf(" ") + 1, message.length()));
			
		} else { participant.chatOut(message); }
	}
	
	/**
	 * Registers {@link Player}s on join
	 * 
	 * @param event {@link PlayerJoinEvent}
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (event == null)
			return;
		
		Participant participant = plugin.getParticipantManager().getParticipant(event.getPlayer());
		
		if (!plugin.getConfig().getBoolean("update-verify", true))
			return;
		
		if (participant.hasPermission("TitanChat.update")) {
			if (update.verify()) {
				participant.sendMessage("&6" + update.getNewVersion() + " &5is out!");
				participant.sendMessage("&5You are running &6" + update.getCurrentVersion());
				participant.sendMessage("&5Update at &9" + site);
			}
		}
	}
	
	/**
	 * Unregisters the {@link Player}s on quit
	 * 
	 * @param event {@link PlayerQuitEvent}
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (event == null)
			return;
		
		Participant participant = plugin.getParticipantManager().getParticipant(event.getPlayer());
		plugin.getParticipantManager().unregisterParticipant(participant);
	}
	
	/**
	 * Colourises text on signs
	 * 
	 * @param event {@link SignChangeEvent}
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		if (event == null)
			return;
		
		for (int line = 0; line < 4; line++)
			event.setLine(line, Format.colourise(event.getLine(line)));
	}
}