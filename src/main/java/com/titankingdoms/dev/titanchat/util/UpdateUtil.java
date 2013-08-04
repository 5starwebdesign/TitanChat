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

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.events.XMLEvent;

import org.apache.commons.lang.math.NumberUtils;

public final class UpdateUtil {
	
	private final String rss;
	private final String currentVersion;
	private String newVersion;
	
	public UpdateUtil(String name, String currentVersion) {
		this.rss = "http://dev.bukkit.org/bukkit-plugins/" + name + "/files.rss";
		this.currentVersion = currentVersion;
		this.newVersion = currentVersion;
	}
	
	public String check() {
		try {
			URL url = new URL(rss);
			
			XMLEventReader reader = XMLInputFactory.newInstance().createXMLEventReader(url.openStream());
			
			boolean isItem = false;
			
			while (reader.hasNext()) {
				XMLEvent event = reader.nextEvent();
				
				if (!isItem) {
					isItem = event.toString().equals("<item>");
					continue;
				}
				
				if (!event.toString().equals("<title>"))
					continue;
				
				this.newVersion = reader.nextEvent().toString().replaceAll("[^\\d\\.]", "");
				break;
			}
			
		} catch (Exception e) {
			this.newVersion = currentVersion;
		}
		
		return newVersion;
	}
	
	public String getCurrentVersion() {
		return currentVersion;
	}
	
	public String getNewVersion() {
		return newVersion;
	}
	
	public boolean newer(String version) {
		String[] currentDigits = currentVersion.split("\\.");
		String[] newDigits = version.split("\\.");
		
		int digits = Math.max(currentDigits.length, newDigits.length);
		
		for (int digit = 0; digit < digits; digit++) {
			try {
				int currentDigit = NumberUtils.toInt(currentDigits[digit], 0);
				int newDigit = NumberUtils.toInt(newDigits[digit], 0);
				
				if (newDigit > currentDigit)
					return true;
				
				if (newDigit < currentDigit)
					break;
				
			} catch (Exception e) {}
		}
		
		return false;
	}
	
	public boolean verify() {
		return newer(check());
	}
}