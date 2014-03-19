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

package com.titankingdoms.dev.titanchat.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.command.Command;

public final class HelpCommand extends Command {
	
	public HelpCommand() {
		super("?");
		setAliases("help", "h");
		setArgumentRange(0, 1024);
		setDescription("Help for and information about TitanChat");
		setRegistrationSupport(false);
		setSyntax("[command] [arguments]");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		if (args.length < 1) {
			
		}
		
		plugin.getServer().dispatchCommand(sender, StringUtils.join(args, " ") + " ?");
	}
}