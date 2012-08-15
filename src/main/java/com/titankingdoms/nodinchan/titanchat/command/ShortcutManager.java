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

import java.util.HashMap;
import java.util.Map;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.channel.Channel;

public final class ShortcutManager {
	
	private final TitanChat plugin;
	
	private final Map<String, Shortcut> shortcuts;
	
	public ShortcutManager() {
		this.plugin = TitanChat.getInstance();
		this.shortcuts = new HashMap<String, Shortcut>();
	}
	
	public void handled(Player player, String shortcut, String[] args) {
		if (!shortcuts.containsKey(shortcut.toLowerCase()))
			return;
		
		shortcuts.get(shortcut.toLowerCase()).run(player, args);
	}
	
	public void load(Channel channel) {
		if (channel.getConfig().getConfigurationSection("command") == null)
			return;
		
		for (String command : channel.getConfig().getConfigurationSection("command").getKeys(false)) {
			if (!plugin.getManager().getCommandManager().hasCommand(command))
				continue;
			
			String shortcut = channel.getConfig().getString("command." + command);
			
			if (shortcuts.containsKey(shortcut))
				continue;
			
			Shortcut sc = new Shortcut(shortcut, command, channel.getName());
			shortcuts.put(shortcut.toLowerCase(), sc);
		}
	}
	
	public final class Shortcut {
		
		private final String shortcut;
		
		private final String command;
		
		private final String channel;
		
		public Shortcut(String shortcut, String command, String channel) {
			this.shortcut = shortcut;
			this.command = command;
			this.channel = channel;
		}
		
		public String getShortcut() {
			return shortcut;
		}
		
		public void run(Player player, String[] args) {
			StringBuilder arguments = new StringBuilder();
			
			for (String arg : args) {
				if (arguments.length() > 0)
					arguments.append(" ");
				
				arguments.append(arg);
			}
			
			String command = "titanchat @" + channel + " " + this.command;
			plugin.getServer().dispatchCommand(player, command + " " + arguments.toString().trim());
		}
	}
}