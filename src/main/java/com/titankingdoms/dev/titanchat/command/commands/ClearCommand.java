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

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.util.Messaging;

public final class ClearCommand extends Command {
	
	public ClearCommand() {
		super("Clear");
		setAliases("clr");
		setArgumentRange(0, 1);
		setDescription("Clear chat");
		getLayer().registerAll(new Scrollback());
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (!(sender instanceof Player)) {
			Messaging.sendNotice(sender, "You cannot clear the console");
			return;
		}
		
		if (args.length < 1)
			Messaging.sendNotice(sender, StringUtils.repeat("\n", 20));
		else
			getLayer().execute(sender, args);
	}
	
	@Override
	public List<String> tab(CommandSender sender, String[] args) {
		return getLayer().tab(sender, args);
	}
	
	public final class Scrollback extends Command {
		
		public Scrollback() {
			super("Scrollback");
			setAliases("s");
			setArgumentRange(0, 0);
			setDescription("Clear chat including scrollback");
		}
		
		@Override
		public void execute(CommandSender sender, String[] args) {
			if (!(sender instanceof Player)) {
				Messaging.sendNotice(sender, "You cannot clear the console");
				return;
			}
			
			Messaging.sendNotice(sender, StringUtils.repeat("\n", 100));
		}
	}
}