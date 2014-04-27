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

package com.titankingdoms.dev.titanchat.api.guide;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;

public class Index implements Chapter {
	
	protected final TitanChat plugin;
	
	private final String title;
	
	private String description = "";
	
	private final Map<String, Chapter> chapters;
	
	public Index(String title) {
		Validate.notEmpty(title, "Title cannot be empty");
		
		this.plugin = TitanChat.instance();
		this.title = title;
		this.chapters = new TreeMap<String, Chapter>();
	}
	
	public void addChapter(Chapter chapter) {
		Validate.notNull(chapter, "Chapter cannot be null");
		Validate.isTrue(!contains(chapter), "Chapter already registered");
		
		chapters.put(chapter.getTitle().toLowerCase(), chapter);
	}
	
	public boolean contains(String title) {
		return title != null && !title.isEmpty() && chapters.containsKey(title.toLowerCase());
	}
	
	public boolean contains(Chapter chapter) {
		return chapter != null && contains(chapter.getTitle()) && getChapter(chapter.getTitle()).equals(chapter);
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	public Chapter getChapter(String title) {
		Validate.notEmpty(title, "Title cannot be empty");
		return chapters.get(title.toLowerCase());
	}
	
	public List<Chapter> getChapters() {
		return new LinkedList<Chapter>(chapters.values());
	}
	
	@Override
	public String getContent(int page) {
		StringBuilder content = new StringBuilder();
		
		List<Chapter> chapters = new ArrayList<Chapter>(this.chapters.values());
		
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
	
	public int getPageCount() {
		int count = chapters.size() / 6;
		
		if ((chapters.size() % 6) != 0)
			count++;
		
		return count;
	}
	
	@Override
	public final String getTitle() {
		return title;
	}
	
	public void removeChapter(Chapter chapter) {
		Validate.notNull(chapter, "Chapter cannot be null");
		Validate.isTrue(contains(chapter), "Chapter not registered");
		
		chapters.remove(chapter.getTitle().toLowerCase());
	}
	
	protected void setDescription(String description) {
		this.description = (description != null) ? description : "";
	}
}