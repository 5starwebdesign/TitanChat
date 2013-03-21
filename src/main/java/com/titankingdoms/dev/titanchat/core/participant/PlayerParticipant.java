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

package com.titankingdoms.dev.titanchat.core.participant;

import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.vault.Vault;

public final class PlayerParticipant extends Participant {
	
	public PlayerParticipant(Player player) {
		super(player.getName());
	}
	
	@Override
	public Player asCommandSender() {
		return plugin.getServer().getPlayer(getName());
	}
	
	@Override
	public String getDisplayName() {
		String name = super.getDisplayName();
		
		if (isOnline() && asCommandSender().getDisplayName().equals(getName()))
			asCommandSender().setDisplayName(name);
		else
			setDisplayName(asCommandSender().getDisplayName());
		
		return super.getDisplayName();
	}
	
	@Override
	public String getPrefix() {
		String prefix = Vault.getPlayerPrefix(asCommandSender());
		return (prefix != null && !prefix.isEmpty()) ? prefix : super.getPrefix();
	}
	
	@Override
	public String getSuffix() {
		String suffix = Vault.getPlayerSuffix(asCommandSender());
		return (suffix != null && !suffix.isEmpty()) ? suffix : super.getSuffix();
	}
	
	@Override
	public void setDisplayName(String name) {
		super.setDisplayName(name);
		
		if (isOnline() && !asCommandSender().getDisplayName().equals(name))
			asCommandSender().setDisplayName(name);
	}
	
	@Override
	public Participant toParticipant() {
		return this;
	}
}