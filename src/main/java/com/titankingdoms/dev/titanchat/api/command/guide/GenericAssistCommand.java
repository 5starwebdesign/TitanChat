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

package com.titankingdoms.dev.titanchat.api.command.guide;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.command.Command;
import com.titankingdoms.dev.titanchat.tools.Format;

public final class GenericAssistCommand extends Command {
	
	private final Command command;
	
	public GenericAssistCommand(Command command) {
		super((command != null) ? "?" : "");
		setArgumentRange(0, 2);
		setSyntax("[index|list] [index]");
		setSupportRegistration(false);
		this.command = command;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		Assistance assistance = command.getAssistance();
		
		boolean list = args.length > 0 && args[0].equalsIgnoreCase("list");
		
		String title = assistance.getTitle();
		
		int max = (list) ? assistance.getListCount() : assistance.getPageCount();
		int page = 1;
		
		if (max > 1) {
			switch (args.length) {
			
			case 0:
				break;
				
			case 1:
				page = (!list) ? NumberUtils.toInt(args[0], 1) : 1;
				break;
				
			default:
				page = NumberUtils.toInt((!list) ? args[0] : args[1], 1);
				break;
			}
			
			title += " (" + page + "/" + max + ")";
		}
		
		if (page < 1)
			page = 1;
		
		if (page > max)
			page = max;
		
		String content = (list) ? assistance.getCommands(page) : assistance.getContent(page);
		
		message(sender, Format.AZURE + StringUtils.center(" " + title + " ", 50, '='));
		
		for (String line : Format.wrap(Format.AZURE + content, 50))
			message(sender, line);
	}
}