package com.titankingdoms.dev.titanchat.tools.release;

import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.bukkit.plugin.PluginDescriptionFile;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import com.titankingdoms.dev.titanchat.tools.release.ReleaseHistory.Version.ReleaseType;

public class ReleaseHistory {
	
	private final long id;
	
	private final String name;
	private final String version;
	private final String[] authors;
	
	private final List<Version> versions;
	
	public ReleaseHistory(long id, PluginDescriptionFile pdf) {
		this.id = id;
		
		this.name = pdf.getName();
		this.version = pdf.getVersion();
		this.authors = pdf.getAuthors().toArray(new String[0]);
		
		this.versions = new LinkedList<Version>();
	}
	
	public String[] getAuthors() {
		return authors;
	}
	
	public long getID() {
		return id;
	}
	
	public Version getLatestVersion() {
		if (versions.size() < 1)
			return null;
		
		return versions.get(versions.size() - 1);
	}
	
	public String getName() {
		return name;
	}
	
	public Version getRunningVersion() {
		return new Version(name + " v" + version, "", ReleaseType.UNKNOWN);
	}
	
	public Version getVersion(int position) {
		if (position < 0 || position >= versions.size())
			throw new IllegalArgumentException("Position cannot be beyond 0 to " + versions.size());
		
		return versions.get(position);
	}
	
	public List<Version> getVersions() {
		return new LinkedList<Version>(versions);
	}
	
	public void searchHistory() {
		try {
			URL url = new URL("https://api.curseforge.com/servermods/files?projectIds=");
			
			URLConnection connection = url.openConnection();
			connection.setConnectTimeout(15000);
			connection.setReadTimeout(15000);
			connection.addRequestProperty("User-Agent", name + "/v" + version + " (By " + authors[0] + ")");
			
			InputStreamReader reader = new InputStreamReader(connection.getInputStream());
			JSONArray json = (JSONArray) JSONValue.parse(reader);
			reader.close();
			
			if (json.size() < 1)
				return;
			
			for (Object o : json) {
				JSONObject item = (JSONObject) o;
				
				String title = (String) item.get("name");
				String link = (String) item.get("downloadUrl");
				ReleaseType type = ReleaseType.fromName((String) item.get("releaseType"));
				
				versions.add(new Version(title, link, type));
			}
			
		} catch (Exception e) {}
	}
	
	public static final class Version {
		
		private final String title;
		private final String link;
		
		private final ReleaseType release;
		
		private Version(String title, String link, ReleaseType release) {
			this.title = title;
			this.link = link;
			this.release = release;
		}
		
		public String getDownloadLink() {
			return link;
		}
		
		public ReleaseType getReleaseType() {
			return release;
		}
		
		public String getTitle() {
			return title;
		}
		
		public enum ReleaseType {
			ALPHA("Alpha"),
			BETA("Beta"),
			RELEASE("Release"),
			UNKNOWN("Unknown");
			
			private final String name;
			private static final Map<String, ReleaseType> NAME_MAP = new HashMap<String, ReleaseType>();
			
			private ReleaseType(String name) {
				this.name = name;
			}
			
			static {
				for (ReleaseType type : EnumSet.allOf(ReleaseType.class))
					NAME_MAP.put(type.name.toLowerCase(), type);
			}
			
			public static ReleaseType fromName(String name) {
				return (NAME_MAP.containsKey(name.toLowerCase())) ? NAME_MAP.get(name.toLowerCase()) : UNKNOWN;
			}
			
			public String getName() {
				return name;
			}
		}
	}
}