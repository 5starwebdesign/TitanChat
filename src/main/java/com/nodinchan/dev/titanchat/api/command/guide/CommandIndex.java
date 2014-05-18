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

package com.nodinchan.dev.titanchat.api.command.guide;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.nodinchan.dev.guide.AbstractChapter;
import com.nodinchan.dev.guide.Chapter;
import com.nodinchan.dev.guide.Index;
import com.nodinchan.dev.titanchat.TitanChat;
import com.nodinchan.dev.titanchat.api.command.Command;
import com.nodinchan.dev.titanchat.api.command.CommandManager;

public final class CommandIndex extends AbstractChapter implements Index {
	
	private final Map<String, Chapter> chapters;
	
	public CommandIndex() {
		super("Commands");
		this.chapters = new LinkedHashMap<>();
	}
	
	@Override
	public void addChapter(Chapter chapter) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public boolean contains(String title) {
		return title != null && !title.isEmpty() && chapters.containsKey(title.toLowerCase());
	}
	
	@Override
	public Chapter getChapter(String title) {
		return (title == null || title.isEmpty()) ? null : chapters.get(title.toLowerCase());
	}
	
	@Override
	public List<Chapter> getChapters() {
		return ImmutableList.copyOf(chapters.values());
	}
	
	@Override
	public String getContent(int page) {
		StringBuilder content = new StringBuilder();
		
		List<Chapter> chapters = getChapters();
		
		int start = (page - 1) * 6;
		int end = start + 6;
		
		if (end > chapters.size())
			end = chapters.size();
		
		for (int count = start; count < end; count++) {
			if (content.length() > 0)
				content.append('\n');
			
			Chapter chapter = chapters.get(count);
			
			String title = chapter.getTitle();
			String description = chapter.getDescription();
			
			content.append(title);
			
			if (!description.isEmpty()) {
				content.append(" - ");
				
				if (description.length() > 52 - title.length())
					content.append(description.substring(0, 49 - title.length()) + "...");
				else
					content.append(description);
			}
		}
		
		return content.toString();
	}
	
	@Override
	public List<String> getContentList() {
		return ImmutableList.copyOf(chapters.keySet());
	}
	
	@Override
	public int getPageCount() {
		int count = chapters.size() / 6;
		
		if ((chapters.size() % 6) != 0)
			count++;
		
		return count;
	}
	
	public void index() {
		if (!TitanChat.system().isLoaded(CommandManager.class))
			return;
		
		chapters.clear();
		
		CommandManager manager = TitanChat.system().getModule(CommandManager.class);
		
		for (Command command : manager.getAll())
			chapters.put(command.getLabel().toLowerCase(), command.getAssistance());
	}
	
	@Override
	public void removeChapter(String title) {
		throw new UnsupportedOperationException();
	}
	
	@Override
	public List<String> search(String title) {
		if (title == null || title.isEmpty())
			return getContentList();
		
		Builder<String> matches = ImmutableList.builder();
		
		for (String match : getContentList()) {
			if (!match.startsWith(title))
				continue;
			
			matches.add(match);
		}
		
		return matches.build();
	}
}