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

package com.titankingdoms.dev.titanchat.command.titanchat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.command.Command;
import com.titankingdoms.dev.titanchat.api.help.HelpIndex;
import com.titankingdoms.dev.titanchat.api.help.HelpProvider;
import com.titankingdoms.dev.titanchat.api.help.HelpSection;
import com.titankingdoms.dev.titanchat.utility.FormatUtils;
import com.titankingdoms.dev.titanchat.utility.Messaging;
import com.titankingdoms.dev.titanchat.utility.FormatUtils.Format;

public final class HelpCommand extends Command {
	
	public HelpCommand() {
		super("?");
		setAliases("help", "h");
		setArgumentRange(0, 10240);
		setDescription("Assistance for TitanChat");
		setSyntax("<section|index>...");
		setRegistrationSupport(false);
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		HelpProvider provider = plugin.getManager(HelpProvider.class);
		
		if (provider == null) {
			Messaging.message(sender, "Assistance cannot be provided at this time");
			return;
		}
		
		HelpSection section = provider;
		
		int page = -1;
		
		for (String arg : args) {
			if (!NumberUtils.isNumber(arg)) {
				if (!HelpIndex.class.isInstance(section))
					break;
				
				section = HelpIndex.class.cast(section).getSection(arg);
				continue;
			}
			
			page = NumberUtils.toInt(arg, 0);
			break;
		}
		
		if (section == null) {
			Messaging.message(sender, Format.RED + "Assistance cannot be provided for requested section");
			return;
		}
		
		int max = section.getPageCount();
		
		String title = section.getTitle();
		
		if (max > 1) {
			if (page < 1)
				page = 1;
			
			if (page > max)
				page = max;
			
			title += " (" + page + "/" + max + ")";
		}
		
		String content = (page < 0) ? section.getContent() : section.getContent(page);
		
		Messaging.message(sender, Format.AZURE + StringUtils.center(" " + title + " ", 50, '='));
		Messaging.message(sender, FormatUtils.wrap(Format.AZURE + content, 50));
	}
}