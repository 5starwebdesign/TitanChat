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

package com.titankingdoms.dev.titanchat.tools.xml;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class XMLObject {
	
	private final String name;
	
	private final Map<String, String> attributes;
	
	public XMLObject(String name) {
		this.name = name;
		this.attributes = new LinkedHashMap<String, String>();
	}
	
	public void addAttribute(String name, String value) {
		if (name == null || hasAttribute(name) || value == null)
			return;
		
		this.attributes.put(name, value);
	}
	
	public String getAttribute(String name) {
		return (hasAttribute(name)) ? attributes.get(name) : null;
	}
	
	public List<Entry<String, String>> getAttributes() {
		return new LinkedList<Entry<String, String>>(attributes.entrySet());
	}
	
	public final String getName() {
		return name;
	}
	
	public XMLType getType() {
		return XMLType.UNKNOWN;
	}
	
	public boolean hasAttribute(String name) {
		return (name != null) ? attributes.containsKey(name) : false;
	}
	
	public boolean isType(XMLType type) {
		return getType().equals(type);
	}
	
	public void removeAttribute(String name) {
		if (name == null || !hasAttribute(name))
			return;
		
		this.attributes.remove(name);
	}
	
	public enum XMLType { DOCUMENT, ELEMENT, SECTION, UNKNOWN }
}