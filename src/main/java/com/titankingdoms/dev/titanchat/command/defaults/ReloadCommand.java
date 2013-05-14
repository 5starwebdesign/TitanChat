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

package com.titankingdoms.dev.titanchat.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.util.vault.Vault;

/**
 * {@link ReloadCommand} - Command for reloading TitanChat
 * 
 * @author NodinChan
 *
 */
public final class ReloadCommand extends Command {
	
	public ReloadCommand() {
		super("Reload");
		setAliases("r");
		setDescription("Reload TitanChat");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		sendMessage(sender, "&6TitanChat is now reloading...");
		plugin.onReload();
		sendMessage(sender, "&6TitanChat is now reloaded");
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return Vault.hasPermission(sender, "TitanChat.reload");
	}
}