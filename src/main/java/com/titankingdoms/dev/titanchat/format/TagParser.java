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

package com.titankingdoms.dev.titanchat.format;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.titankingdoms.dev.titanchat.Manager;
import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.event.ConverseEvent;

public abstract class TagParser implements Manager<Tag> {
	
	protected final TitanChat plugin;
	
	protected final Pattern pattern = Pattern.compile("(?i)(%)([a-z0-9]+)");
	
	public TagParser() {
		this.plugin = TitanChat.getInstance();
	}
	
	public final Tag getTag(String name) {
		return get(name);
	}
	
	public final List<Tag> getTags() {
		return getAll();
	}
	
	public final boolean hasTag(String name) {
		return has(name);
	}
	
	public final boolean hasTag(Tag tag) {
		return has(tag);
	}
	
	public String parse(ConverseEvent event) {
		StringBuffer format = new StringBuffer();
		Matcher match = pattern.matcher(event.getFormat());
		
		while (match.find()) {
			String replacement = match.group();
			
			if (hasTag(replacement))
				replacement = getTag(replacement).getValue(event);
			
			match.appendReplacement(format, (replacement != null) ? replacement : "");
		}
		
		return match.appendTail(format).toString();
	}
}