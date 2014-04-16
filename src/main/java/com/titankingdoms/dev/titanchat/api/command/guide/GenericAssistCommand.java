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

package com.titankingdoms.dev.titanchat.api.command.guide;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.command.Command;
import com.titankingdoms.dev.titanchat.utility.FormatUtils;
import com.titankingdoms.dev.titanchat.utility.Messaging;
import com.titankingdoms.dev.titanchat.utility.FormatUtils.Format;

public final class GenericAssistCommand extends Command {
	
	private final Command command;
	
	public GenericAssistCommand(Command command) {
		super((command != null) ? "?" : "");
		setSupportRegistration(false);
		this.command = command;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		Assistance assistance = command.getAssistance();
		
		int max = assistance.getPageCount();
		
		String title = assistance.getTitle();
		
		int page = 1;
		
		if (max > 1) {
			if (args.length > 0)
				page = NumberUtils.toInt(args[0], 1);
			
			if (page < 1)
				page = 1;
			
			if (page > max)
				page = max;
			
			title += " (" + page + "/" + max + ")";
		}
		
		Messaging.message(sender, Format.AZURE + StringUtils.center(" " + title + " ", 50, '='));
		
		for (String content : FormatUtils.wrap(Format.AZURE + assistance.getContent(page), 50))
			Messaging.message(sender, content);
	}
}