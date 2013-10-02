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

package com.titankingdoms.dev.titanchat.api.format.var;

import java.util.*;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.Manager;
import com.titankingdoms.dev.titanchat.api.event.ConverseEvent;

public final class VarFormat implements Manager<Var> {
	
	private final TitanChat plugin;
	
	private final Pattern pattern = Pattern.compile("%\\w+");
	
	private final Map<String, Var> vars;
	
	public VarFormat() {
		this.plugin = TitanChat.getInstance();
		this.vars = new HashMap<String, Var>();
	}
	
	@Override
	public Var get(String name) {
		return (has(name)) ? vars.get(name.toLowerCase()) : null;
	}
	
	@Override
	public Collection<Var> getAll() {
		return new HashSet<Var>(vars.values());
	}
	
	public String getDefaultFormat() {
		return plugin.getConfig().getString("var.format.default", "%var%");
	}
	
	public String getFormat(String name) {
		return plugin.getConfig().getString("var.format.vars." + name.toLowerCase(), getDefaultFormat());
	}
	
	@Override
	public String getName() {
		return "VarFormat";
	}
	
	public Var getVar(String name) {
		return get(name);
	}
	
	public Collection<Var> getVars() {
		return getAll();
	}
	
	@Override
	public boolean has(String name) {
		return (name != null) ? vars.containsKey(name.toLowerCase()) : false;
	}
	
	@Override
	public boolean has(Var var) {
		return (var != null && has(var.getName())) ? get(var.getName()).equals(var) : false;
	}
	
	public boolean hasVar(String name) {
		return has(name);
	}
	
	public boolean hasVar(Var var) {
		return has(var);
	}
	
	@Override
	public void init() {}
	
	@Override
	public void load() {}
	
	@Override
	public List<String> match(String name) {
		if (name == null || name.isEmpty())
			return new ArrayList<String>(vars.keySet());
		
		List<String> matches = new ArrayList<String>();
		
		for (String var : vars.keySet()) {
			if (!var.startsWith(name.toLowerCase()))
				continue;
			
			matches.add(var);
		}
		
		Collections.sort(matches);
		
		return matches;
	}
	
	public String parse(ConverseEvent event) {
		StringBuffer format = new StringBuffer();
		Matcher match = pattern.matcher(event.getFormat());
		
		while (match.find()) {
			String replacement = match.group();
			
			if (has(match.group())) {
				String value = get(match.group()).getValue(event.clone());
				
				if (value == null)
					value = "";
				
				replacement = (!value.isEmpty()) ? getFormat(match.group()).replace("%var%", value) : "";
			}
			
			match.appendReplacement(format, Matcher.quoteReplacement(replacement));
		}
		
		return match.appendTail(format).toString();
	}
	
	@Override
	public void registerAll(Var... vars) {
		if (vars == null)
			return;
		
		for (Var var : vars) {
			if (var == null)
				continue;
			
			if (has(var)) {
				plugin.log(Level.WARNING, "Duplicate: " + var);
				continue;
			}
			
			this.vars.put(var.getName().toLowerCase(), var);
		}
	}
	
	@Override
	public void reload() {
		unload();
		load();
	}
	
	@Override
	public void unload() {}
	
	@Override
	public void unregister(Var var) {
		if (var == null || !has(var))
			return;
		
		this.vars.remove(var.getName().toLowerCase());
	}
}