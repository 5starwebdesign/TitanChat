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
import com.titankingdoms.nodinchan.titanchat.TitanChat.MessageLevel;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;

/**
 * ChatProcess - Process chat
 * 
 * @author NodinChan
 *
 */
public final class ChatProcess implements Runnable {
	
	private final TitanChat plugin;
	
	private final Player sender;
	
	private final String message;
	
	public ChatProcess(Player sender, String message) {
		this.plugin = TitanChat.getInstance();
		this.sender = sender;
		this.message = message;
	}
	
	public void run() {
		Channel channel = plugin.getManager().getChannelManager().getChannel(sender);
		
		if (channel == null) {
			plugin.send(MessageLevel.WARNING, sender, "You are not in a channel, please join one to chat");
			return;
		}
		
		if (plugin.voiceless(sender, channel, true))
			return;
		
		String log = channel.sendMessage(sender, message);
		
		if (log != null && !log.equals(""))
			plugin.chatLog(log);
	}
}