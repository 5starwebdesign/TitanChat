/*
 *     Copyright (C) 2013  Nodin Chan
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

package com.titankingdoms.dev.titanchat.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.EndPoint;
import com.titankingdoms.dev.titanchat.core.user.User;
import com.titankingdoms.dev.titanchat.core.user.UserManager;
import com.titankingdoms.dev.titanchat.core.user.participant.Participant;
import com.titankingdoms.dev.titanchat.event.ConverseEvent;
import com.titankingdoms.dev.titanchat.format.TagParser;

public final class TitanChatListener implements Listener {
	
	private final TitanChat plugin;
	
	private final String DEFAULT_FORMAT = "%prefix%display%suffix: %message";
	
	public TitanChatListener() {
		this.plugin = TitanChat.getInstance();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		
		User sender = plugin.getManager(UserManager.class).getUser(event.getPlayer());
		EndPoint recipient = sender.getCurrentEndPoint();
		String format = plugin.getConfig().getString("format.converse", DEFAULT_FORMAT);
		String message = event.getMessage();
		
		if (recipient == null)
			recipient = sender;
		
		ConverseEvent ce = new ConverseEvent(sender, recipient.getRelayPoints(), format, message);
		
		if (!ce.getRecipients().contains(sender))
			ce.getRecipients().add(sender);
		
		plugin.getServer().getPluginManager().callEvent(ce);
		
		String line = plugin.getManager(TagParser.class).parse(ce).replace("%message", ce.getMessage());
		
		if (!ce.getRecipients().contains(sender))
			ce.getRecipients().add(sender);
		
		for (EndPoint relay : ce.getRecipients())
			relay.sendRawLine(line);
		
		if (ce.getRecipients().size() < 2)
			sender.sendNotice("Nobody heard you...");
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		UserManager manager = plugin.getManager(UserManager.class);
		User user = new Participant(event.getPlayer());
		manager.registerAll(user);
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerQuit(PlayerQuitEvent event) {
		UserManager manager = plugin.getManager(UserManager.class);
		User user = manager.get(event.getPlayer().getName());
		manager.unregister(user);
	}
}