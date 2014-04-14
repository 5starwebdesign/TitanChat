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
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.help.HelpIndex;
import com.titankingdoms.dev.titanchat.utility.FormatUtils;
import com.titankingdoms.dev.titanchat.utility.Messaging;
import com.titankingdoms.dev.titanchat.utility.FormatUtils.Format;

public final class AssistanceCommand extends Command {
	
	private final Command command;
	
	public AssistanceCommand(Command command) {
		super((command != null) ? "?" : "");
		setSupportRegistration(false);
		this.command = command;
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		HelpIndex assistance = command.getAssistance();
		
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
	
	public static final class Assistance extends HelpIndex {
		
		private final Command command;

		public Assistance(Command command) {
			super((command != null) ? command.getLabel() : "");
			this.command = command;
		}
		
		@Override
		public String getContent(int page) {
			String description = command.getDescription();
			String aliases = StringUtils.join(command.getAliases(), ", ");
			String range = "[" + command.getMinArguments() + ", " + command.getMaxArguments() + "]";
			String syntax = command.getCanonicalSyntax();
			
			StringBuilder content = new StringBuilder();
			
			content.append("Description: ").append(description).append("\n");
			content.append("Aliases: ").append(aliases).append("\n");
			content.append("Argument Range: ").append(range).append("\n");
			content.append("Syntax: ").append(syntax);
			
			return content.toString();
		}
		
		@Override
		public String getDescription() {
			return command.getDescription();
		}
		
		@Override
		public int getPageCount() {
			return 1;
		}
	}
}