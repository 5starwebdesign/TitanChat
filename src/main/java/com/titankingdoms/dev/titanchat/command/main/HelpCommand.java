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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.command.Command;
import com.titankingdoms.dev.titanchat.api.command.guide.GenericAssistCommand;
import com.titankingdoms.dev.titanchat.api.guide.Chapter;
import com.titankingdoms.dev.titanchat.api.guide.Enchiridion;
import com.titankingdoms.dev.titanchat.api.guide.AbstractIndex;
import com.titankingdoms.dev.titanchat.tools.Format;

public final class HelpCommand extends Command {
	
	public HelpCommand() {
		super("?");
		setAliases("help", "h");
		setArgumentRange(0, 10240);
		setDescription("Assistance for TitanChat");
		setSyntax("<chapter|index>...");
		register(new GenericAssistCommand(this));
	}
	
	@Override
	protected void execute(CommandSender sender, String[] args) {
		if (!plugin.getSystem().isLoaded(Enchiridion.class)) {
			message(sender, Format.RED + "Enchiridion not found");
			return;
		}
		
		Enchiridion enchiridion = plugin.getSystem().getModule(Enchiridion.class);
		
		Chapter chapter = enchiridion;
		
		int page = 1;
		
		for (String arg : args) {
			if (!NumberUtils.isNumber(arg)) {
				if (!AbstractIndex.class.isInstance(chapter))
					break;
				
				chapter = AbstractIndex.class.cast(chapter).getChapter(arg);
				continue;
			}
			
			page = NumberUtils.toInt(arg, 0);
			break;
		}
		
		if (chapter == null) {
			message(sender, Format.RED + "Assistance cannot be provided for requested chapter");
			return;
		}
		
		int max = chapter.getPageCount();
		
		String title = chapter.getTitle();
		
		if (max > 1) {
			if (page < 1)
				page = 1;
			
			if (page > max)
				page = max;
			
			title += " (" + page + "/" + max + ")";
		}
		
		message(sender, Format.AZURE + StringUtils.center(" " + title + " ", 50, '='));
		
		for (String line : Format.wrap(Format.AZURE + chapter.getContent(page), 50))
			message(sender, line);
	}
}