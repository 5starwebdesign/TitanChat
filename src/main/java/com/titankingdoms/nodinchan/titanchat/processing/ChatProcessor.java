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

package com.titankingdoms.nodinchan.titanchat.processing;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

/**
 * ChatProcessor - Chat process
 * 
 * @author NodinChan
 *
 */
public final class ChatProcessor extends Thread implements Listener {
	
	private final TitanChat plugin;
	
	private final Queue<ChatPacket> chatQueue;
	
	public ChatProcessor() {
		super("TitanChat Chat Processor");
		this.plugin = TitanChat.getInstance();
		this.chatQueue = new ConcurrentLinkedQueue<ChatPacket>();
		start();
	}
	
	/**
	 * Listens to the AsyncPlayerChatEvent
	 * 
	 * @param event AsyncPlayerChatEvent
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		
		ChatProcess process = new ChatProcess(event.getPlayer(), event.getMessage());
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, process);
	}
	
	@Override
	public void run() {
		while (true) {
			while (!chatQueue.isEmpty()) {
				chatQueue.poll().chat();
			}
		}
	}
	
	/**
	 * Queues the chat
	 * 
	 * @param packet The chat to queue
	 */
	public void sendPacket(ChatPacket packet) {
		if (packet != null)
			chatQueue.offer(packet);
	}
}