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

package com.titankingdoms.dev.titanchat.tools.update;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.plugin.PluginDescriptionFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

public abstract class UpdateAPI {
	
	private final String api = "https://api.curseforge.com/servermods/files?projectIds=";
	private final long id;
	
	private final String name;
	private final String version;
	private final String author;
	
	private final String currentTitle;
	private String newTitle;
	
	private final String currentVersion;
	private String newVersion;
	
	private String link = "";
	
	private boolean available = false;
	
	public UpdateAPI(long id, PluginDescriptionFile pdf) {
		Validate.notNull(id, "ID cannot be null");
		Validate.notNull(pdf, "PDF cannot be null");
		
		this.id = id;
		
		this.name = pdf.getName();
		this.version = pdf.getVersion();
		this.author = pdf.getAuthors().get(0);
		
		this.currentTitle = name + " v" + version;
		this.currentVersion = processVersion(currentTitle);
		
		this.newTitle = currentTitle;
		this.newVersion = currentVersion;
	}
	
	public boolean checkAvailability() {
		if (newVersion.equalsIgnoreCase(version))
			return false;
		
		String[] currentDigits = version.split("\\.");
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
		
		return available;
	}
	
	public String getAuthor() {
		return author;
	}
	
	public String getCurrentTitle() {
		return currentTitle;
	}
	
	public String getCurrentVersionNumber() {
		return currentVersion;
	}
	
	public long getID() {
		return id;
	}
	
	public String getDownloadLink() {
		return link;
	}
	
	public String getName() {
		return name;
	}
	
	public String getNewTitle() {
		return newTitle;
	}
	
	public String getNewVersionNumber() {
		return newVersion;
	}
	
	public boolean hasDownloadLink() {
		return link != null && !link.isEmpty();
	}
	
	public boolean isAvailable() {
		return available;
	}
	
	public abstract String processVersion(String version);
	
	public String queryAPI() {
		try {
			URL url = new URL(api + id);
			
			URLConnection connection = url.openConnection();
			connection.addRequestProperty("User-Agent", name + "/v" + version + " (By " + author + ")");
			
			InputStreamReader reader = new InputStreamReader(connection.getInputStream());
			JSONArray json = (JSONArray) JSONValue.parse(reader);
			reader.close();
			
			if (json.size() > 1)
				throw new Exception();
			
			JSONObject item = (JSONObject) json.get(json.size() - 1);
			
			this.newTitle = (String) item.get("name");
			this.newVersion = processVersion(newTitle);
			
			this.link = (String) item.get("downloadUrl");
			
		} catch (Exception e) {
			this.newTitle = currentTitle;
			this.newVersion = version;
			
			this.link = "";
		}
		
		return newVersion;
	}
}