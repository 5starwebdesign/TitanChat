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

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.Validate;

import com.google.common.collect.ImmutableList;

public abstract class AbstractIndex implements Index {
	
	private final String title;
	
	private String description;
	
	private final Map<String, Chapter> chapters;
	
	public AbstractIndex(String title) {
		this.title = title;
		this.description = "";
		this.chapters = new TreeMap<>();
	}
	
	public void addChapter(Chapter chapter) {
		Validate.notNull(chapter, "Chapter cannot be null");
		Validate.isTrue(!contains(chapter.getTitle()), "Chapter already registered");
		
		chapters.put(chapter.getTitle().toLowerCase(), chapter);
	}
	
	@Override
	public boolean contains(String title) {
		return title != null && !title.isEmpty() && chapters.containsKey(title.toLowerCase());
	}
	
	@Override
	public Chapter getChapter(String title) {
		return (title != null && !title.isEmpty()) ? chapters.get(title.toLowerCase()) : null;
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
	public List<String> getContentTable() {
		return ImmutableList.copyOf(chapters.keySet());
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
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
	
	@Override
	public void removeChapter(String title) {
		Validate.notEmpty(title, "Title cannot be empty");
		Validate.isTrue(contains(title), "Chapter not registered");
		
		chapters.remove(title.toLowerCase());
	}
	
	public void setDescription(String description) {
		this.description = (description != null) ? description : "";
	}
}