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

package com.titankingdoms.dev.titanchat.api.help;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;

public class HelpIndex implements HelpSection {
	
	protected final TitanChat plugin;
	
	private final String title;
	
	private String description = "";
	
	private final Map<String, HelpSection> sections;
	
	public HelpIndex(String title) {
		Validate.notEmpty(title, "Title cannot be empty");
		
		this.plugin = TitanChat.instance();
		this.title = title;
		this.sections = new TreeMap<String, HelpSection>();
	}
	
	public void addSection(HelpSection section) {
		Validate.notNull(section, "Addon cannot be null");
		Validate.isTrue(!contains(section), "Addon already registered");
		
		sections.put(section.getTitle().toLowerCase(), section);
	}
	
	public boolean contains(String title) {
		return title != null && !title.isEmpty() && sections.containsKey(title.toLowerCase());
	}
	
	public boolean contains(HelpSection section) {
		return section != null && contains(section.getTitle()) && getSection(section.getTitle()).equals(section);
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	public HelpSection getSection(String title) {
		Validate.notEmpty(title, "Title cannot be empty");
		return sections.get(title.toLowerCase());
	}
	
	public List<HelpSection> getSections() {
		return new LinkedList<HelpSection>(sections.values());
	}
	
	@Override
	public String getContent(int page) {
		StringBuilder content = new StringBuilder();
		
		List<HelpSection> sections = new ArrayList<HelpSection>(this.sections.values());
		
		int start = (page - 1) * 6;
		int end = start + 6;
		
		if (end > sections.size())
			end = sections.size();
		
		for (int count = start; count < end; count++) {
			if (content.length() > 0)
				content.append('\n');
			
			HelpSection section = sections.get(count);
			
			String title = section.getTitle();
			String description = section.getDescription();
			
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
		int count = sections.size() / 6;
		
		if ((sections.size() % 6) != 0)
			count++;
		
		return count;
	}
	
	@Override
	public final String getTitle() {
		return title;
	}
	
	public void removeSection(HelpSection section) {
		Validate.notNull(section, "Addon cannot be null");
		Validate.isTrue(contains(section), "Addon not registered");
		
		sections.remove(section.getTitle().toLowerCase());
	}
	
	protected void setDescription(String description) {
		this.description = (description != null) ? description : "";
	}
}