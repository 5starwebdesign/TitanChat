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

package com.titankingdoms.dev.titanchat.api.command.assistance;

import org.apache.commons.lang.StringUtils;

import com.titankingdoms.dev.titanchat.api.command.Command;
import com.titankingdoms.dev.titanchat.api.guide.Index;

public final class CommandAssistance extends Index {
	
	private final Command command;
	
	public CommandAssistance(Command command) {
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
	
	@Override
	public void setDescription(String description) {}
}