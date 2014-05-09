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

package com.titankingdoms.dev.titanchat.command.main;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.command.Command;

public final class ConverseCommand extends Command {
	
	public ConverseCommand(String label) {
		super("Converse");
		setAliases("chat", "speak", "talk");
		setArgumentRange(1, 10240);
		setDescription("Converses through/with the viewing node");
		setSyntax("<message>");
	}
	
	@Override
	public void invokeExecution(CommandSender sender, String[] args) {}
}