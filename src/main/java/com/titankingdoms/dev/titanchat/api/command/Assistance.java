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

package com.titankingdoms.dev.titanchat.api.command;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.help.HelpIndex;
import com.titankingdoms.dev.titanchat.utility.Messaging;

public final class Assistance extends Command {
	
	private final Command command;
	
	public Assistance(Command command) {
		super((command != null) ? "?" : "");
		this.command = command;
		setRegistrationSupport(false);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		HelpIndex section = command.getHelpSection();
		
		int max = section.getPageCount();
		
		String title = section.getTitle();
		String content = "";
		
		if (max > 1) {
			int page = 1;
			
			if (args.length > 0)
				try { page = Integer.parseInt(args[0]); } catch (Exception e) {}
			
			if (page < 1)
				page = 1;
			
			if (page > max)
				page = max;
			
			title += " (" + page + "/" + max + ")";
			content = section.getContent(page);
			
		} else {
			content = section.getContent();
		}
		
		Messaging.message(sender, StringUtils.center(" " + title + " ", 55, '='));
		Messaging.message(sender, content);
		Messaging.message(sender, "=======================================================");
	}
}