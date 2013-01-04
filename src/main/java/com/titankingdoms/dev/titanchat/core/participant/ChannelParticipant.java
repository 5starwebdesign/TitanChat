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

package com.titankingdoms.dev.titanchat.core.participant;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.util.Debugger;
import com.titankingdoms.dev.titanchat.vault.Vault;

public final class ChannelParticipant extends Participant {
	
	private final Debugger db = new Debugger(2, "ChannelParticipant");
	
	public ChannelParticipant(String name) {
		super(name);
	}
	
	@Override
	public CommandSender getCommandSender() {
		return plugin.getServer().getPlayer(getName());
	}
	
	@Override
	public boolean hasPermission(String permission) {
		if (!isOnline())
			return false;
		
		return Vault.hasPermission(getCommandSender(), permission);
	}
	
	@Override
	public boolean isOnline() {
		return plugin.getServer().getOfflinePlayer(getName()).isOnline();
	}
	
	@Override
	public void send(String... messages) {
		if (!isOnline())
			return;
		
		for (String message : messages)
			db.i("@" + getName() + ": " + message);
		
		getCommandSender().sendMessage(messages);
	}
	
	public void setDisplayName(String displayName) {
		super.setDisplayName(displayName);
		
		if (isOnline())
			((Player) getCommandSender()).setDisplayName(displayName);
	}
}