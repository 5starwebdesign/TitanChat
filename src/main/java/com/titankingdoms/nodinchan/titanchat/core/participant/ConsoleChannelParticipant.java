/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.core.participant;

import java.util.logging.Level;

import org.bukkit.command.CommandSender;

public final class ConsoleChannelParticipant extends Participant {
	
	public ConsoleChannelParticipant() {
		super("CONSOLE");
	}
	
	@Override
	public CommandSender getCommandSender() {
		return plugin.getServer().getConsoleSender();
	}
	
	@Override
	public boolean hasPermission(String permission) {
		return true;
	}
	
	@Override
	public boolean isMuted(String channel) {
		return false;
	}
	
	@Override
	public boolean isOnline() {
		return true;
	}
	
	@Override
	public void mute(String channel, boolean mute) {}
	
	@Override
	public void send(String... messages) {
		for (String message : messages)
			plugin.log(Level.INFO, message);
	}
}