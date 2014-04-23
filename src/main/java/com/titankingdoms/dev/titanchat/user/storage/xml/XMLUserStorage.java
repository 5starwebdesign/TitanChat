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

package com.titankingdoms.dev.titanchat.user.storage.xml;

import java.io.File;
import java.io.FileInputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.tools.parser.xml.XMLDocument;
import com.titankingdoms.dev.titanchat.tools.parser.xml.XMLElement;
import com.titankingdoms.dev.titanchat.tools.parser.xml.XMLParser;
import com.titankingdoms.dev.titanchat.user.storage.UserData;
import com.titankingdoms.dev.titanchat.user.storage.UserStorage;

public final class XMLUserStorage implements UserStorage {
	
	private final TitanChat plugin;
	
	private static final String NAME = "XMLStorage";
	
	private File xmlFile;
	private XMLDocument xml;
	
	public XMLUserStorage() {
		this.plugin = TitanChat.instance();
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	public XMLDocument getXML() {
		if (xml == null)
			reloadXML();
		
		return xml;
	}
	
	@Override
	public void loadData(UserData data) {
		Validate.notNull(data, "Data cannot be null");
		
		String id = data.getUniqueId().toString();
		
		reloadXML();
		
		if (!getXML().hasElement("users"))
			getXML().appendElement(new XMLElement("users"));
		
		for (XMLElement user : getXML().getElements("users").get(0).getElements("user")) {
			if (!user.hasAttribute("id") || !user.getAttribute("id").getValue().equals(id))
				continue;
			
			if (user.hasElement("connections")) {
				XMLElement connections = user.getElements("connections").get(0);
				
				if (connections.hasElement("viewing")) {
					XMLElement viewing = connections.getElements("viewing").get(0);
					
					if (viewing.hasValue() && viewing.hasAttribute("type"))
						data.setViewing(viewing.getValue() + "::" + viewing.getAttribute("type").getValue());
				}
				
				if (connections.hasElement("connected")) {
					XMLElement connected = connections.getElements("connected").get(0);
					
					Set<String> nodes = new HashSet<String>();
					
					for (XMLElement node : connected.getElements("node")) {
						if (!node.hasValue() || !node.hasAttribute("type"))
							continue;
						
						nodes.add(node.getValue() + "::" + node.getAttribute("type").getValue());
					}
					
					data.setConnected(nodes);
				}
			}
			
			if (user.hasElement("metadata")) {
				Map<String, String> metadata = new HashMap<String, String>();
				
				for (XMLElement meta : user.getElements("metadata").get(0).getElements()) {
					if (!meta.hasValue())
						continue;
					
					metadata.put(meta.getName(), meta.getValue());
				}
				
				data.setMetadata(metadata);
			}
			
			break;
		}
	}
	
	public void reloadXML() {
		if (xmlFile == null)
			xmlFile = new File(plugin.getDataFolder(), "users.xml");
		
		try { xml = XMLParser.parse(new FileInputStream(xmlFile)); } catch (Exception e) {}
	}
	
	@Override
	public void saveData(UserData data) {
		Validate.notNull(data, "Data cannot be null");
		
		String id = data.getUniqueId().toString();
		
		if (!getXML().hasElement("users"))
			getXML().appendElement(new XMLElement("users"));
		
		for (XMLElement element : getXML().getElements("users").get(0).getElements("user")) {
			if (!element.hasAttribute("id") || !element.getAttribute("id").getValue().equals(id))
				continue;
			
			element.remove();
		}
		
		XMLElement user = new XMLElement("user").appendAttribute("id", id);
		
		XMLElement connections = user.getElements("connections").get(0);
		
		for (XMLElement viewing : connections.getElements("viewing"))
			viewing.remove();
		
		if (data.getViewing().contains("::")) {
			String[] v = data.getViewing().split("::");
			
			XMLElement viewing = new XMLElement("viewing");
			
			viewing.appendAttribute("type", v[1]);
			viewing.setValue(v[0]);
			
			connections.appendElement(viewing);
		}
		
		XMLElement connected = new XMLElement("connected");
		
		for (String connection : data.getConnected()) {
			String[] c = connection.split("::");
			
			XMLElement node = new XMLElement("node");
			
			node.appendAttribute("type", c[1]);
			node.setValue(c[0]);
			
			connected.appendElement(node);
		}
		
		connections.appendElement(connected);
		
		XMLElement metadata = new XMLElement("metadata");
		
		for (Entry<String, String> meta : data.getMetadata().entrySet())
			metadata.appendElement(new XMLElement(meta.getKey()).setValue(meta.getValue()));
		
		user.appendElement(metadata);
		
		getXML().getElements("users").get(0).appendElement(user);
		
		saveXML();
	}
	
	public void saveXML() {
		if (xml == null || xmlFile == null)
			return;
		
		try { xml.save(xmlFile); } catch (Exception e) {}
	}
}