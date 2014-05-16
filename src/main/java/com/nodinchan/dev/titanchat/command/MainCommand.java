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

package com.nodinchan.dev.titanchat.command;

import java.util.Arrays;

import org.bukkit.command.CommandSender;

import com.nodinchan.dev.titanchat.api.command.Command;
import com.nodinchan.dev.tools.Format;

public final class MainCommand extends Command {
	
	public MainCommand() {
		super("TitanChat");
		setAliases("tc", "tchat");
		setArgumentRange(0, 10240);
		setDescription("Main commands of TitanChat");
		setSyntax("[command]");
	}
	
	@Override
	public void invokeExecution(CommandSender sender, String[] args) {
		if (args.length < 1) {
			message(sender, Format.GOLD + "You are running v" + plugin.getDescription().getVersion());
			message(sender, Format.GOLD + "Type \"/titanchat ?\" for help");
			return;
		}
		
		callProgressiveExecution(sender, args[0], Arrays.copyOfRange(args, 1, args.length));
	}
}