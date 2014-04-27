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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.api.Manager;

public final class Enchiridion extends Index implements Manager<Chapter> {
	
	private static final String DESC = "The General Index";
	private static final String NAME = "Enchiridion";
	
	private static final Set<String> DEPENDENCIES = ImmutableSet.<String>builder().build();
	
	private final Map<String, Chapter> chapters;
	
	public Enchiridion() {
		super("General");
		this.chapters = new TreeMap<String, Chapter>();
	}
	
	@Override
	public Chapter get(String title) {
		return getChapter(title);
	}
	
	@Override
	public Collection<Chapter> getAll() {
		return getChapters();
	}
	
	@Override
	public Collection<String> getDependencies() {
		return DEPENDENCIES;
	}
	
	@Override
	public String getDescription() {
		return DESC;
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public boolean has(String title) {
		return contains(title);
	}
	
	@Override
	public boolean has(Chapter chapter) {
		return contains(chapter);
	}
	
	@Override
	public void load() {}
	
	@Override
	public Collection<String> match(String title) {
		if (title == null || title.isEmpty())
			return new ArrayList<String>(chapters.keySet());
		
		List<String> matches = new ArrayList<String>();
		
		for (String match : chapters.keySet()) {
			if (!match.startsWith(title))
				continue;
			
			matches.add(match);
		}
		
		Collections.sort(matches);
		return matches;
	}
	
	@Override
	public void register(Chapter chapter) {
		addChapter(chapter);
	}
	
	@Override
	public void reload() {}
	
	@Override
	protected void setDescription(String description) {}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(Chapter chapter) {
		removeChapter(chapter);
	}
}