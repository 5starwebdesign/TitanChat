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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.util.Debugger;
import com.titankingdoms.dev.titanchat.vault.Vault;

/**
 * {@link DebugCommand} - Command for debugging
 * 
 * @author NodinChan
 *
 */
public final class DebugCommand extends Command {
	
	public DebugCommand() {
		super("Debug");
		setAliases("db");
		setArgumentRange(2, 1024);
		setUsage("[start/stop] [id]...");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (args[0].equalsIgnoreCase("start")) {
			List<String> ids = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
			
			for (String id : new ArrayList<String>(ids))
				try { Debugger.startDebug(Integer.valueOf(id)); } catch (Exception e) { ids.remove(id); }
			
			Collections.sort(ids);
			
			sendMessage(sender, "&6Started debugging: " + StringUtils.join(ids, ", "));
			
		} else if (args[0].equalsIgnoreCase("stop")) {
			List<String> ids = Arrays.asList(Arrays.copyOfRange(args, 1, args.length));
			
			for (String id : new ArrayList<String>(ids))
				try { Debugger.stopDebug(Integer.valueOf(id)); } catch (Exception e) { ids.remove(id); }
			
			Collections.sort(ids);
			
			sendMessage(sender, "&6Stopped debugging: " + StringUtils.join(ids, ", "));
			
		} else {
			sendMessage(sender, "&4Invalid action");
		}
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return Vault.hasPermission(sender, "TitanChat.debug");
	}
}