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

package com.titankingdoms.dev.titanchat.command.commands.whitelist;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelManager;

public final class View extends Command {
	
	public View() {
		super("View");
		setAliases("v");
		setArgumentRange(1, 1);
		setDescription("View the whitelist of the channel");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {}
	
	@Override
	public List<String> tab(CommandSender sender, String[] args) {
		List<String> tabCompletions = new ArrayList<String>();
		
		switch (args.length) {
		
		case 1:
			for (Channel channel : plugin.getManager(ChannelManager.class).getAll()) {
				if (!channel.getName().startsWith(args[0]))
					continue;
				
				tabCompletions.add(channel.getName());
			}
			break;
		}
		
		return tabCompletions;
	}
}