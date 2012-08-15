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

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

public final class ChatPacket {
	
	private final TitanChat plugin;
	
	private final String recipant;
	
	private final String[] chat;
	
	public ChatPacket(Player recipant, String[] chat) {
		this.plugin = TitanChat.getInstance();
		this.recipant = recipant.getName();
		this.chat = chat;
	}
	
	public void chat() {
		Player recipant = plugin.getPlayer(this.recipant);
		
		if (recipant != null)
			recipant.sendMessage(chat);
	}
}