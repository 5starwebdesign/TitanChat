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

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.command.Command;
import com.titankingdoms.dev.titanchat.api.command.CommandManager;
import com.titankingdoms.dev.titanchat.api.guide.Chapter;
import com.titankingdoms.dev.titanchat.api.guide.AbstractIndex;

public final class CommandIndex extends AbstractIndex {
	
	public CommandIndex() {
		super("Commands");
	}
	
	@Override
	public void addChapter(Chapter chapter) {}
	
	public void index() {
		if (!TitanChat.system().isLoaded(CommandManager.class))
			return;
		
		CommandManager manager = TitanChat.system().getModule(CommandManager.class);
		
		for (Chapter chapter : getChapters()) {
			if (manager.has(chapter.getTitle()))
				continue;
			
			super.removeChapter(chapter.getTitle());
		}
		
		for (Command command : manager.getAll()) {
			if (contains(command.getLabel()))
				super.removeChapter(command.getLabel());
			
			super.addChapter(command.getAssistance());
		}
	}
	
	@Override
	public void removeChapter(String title) {}
}