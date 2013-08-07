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

package com.titankingdoms.dev.titanchat.util;

import java.net.URL;

import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.plugin.PluginDescriptionFile;

import com.titankingdoms.dev.titanchat.util.xml.XMLDocument;
import com.titankingdoms.dev.titanchat.util.xml.XMLElement;
import com.titankingdoms.dev.titanchat.util.xml.XMLObject;
import com.titankingdoms.dev.titanchat.util.xml.XMLObject.XMLType;
import com.titankingdoms.dev.titanchat.util.xml.XMLParser;
import com.titankingdoms.dev.titanchat.util.xml.XMLSection;

public final class UpdateUtil {
	
	private final String rss;
	
	private final String currentName;
	private String newName;
	
	private final String currentVersion;
	private String newVersion;
	
	private boolean available;
	
	public UpdateUtil(String name, PluginDescriptionFile pdf) {
		this.rss = "http://dev.bukkit.org/bukkit-plugins/" + name + "/files.rss";
		this.currentName = pdf.getName() + " v" + pdf.getVersion();
		this.newName = currentName;
		this.currentVersion = pdf.getVersion().replace("#", ".").replaceAll("[^\\d\\.]", "");
		this.newVersion = currentVersion;
		this.available = false;
	}
	
	public boolean checkAvailability() {
		if (!this.available) {
			String[] currentDigits = currentVersion.split("\\.");
			String[] newDigits = newVersion.split("\\.");
			
			int digits = Math.max(currentDigits.length, newDigits.length);
			
			for (int digit = 0; digit < digits; digit++) {
				try {
					int currentDigit = NumberUtils.toInt(currentDigits[digit], 0);
					int newDigit = NumberUtils.toInt(newDigits[digit], 0);
					
					if (newDigit == currentDigit)
						continue;
					
					this.available = newDigit > currentDigit;
					break;
					
				} catch (Exception e) {}
			}
		}
		
		return available;
	}
	
	public String getCurrentName() {
		return currentName;
	}
	
	public String getCurrentVersion() {
		return currentVersion;
	}
	
	public String getNewName() {
		return newName;
	}
	
	public String getNewVersion() {
		return newVersion;
	}
	
	public boolean hasUpdate() {
		return available;
	}
	
	public String readFeed() {
		try {
			URL url = new URL(rss);
			
			XMLDocument document = XMLParser.parse(url.openStream());
			
			XMLObject section = document.getElements("item").get(0);
			
			if (!section.getType().equals(XMLType.SECTION))
				throw new Exception();
			
			XMLObject element = ((XMLSection) section).getElements("title").get(0);
			
			if (!element.getType().equals(XMLType.ELEMENT))
				throw new Exception();
			
			this.newName = ((XMLElement) element).getValue();
			this.newVersion = newName.replace("#", ".").replaceAll("[^\\d\\.]", "");
			
		} catch (Exception e) {
			this.newName = currentName;
			this.newVersion = currentVersion;
		}
		
		return newVersion;
	}
}