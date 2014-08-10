/*
 *     Copyright 2011-2013 Tyler Blair. All rights reserved.
 *     
 *     Redistribution and use in source and binary forms, with or without modification, are
 *     permitted provided that the following conditions are met:
 *     
 *      1. Redistributions of source code must retain the above copyright notice, this list of
 *         conditions and the following disclaimer.
 *         
 *      2. Redistributions in binary form must reproduce the above copyright notice, this list
 *         of conditions and the following disclaimer in the documentation and/or other materials
 *         provided with the distribution.
 *      
 *     THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *     WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *     FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR 
 *     CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *     CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *     SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *     ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *     NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *     ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *     
 *     The views and conclusions contained in the software and documentation are those of the
 *     authors and contributors and should not be interpreted as representing official policies,
 *     either expressed or implied, of anybody else.
 */

package com.nodinchan.dev.titanchat.tools.metrics;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.logging.Level;
import java.util.zip.GZIPOutputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.scheduler.BukkitTask;

/**
 * <p>
 * The metrics class obtains data about a plugin and submits statistics about it to the metrics backend.
 * </p>
 * <p>
 * Public methods provided by this class:
 * </p>
 * <code>
 * Graph createGraph(String name); <br/>
 * void addCustomData(Metrics.Plotter plotter); <br/>
 * void start(); <br/>
 * </code>
 */
public class Metrics {
	
	/**
	 * The current revision number
	 */
	private static final int REVISION = 7;
	
	/**
	 * The base url of the metrics domain
	 */
	private static final String BASE_URL = "http://report.mcstats.org";
	
	/**
	 * The url used to report a server's status
	 */
	private static final String REPORT_URL = "/plugin/%s";
	
	/**
	 * Interval of time to ping (in minutes)
	 */
	private static final int PING_INTERVAL = 15;
	
	/**
	 * The plugin this metrics submits for
	 */
	private final Plugin plugin;
	
	/**
	 * All of the custom graphs to submit to metrics
	 */
	private final Set<Graph> graphs = Collections.synchronizedSet(new HashSet<Graph>());
	
	/**
	 * The plugin configuration
	 */
	private final YamlConfiguration configuration;
	
	/**
	 * The plugin configuration file
	 */
	private final File configurationFile;
	
	/**
	 * Unique server id
	 */
	private final String guid;
	
	/**
	 * Debug mode
	 */
	private final boolean debug;
	
	/**
	 * Lock for synchronization
	 */
	private final Object optOutLock = new Object();
	
	/**
	 * The scheduled task
	 */
	private volatile BukkitTask task = null;
	
	public Metrics(final Plugin plugin) throws IOException {
		if (plugin == null)
			throw new IllegalArgumentException("Plugin cannot be null");
		
		this.plugin = plugin;
		
		this.configurationFile = getConfigFile();
		this.configuration = YamlConfiguration.loadConfiguration(configurationFile);
		
		configuration.addDefault("opt-out", false);
		configuration.addDefault("guid", UUID.randomUUID().toString());
		configuration.addDefault("debug", false);
		
		if (configuration.get("guid", null) == null) {
			configuration.options().header("http://mcstats.org").copyDefaults(true);
			configuration.save(configurationFile);
		}
		
		this.guid = configuration.getString("guid");
		this.debug = configuration.getBoolean("debug", false);
	}
	
	/**
	 * Adds a Graph object to metrics that represents data for the plugin that should be sent to the backend.
	 * 
	 * @param graph The Graph
	 */
	public void addGraph(final Graph graph) {
		if (graph == null)
			throw new IllegalArgumentException("Graph cannot be null");
		
		graphs.add(graph);
	}
	
	/**
	 * Appends a JSON encoded key/value pair to the given StringBuilder.
	 */
	private static void appendJSONPair(StringBuilder json, String key, String value) throws UnsupportedEncodingException {
		boolean isValueNumeric = false;
		
		try {
			if (value.equals("0") || !value.endsWith("0")) {
				Double.parseDouble(value);
				isValueNumeric = true;
			}
			
		} catch (NumberFormatException e) {
			isValueNumeric = false;
		}
		
		if (json.charAt(json.length() - 1) != '{')
			json.append(',');
		
		json.append(escapeJSON(key));
		json.append(':');
		
		if (isValueNumeric)
			json.append(value);
		else
			json.append(escapeJSON(value));
	}
	
	/**
	 * Construct and create a Graph that can be used to separate specific Plotters to their own Graphs on the metrics
	 * website. Plotters can be added to the Graph object returned.
	 * 
	 * @param name The name of the Graph
	 * 
	 * @return Graph object created. Will never return NULL under normal circumstances unless bad parameters are given
	 */
	public Graph createGraph(final String name) {
		if (name == null)
			throw new IllegalArgumentException("Graph name cannot be null");
		
		final Graph graph = new Graph(name);
		graphs.add(graph);
		
		return graph;
	}
	
	/**
	 * Disables metrics for the server by setting "opt-out" to true in the config file and cancelling the metrics task.
	 * 
	 * @throws IOException
	 */
	public void disable() throws IOException {
		synchronized (optOutLock) {
			if (!isOptOut()) {
				configuration.set("opt-out", true);
				configuration.save(configurationFile);
			}
			
			if (task != null) {
				task.cancel();
				task = null;
			}
		}
	}
	
	/**
	 * Enables metrics for the server by setting "opt-out" to false in the config file and starting the metrics task.
	 * 
	 * @throws IOException
	 */
	public void enable() throws IOException {
		synchronized (optOutLock) {
			if (isOptOut()) {
				configuration.set("opt-out", false);
				configuration.save(configurationFile);
			}
			
			if (task == null)
				start();
		}
	}
	
	/**
	 * Escape a String to create a valid JSON String.
	 */
	private static String escapeJSON(String text) {
		StringBuilder builder = new StringBuilder();
		
		builder.append('"');
		
		for (int index = 0; index < text.length(); index++) {
			char chr = text.charAt(index);
			
			switch (chr) {
			
			case '"':
			case '\\':
				builder.append('\\');
				builder.append(chr);
				break;
				
			case '\b':
				builder.append("\\b");
				break;
				
			case '\t':
				builder.append("\\t");
				break;
				
			case '\n':
				builder.append("\\n");
				break;
				
			case '\r':
				builder.append("\\r");
				break;
				
			default:
				if (chr < ' ') {
					String t = "000" + Integer.toHexString(chr);
					builder.append("\\u" + t.substring(t.length() - 4));
					break;
				}
				
				builder.append(chr);
				break;
			}
		}
		
		builder.append('"');
		
		return builder.toString();
	}
	
	/**
	 * Gets the File object of the configuration file that should be used to store data such as the GUID and opt-out status.
	 * 
	 * @return The File object for the configuration file
	 */
	public File getConfigFile() {
		File pluginsFolder = plugin.getDataFolder().getParentFile();
		return new File(new File(pluginsFolder, "PluginMetrics"), "config.yml");
	}
	
	/**
	 * GZip compress a string of bytes
	 */
	public static byte[] gzip(String input) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		GZIPOutputStream gzos = null;
		
		try {
			gzos = new GZIPOutputStream(baos);
			gzos.write(input.getBytes("UTF-8"));
			
		} catch (IOException e) {
			e.printStackTrace();
			
		} finally {
			if (gzos != null)
				try { gzos.close(); } catch (IOException e) {}	
		}
		
		return baos.toByteArray();
	}
	
	/**
	 * Checks if Mineshafter is present. If it is, we need to bypass it to send POST requests
	 * 
	 * @return true if Mineshafter is installed on the server
	 */
	private boolean isMineshafterPresent() {
		try {
			Class.forName("mineshafter.MineServer");
			return true;
			
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * Checks if the server owner has denied metrics
	 * 
	 * @return True if metrics should be opted out of it
	 */
	public boolean isOptOut() {
		synchronized (optOutLock) {
			try {
				configuration.load(getConfigFile());
				
			} catch (IOException e) {
				if (debug)
					Bukkit.getLogger().log(Level.INFO, "[Metrics] " + e.getMessage());
				
				return true;
				
			} catch (InvalidConfigurationException e) {
				if (debug)
					Bukkit.getLogger().log(Level.INFO, "[Metrics] " + e.getMessage());
				
				return true;
			}
		}
		
		return configuration.getBoolean("opt-out", false);
	}
	
	/**
	 * Generic method that posts a plugin to the metrics website
	 */
	private void postPlugin(final boolean isPing) throws IOException {
		PluginDescriptionFile description = plugin.getDescription();
		String pluginName = description.getName();
		boolean onlineMode = Bukkit.getServer().getOnlineMode();
		String pluginVersion = description.getVersion();
		String serverVersion = Bukkit.getVersion();
		int playersOnline = Bukkit.getServer().getOnlinePlayers().length;
		
		StringBuilder json = new StringBuilder(1024);
		json.append('{');
		
		appendJSONPair(json, "guid", guid);
		appendJSONPair(json, "plugin_version", pluginVersion);
		appendJSONPair(json, "server_version", serverVersion);
		appendJSONPair(json, "players_online", Integer.toString(playersOnline));
		
		String osname = System.getProperty("os.name");
		String osarch = System.getProperty("os.arch");
		String osversion = System.getProperty("os.version");
		String java_version = System.getProperty("java.version");
		int coreCount = Runtime.getRuntime().availableProcessors();
		
		if (osarch.equals("amd64"))
			osarch = "x86_64";
		
		appendJSONPair(json, "osname", osname);
		appendJSONPair(json, "osarch", osarch);
		appendJSONPair(json, "osversion", osversion);
		appendJSONPair(json, "cores", Integer.toString(coreCount));
		appendJSONPair(json, "auth_mode", (onlineMode) ? "1" : "0");
		appendJSONPair(json, "java_version", java_version);
		
		if (isPing)
			appendJSONPair(json, "ping", "1");
		
		if (graphs.size() > 0) {
			synchronized (graphs) {
				json.append(',');
				json.append('"');
				json.append("graphs");
				json.append('"');
				json.append(':');
				json.append('{');
				
				boolean firstGraph = true;
				
				final Iterator<Graph> graphs = this.graphs.iterator();
				
				while (graphs.hasNext()) {
					Graph graph = graphs.next();
					
					StringBuilder graphJson = new StringBuilder();
					graphJson.append('{');
					
					for (Plotter plotter : graph.getPlotters())
						appendJSONPair(graphJson, plotter.getColumnName(), Integer.toString(plotter.getValue()));
					
					graphJson.append('}');
					
					if (!firstGraph)
						json.append(',');
					
					json.append(escapeJSON(graph.getName()));
					json.append(':');
					json.append(graphJson);
					
					firstGraph = false;
				}
				
				json.append('}');
			}
		}
		
		json.append('}');
		
		URL url = new URL(BASE_URL + String.format(REPORT_URL, urlEncode(pluginName)));
		
		URLConnection connection;
		
		if (isMineshafterPresent())
			connection = url.openConnection(Proxy.NO_PROXY);
		else
			connection = url.openConnection();
		
		byte[] uncompressed = json.toString().getBytes();
		byte[] compressed = gzip(json.toString());
		
		connection.addRequestProperty("User-Agent", "MCStats/" + REVISION);
		connection.addRequestProperty("Content-Type", "application/json");
		connection.addRequestProperty("Content-Encoding", "gzip");
		connection.addRequestProperty("Content-Length", Integer.toString(compressed.length));
		connection.addRequestProperty("Accept", "application/json");
		connection.addRequestProperty("Connection", "close");
		
		connection.setDoOutput(true);
		
		if (debug)
			Bukkit.getLogger().log(Level.INFO, "[Metrics] Prepared request for " + pluginName + " uncompressed=" + uncompressed.length + " compressed=" + compressed.length);
		
		OutputStream os = connection.getOutputStream();
		os.write(compressed);
		os.flush();
		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		String response = reader.readLine();
		
		os.close();
		reader.close();
		
		if (response == null || response.startsWith("ERR") || response.startsWith("7")) {
			if (response == null)
				response = "null";
			else if (response.startsWith("7"))
				response = response.substring((response.startsWith("7,")) ? 2 : 1);
			
		} else {
			if (response.equals("1") || response.contains("This is your first update this hour")) {
				synchronized (graphs) {
					final Iterator<Graph> graphs = this.graphs.iterator();
					
					while (graphs.hasNext()) {
						final Graph graph = graphs.next();
						
						for (Plotter plotter : graph.getPlotters())
							plotter.reset();
					}
				}
			}
		}
	}
	
	/**
	 * Start measuring statistics. This will immediately create an async repeating task as the plugin and send the
	 * initial data to the metrics backend, and then after that it will post in increments of PING_INTERVAL * 1200
	 * ticks.
	 * 
	 * @return True if statistics measuring is running, otherwise false
	 */
	public boolean start() {
		synchronized (optOutLock) {
			if (isOptOut())
				return false;
			
			if (task != null)
				return true;
			
			task = plugin.getServer().getScheduler().runTaskTimerAsynchronously(plugin, new Runnable() {
				
				private boolean firstPost = true;
				
				public void run() {
					try {
						synchronized (optOutLock) {
							if (isOptOut() && task != null) {
								task.cancel();
								task = null;
								
								for (Graph graph : graphs)
									graph.onOptOut();
							}
						}
						
						postPlugin(!firstPost);
						
						firstPost = false;
						
					} catch (IOException e) {
						if (debug)
							Bukkit.getLogger().log(Level.INFO, "[Metrics] " + e.getMessage());
					}
				}
				
			}, 0, PING_INTERVAL * 1200);
			
			return true;
		}
	}
	
	/**
	 * Encode text as UTF-8
	 * 
	 * @param text The text to encode
	 * 
	 * @return The encoded text, as UTF-8
	 */
	private static String urlEncode(final String text) throws UnsupportedEncodingException {
		return URLEncoder.encode(text, "UTF-8");
	}
	
	/**
	 * Represents a custom graph on the website
	 */
	public static class Graph {
		
		/**
		 * The graph's name (alphanumeric and spaces only)
		 * If it does not comply to the above when submitted, it is rejected
		 */
		private final String name;
		
		/**
		 * The set of plotters that are contained within this graph
		 */
		private final Set<Plotter> plotters = new LinkedHashSet<Plotter>();
		
		private Graph(final String name) {
			this.name = name;
		}
		
		/**
		 * Adds a plotter to the graph, which will be used to plot entries
		 * 
		 * @param plotter the plotter to add to the graph
		 */
		public void addPlotter(final Plotter plotter) {
			plotters.add(plotter);
		}
		
		@Override
		public boolean equals(Object object) {
			if (!(object instanceof Graph))
				return false;
			
			return ((Graph) object).name.equals(name);
		}
		
		/**
		 * Gets the graph's name
		 * 
		 * @return the graph's name
		 */
		public String getName() {
			return name;
		}
		
		/**
		 * Gets an <b>unmodifiable</b> Set of the Plotters in the graph
		 * 
		 * @return an unmodifiable Set of Plotters
		 */
		public Set<Plotter> getPlotters() {
			return Collections.unmodifiableSet(plotters);
		}
		
		@Override
		public int hashCode() {
			return name.hashCode();
		}
		
		/**
		 * Called when the server owners decides to opt-out of Metrics while the server is running
		 */
		protected void onOptOut() {}
		
		/**
		 * Removes a Plotter from the Graph
		 * 
		 * @param plotter the Plotter to remove from the Graph
		 */
		public void removePlotter(final Plotter plotter) {
			plotters.remove(plotter);
		}
	}
	
	/**
	 * Interface used to collect custom data for a plugin
	 */
	public static abstract class Plotter {
		
		/**
		 * The plot's name
		 */
		private final String name;
		
		/**
		 * Construct a Plotter with the default plot name
		 */
		public Plotter() {
			this("Default");
		}
		
		/**
		 * Construct a Plotter with a specified plot name
		 * 
		 * @param name the name of the Plotter to use, which will show up on the website
		 */
		public Plotter(final String name) {
			this.name = name;
		}
		
		@Override
		public boolean equals(Object object) {
			if (!(object instanceof Plotter))
				return false;
			
			return ((Plotter) object).name.equals(name) && ((Plotter) object).getValue() == getValue();
		}
		
		/**
		 * Gets the column name for the plotted point
		 * 
		 * @return the plotted point's column name
		 */
		public String getColumnName() {
			return name;
		}
		
		/**
		 * Gets the current value for the plotted point. Since this function defers to an external function
		 * it may or may not return immediately thus cannot be guranteed to be thread friendly or safe.
		 * This function can be called from any thread so care should be taken when accessing resources
		 * the need to be synchronized
		 * 
		 * @return the current value for the point to be plotted
		 */
		public abstract int getValue();
		
		@Override
		public int hashCode() {
			return getColumnName().hashCode();
		}
		
		/**
		 * Called after the website graphs have been updated
		 */
		public void reset() {}
	}
}