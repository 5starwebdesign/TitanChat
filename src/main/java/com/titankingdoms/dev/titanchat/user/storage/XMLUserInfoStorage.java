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
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.user.storage.UserInfo;
import com.titankingdoms.dev.titanchat.api.user.storage.UserInfoStorage;
import com.titankingdoms.dev.titanchat.tools.parser.xml.XMLDocument;
import com.titankingdoms.dev.titanchat.tools.parser.xml.XMLElement;
import com.titankingdoms.dev.titanchat.tools.parser.xml.XMLParser;

public final class XMLUserInfoStorage implements UserInfoStorage {
	
	private final TitanChat plugin;
	
	private File xmlFile;
	private XMLDocument xml;
	
	private final Map<String, UserInfo> info;
	
	public XMLUserInfoStorage() {
		this.plugin = TitanChat.instance();
		this.info = new HashMap<String, UserInfo>();
	}
	
	@Override
	public UserInfo get(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		
		if (!info.containsKey(name.toLowerCase()))
			return revise(name);
		
		return info.get(name.toLowerCase());
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
	public void reload() {}
	
	public void reloadXML() {
		if (xmlFile == null)
			xmlFile = new File(plugin.getDataFolder(), "users.xml");
		
		try { xml = XMLParser.parse(new FileInputStream(xmlFile)); } catch (FileNotFoundException e) {}
	}
	
	@Override
	public UserInfo revise(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		
		if (!getXML().hasElementWithName("users"))
			throw new UnsupportedOperationException("Unable to revise without XML entry");
		
		XMLElement root = getXML().getElementsByName("users").get(0);
		
		if (!root.hasElementWithName(name.toLowerCase()))
			throw new UnsupportedOperationException("Unable to revise without XML entry");
		
		XMLUserInfo info = new XMLUserInfo(root.getElementsByName(name.toLowerCase()).get(0));
		this.info.put(name.toLowerCase(), info);
		return info;
	}
	
	@Override
	public void save() {}
	
	@Override
	public void save(UserInfo info) {
		if (!getXML().hasElementWithName("users"))
			getXML().appendElement(new XMLElement("users"));
		
		XMLElement root = getXML().getElementsByName("users").get(0);
		
		if (!root.hasElementWithName(info.getName().toLowerCase()))
			root.appendElement(new XMLElement(info.getName().toLowerCase()));
		
		XMLElement user = root.getElementsByName(info.getName().toLowerCase()).get(0).removeElements();
		
		XMLElement connections = new XMLElement("connections");
		
		XMLElement viewing = new XMLElement("viewing");
		String[] vNode = info.getViewing().split("::");
		
		if (vNode.length > 1) {
			viewing.appendElement(new XMLElement("name").setValue(vNode[0]));
			viewing.appendElement(new XMLElement("type").setValue(vNode[1]));
		}
		
		connections.appendElement(viewing);
		
		XMLElement connected = new XMLElement("connected");
		
		for (String node : info.getConnected()) {
			XMLElement xmlNode = new XMLElement("node");
			String[] cNode = node.split("::");
			
			xmlNode.appendElement(new XMLElement("name").setValue(cNode[0]));
			xmlNode.appendElement(new XMLElement("type").setValue(cNode[1]));
			
			connected.appendElement(xmlNode);
		}
		
		connections.appendElement(connected);
		
		user.appendElement(connections);
		
		XMLElement metadata = new XMLElement("metadata");
		
		for (Entry<String, String> meta : info.getMetadata().entrySet())
			metadata.appendElement(new XMLElement(meta.getKey()).setValue(meta.getValue()));
		
		user.appendElement(metadata);
		
		saveXML();
	}
	
	public void saveXML() {
		if (xmlFile == null || xml == null)
			return;
		
		try { xml.save(xmlFile); } catch (Exception e) {}
	}
	
	public final class XMLUserInfo extends UserInfo {
		
		public XMLUserInfo(XMLElement user) {
			super((user != null) ? user.getName() : "");
			
			if (user.hasElementWithName("connections")) {
				XMLElement connections = user.getElementsByName("connections").get(0);
				
				if (connections.hasElementWithName("viewing")) {
					XMLElement viewing = connections.getElementsByName("viewing").get(0);
					
					if (viewing.hasElementWithName("name") && viewing.hasElementWithName("type")) {
						XMLElement vName = viewing.getElementsByName("name").get(0);
						XMLElement vType = viewing.getElementsByName("type").get(0);
						
						if (vName.hasValue() && vType.hasValue())
							this.viewing = vName.getValue() + "::" + vType.getValue();
					}
				}
				
				if (connections.hasElementWithName("connected")) {
					XMLElement connected = connections.getElementsByName("connected").get(0);
					
					for (XMLElement node : connected.getElementsByName("node")) {
						if (!node.hasElementWithName("name") || !node.hasElementWithName("type"))
							continue;
						
						XMLElement nName = node.getElementsByName("name").get(0);
						XMLElement nType = node.getElementsByName("type").get(0);
						
						if (!nName.hasValue() || !nType.hasValue())
							continue;
						
						this.connected.add(nName.getValue() + "::" + nType.getValue());
					}
				}
			}
			
			if (user.hasElementWithName("metadata")) {
				XMLElement metadata = user.getElementsByName("metadata").get(0);
				
				for (XMLElement meta : metadata.getElements()) {
					if (!meta.hasValue())
						continue;
					
					this.metadata.put(meta.getName(), meta.getValue());
				}
			}
		}
	}
}