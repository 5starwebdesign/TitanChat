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

package com.titankingdoms.dev.titanchat.command.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.util.Messaging;
import com.titankingdoms.dev.titanchat.util.VaultUtils;

public final class ReloadCommand extends Command {
	
	public ReloadCommand(String name) {
		super("Reload");
		setAliases("rl");
		setDescription("Reload TitanChat");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (sender instanceof Player)
			Messaging.sendMessage(sender, "&6TitanChat is reloading...");
		
		plugin.onReload();
		
		if (sender instanceof Player)
			Messaging.sendMessage(sender, "&6TitanChat is reloaded");
	}
	
	@Override
	public boolean isPermitted(CommandSender sender, String[] args) {
		return VaultUtils.hasPermission(sender, "TitanChat.reload");
	}
}