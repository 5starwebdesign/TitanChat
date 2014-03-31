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

import com.titankingdoms.dev.titanchat.api.help.HelpIndex;
import com.titankingdoms.dev.titanchat.utility.FormatUtils;

public final class CommandSection extends HelpIndex {
	
	private final String text;
	
	public CommandSection(Command command) {
		super((command != null) ? command.getLabel() : "");
		
		String description = command.getDescription();
		String aliases = StringUtils.join(command.getAliases(), ", ");
		String range = "[" + command.getMinArguments() + ", " + command.getMaxArguments() + "]";
		String syntax = command.getCanonicalSyntax();
		
		StringBuilder text = new StringBuilder();
		
		text.append(StringUtils.join(FormatUtils.wrap("Description: " + description, 55), '\n') + "\n");
		text.append(StringUtils.join(FormatUtils.wrap("Aliases: " + aliases, 55), '\n') + "\n");
		text.append(StringUtils.join(FormatUtils.wrap("Argument Range: " + range, 55), '\n') + "\n");
		text.append(StringUtils.join(FormatUtils.wrap("Syntax: " + syntax, 55), '\n') + "\n");
		
		this.text = text.toString();
	}
	
	@Override
	public String getContent() {
		return text;
	}
	
	@Override
	public String getContent(int page) {
		return text;
	}
	
	@Override
	public int getPageCount() {
		return 1;
	}
}