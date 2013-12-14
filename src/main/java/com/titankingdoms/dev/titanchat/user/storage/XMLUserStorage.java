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

package com.titankingdoms.dev.titanchat.user.storage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.user.storage.UserSection;
import com.titankingdoms.dev.titanchat.api.user.storage.UserStorage;
import com.titankingdoms.dev.titanchat.tools.parser.xml.XMLDocument;
import com.titankingdoms.dev.titanchat.tools.parser.xml.XMLElement;
import com.titankingdoms.dev.titanchat.tools.parser.xml.XMLParser;

public final class XMLUserStorage implements UserStorage {
	
	private final TitanChat plugin;
	
	private File xmlFile;
	private XMLDocument xml;
	
	public XMLUserStorage() {
		this.plugin = TitanChat.getInstance();
	}
	
	@Override
	public String getName() {
		return "XML";
	}
	
	public XMLDocument getXML() {
		if (xml == null)
			reloadXML();
		
		return xml;
	}
	
	@Override
	public void load() {}
	
	@Override
	public UserSection loadSection(String name) {
		UserSection user = new UserSection(name);
		
		if (!getXML().hasElementWithName("users"))
			return user;
		
		XMLElement root = getXML().getRoot();
		
		if (!root.getName().equals("users") || root.getElements().size() < 1)
			return user;
		
		for (XMLElement userSection : root.getElementsByName("user")) {
			try {
				if (!userSection.getElementsByName("name").get(0).getValue().equals(name))
					throw new Exception();
				
			} catch (Exception e) {
				continue;
			}
			
			if (userSection.hasElementWithName("conversation")) {
				XMLElement conversation = userSection.getElementsByName("conversation").get(0);
				
				if (conversation.hasElementWithName("current")) {
					XMLElement current = conversation.getElementsByName("current").get(0);
					
					if (current.hasElementWithName("name") && current.hasElementWithName("type")) {
						try {
							String nName = current.getElementsByName("name").get(0).getValue();
							String nType = current.getElementsByName("type").get(0).getValue();
							
							if (!nName.isEmpty() && !nType.isEmpty())
								user.setCurrentNode(nName, nType);
							
						} catch (Exception e) {}
					}
				}
				
				if (conversation.hasElementWithName("nodes")) {
					XMLElement nodes = conversation.getElementsByName("nodes").get(0);
					
					for (XMLElement node : nodes.getElementsByName("node")) {
						if (!node.hasElementWithName("name") || !node.hasElementWithName("type"))
							continue;
						
						try {
							String nName = node.getElementsByName("name").get(0).getValue();
							String nType = node.getElementsByName("type").get(0).getValue();
							
							if (!nName.isEmpty() && !nType.isEmpty())
								user.addNode(nName, nType);
							
						} catch (Exception e) {}
					}
				}
			}
			
			if (userSection.hasElementWithName("metadata")) {
				XMLElement metadata = userSection.getElementsByName("metadata").get(0);
				
				for (XMLElement meta : metadata.getElements())
					try { user.setMetadata(meta.getName(), meta.getValue()); } catch (Exception e) {}
			}
			
			break;
		}
		
		return user;
	}
	
	@Override
	public void reload() {
		reloadXML();
	}
	
	public void reloadXML() {
		if (xmlFile == null)
			xmlFile = new File(plugin.getDataFolder(), "users.xml");
		
		try { xml = XMLParser.parse(new FileInputStream(xmlFile)); } catch (FileNotFoundException e) {}
	}
	
	@Override
	public void save() {
		saveXML();
	}
	
	@Override
	public void saveSampleStorage() {}
	
	public void saveXML() {
		if (xmlFile == null || xml == null)
			return;
		
		try { xml.save(xmlFile); } catch (Exception e) {}
	}
	
	@Override
	public void saveSection(UserSection section) {}
}