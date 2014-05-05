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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.titankingdoms.dev.titanchat.api.Module;

public final class Enchiridion extends Index implements Module {
	
	private static final String DESC = "The General Index";
	private static final String NAME = "Enchiridion";
	
	private static final String[] DEPENDENCIES = new String[0];
	
	private boolean loaded;
	
	public Enchiridion() {
		super("General");
		this.loaded = false;
	}
	
	public Chapter get(String title) {
		return getChapter(title);
	}
	
	public List<Chapter> getAll() {
		return getChapters();
	}
	
	@Override
	public String[] getDependencies() {
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
	
	public boolean has(String title) {
		return contains(title);
	}
	
	public boolean has(Chapter chapter) {
		return contains(chapter);
	}
	
	@Override
	public boolean isLoaded() {
		return loaded;
	}
	
	@Override
	public void load() {}
	
	public List<String> match(String title) {
		if (title == null || title.isEmpty())
			return getContent();
		
		Builder<String> matches = ImmutableList.builder();
		
		for (String match : getContent()) {
			if (!match.startsWith(title))
				continue;
			
			matches.add(match);
		}
		
		return matches.build();
	}
	
	@Override
	public void reload() {}
	
	@Override
	protected void setDescription(String description) {}
	
	@Override
	public void setLoaded(boolean loaded) {
		if (this.loaded == loaded)
			return;
		
		this.loaded = loaded;
		
		if (loaded)
			load();
		else
			unload();
	}
	
	@Override
	public void unload() {}
}