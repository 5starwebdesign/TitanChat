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

package com.nodinchan.dev.titanchat.tools.vault;

import com.nodinchan.dev.titanchat.TitanChat;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.permission.Permission;

public final class Chat_TitanChat extends Chat {
	
	private static final String NAME = "TitanChat";
	
	private final TitanChat plugin;
	
	public Chat_TitanChat(Permission perm) {
		super(perm);
		this.plugin = TitanChat.instance();
	}
	
	@Override
	public boolean getGroupInfoBoolean(String world, String group, String node, boolean def) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public double getGroupInfoDouble(String world, String group, String node, double def) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public int getGroupInfoInteger(String world, String group, String node, int def) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public String getGroupInfoString(String world, String group, String node, String def) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public String getGroupPrefix(String world, String group) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public String getGroupSuffix(String world, String group) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean getPlayerInfoBoolean(String world, String player, String node, boolean def) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public double getPlayerInfoDouble(String world, String player, String node, double def) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getPlayerInfoInteger(String world, String player, String node, int def) {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public String getPlayerInfoString(String world, String player, String node, String def) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getPlayerPrefix(String world, String player) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String getPlayerSuffix(String world, String player) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	@Override
	public void setGroupInfoBoolean(String world, String group, String node, boolean value) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public void setGroupInfoDouble(String world, String group, String node, double value) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public void setGroupInfoInteger(String world, String group, String node, int value) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public void setGroupInfoString(String world, String group, String node, String value) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public void setGroupPrefix(String world, String group, String prefix) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public void setGroupSuffix(String world, String group, String suffix) {
		throw new UnsupportedOperationException("TitanChat does not support group info nodes");
	}
	
	@Override
	public void setPlayerInfoBoolean(String world, String player, String node, boolean value) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void setPlayerInfoDouble(String world, String player, String node, double value) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void setPlayerInfoInteger(String world, String player, String node, int value) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void setPlayerInfoString(String world, String player, String node, String value) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void setPlayerPrefix(String world, String player, String prefix) {
		// TODO Auto-generated method stub

	}
	
	@Override
	public void setPlayerSuffix(String world, String player, String suffix) {
		// TODO Auto-generated method stub

	}
}