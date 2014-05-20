/*
 *     Copyright (C) 2014  Nodin Chan
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
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.nodinchan.dev.titanchat.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.nodinchan.dev.conversation.Network;
import com.nodinchan.dev.conversation.Node;
import com.nodinchan.dev.titanchat.TitanChat;
import com.nodinchan.dev.titanchat.api.conversation.user.User;
import com.nodinchan.dev.titanchat.api.conversation.user.UserManager;
import com.nodinchan.dev.tools.Format;

public final class TitanChatListener implements Listener {
	
	private final TitanChat plugin;
	
	public TitanChatListener() {
		this.plugin = TitanChat.instance();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		if (!plugin.getSystem().isLoaded(UserManager.class))
			return;
		
		UserManager manager = plugin.getSystem().getModule(UserManager.class);
		
		event.setCancelled(true);
		
		User user = manager.getByUniqueId(event.getPlayer().getUniqueId());
		Node viewing = user.getViewing();
		
		if (viewing == null) {
			user.sendLine(Format.RED + "Please join a Node to converse");
			return;
		}
		
		Network.post(plugin, viewing.getConversation(user, event.getMessage()));
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (!plugin.getSystem().isLoaded(UserManager.class))
			return;
		
		UserManager manager = plugin.getSystem().getModule(UserManager.class);
		
		if (manager.isRegistered(event.getPlayer()))
			return;
		
		manager.onJoin(event.getPlayer());
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		if (!plugin.getSystem().isLoaded(UserManager.class))
			return;
		
		UserManager manager = plugin.getSystem().getModule(UserManager.class);
		
		if (!manager.isRegistered(event.getPlayer()))
			return;
		
		manager.onQuit(event.getPlayer());
	}
}