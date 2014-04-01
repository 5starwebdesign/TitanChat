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
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import com.google.common.collect.ImmutableSet;
import com.titankingdoms.dev.titanchat.api.Manager;

public final class HelpProvider extends HelpIndex implements Manager<HelpSection> {
	
	private final Map<String, HelpSection> sections;
	
	private final Set<String> dependencies = ImmutableSet.<String>builder().build();
	
	public HelpProvider() {
		super("General");
		this.sections = new TreeMap<String, HelpSection>();
	}
	
	@Override
	public HelpSection get(String title) {
		return getSection(title);
	}
	
	@Override
	public Collection<HelpSection> getAll() {
		return getSections();
	}
	
	@Override
	public Collection<String> getDependencies() {
		return dependencies;
	}
	
	@Override
	public String getDescription() {
		return "The General Help Index";
	}
	
	@Override
	public String getName() {
		return "HelpCentre";
	}
	
	@Override
	public boolean has(String title) {
		return contains(title);
	}
	
	@Override
	public boolean has(HelpSection section) {
		return contains(section);
	}
	
	@Override
	public void load() {}
	
	@Override
	public Collection<String> match(String title) {
		if (title == null || title.isEmpty())
			return new ArrayList<String>(sections.keySet());
		
		List<String> matches = new ArrayList<String>();
		
		for (String match : sections.keySet()) {
			if (!match.startsWith(title))
				continue;
			
			matches.add(match);
		}
		
		Collections.sort(matches);
		return matches;
	}
	
	@Override
	public void register(HelpSection section) {
		addSection(section);
	}
	
	@Override
	public void reload() {}
	
	@Override
	protected void setDescription(String description) {}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(HelpSection section) {
		removeSection(section);
	}
}