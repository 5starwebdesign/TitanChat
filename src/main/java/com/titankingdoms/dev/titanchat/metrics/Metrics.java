/*
 * Copyright 2011-2013 Tyler Blair. All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are
 * permitted provided that the following conditions are met:
 * 
 *  1. Redistributions of source code must retain the above copyright notice, this list of
 *     conditions and the following disclaimer.
 *     
 *  2. Redistributions in binary form must reproduce the above copyright notice, this list
 *     of conditions and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *  
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR 
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are those of the
 * authors and contributors and should not be interpreted as representing official policies,
 * either expressed or implied, of anybody else.
 */

package com.titankingdoms.dev.titanchat.metrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;

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
	private static final int REVISION = 6;
	
	/**
	 * The base url of the metrics domain
	 */
	private static final String BASE_URL = "http://mcstats.org";
	
	/**
	 * The url used to report a server's status
	 */
	private static final String REPORT_URL = "/report/%s";
	
	/**
	 * The separator to use for custom data. This MUST NOT change unless you are hosting your own
	 * version of metrics and want to change it
	 */
	private static final String CUSTOM_DATA_SEPARATOR = "~~";
	
	/**
	 * Interval of time to ping (in minutes)
	 */
	private static final int PING_INTERVAL = 10;
	
	/**
	 * The plugin this metrics submits for
	 */
	private final Plugin plugin;
	
	/**
	 * All of the custom graphs to submit to metrics
	 */
	private final Set<Graph> graphs = Collections.synchronizedSet(new HashSet<Graph>());
	
	/**
	 * The default graph, used for addCustomData when you don't want a specific graph
	 */
	private final Graph defaultGraph = new Graph("Default");
	
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
		
		configurationFile = getConfigFile();
		configuration = YamlConfiguration.loadConfiguration(configurationFile);
		
		configuration.addDefault("opt-out", false);
		configuration.addDefault("guid", UUID.randomUUID().toString());
		configuration.addDefault("debug", false);
		
		if (configuration.get("guid", null) == null) {
			configuration.options().header("http://mcstats.org").copyDefaults(true);
			configuration.save(configurationFile);
		}
		
		guid = configuration.getString("guid");
		debug = configuration.getBoolean("debug", false);
	}
	
	/**
	 * Adds a custom data {@link Plotter} to the default {@link Graph}
	 * 
	 * @param plotter the {@link Plotter} to use to plot custom data
	 */
	public void addCustomData(final Plotter plotter) {
		if (plotter == null)
			throw new IllegalArgumentException("Plotter cannot be null");
		
		defaultGraph.addPlotter(plotter);
		graphs.add(defaultGraph);
	}
	
	/**
	 * Adds a {@link Graph} object to metrics that represents data for the plugin that should be sent to the backend
	 * 
	 * @param graph the {@link Graph}
	 */
	public void addGraph(final Graph graph) {
		if (graph == null)
			throw new IllegalArgumentException("Graph cannot be null");
		
		graphs.add(graph);
	}
	
	/**
	 * Construct a {@link Graph} that can be used to separate specific {@link Plotter}s to their own {@link Graph}s
	 * on the metrics website. {@link Plotter}s can be added to the graph object returned
	 * 
	 * @param name the name of the {@link Graph}
	 * 
	 * @return {@link Graph} object created. Will never return NULL under normal circumstances unless bad parameters are given
	 */
	public Graph createGraph(final String name) {
		if (name == null)
			throw new IllegalArgumentException("Graph name cannot be null");
		
		final Graph graph = new Graph(name);
		graphs.add(graph);
		
		return graph;
	}
	
	/**
	 * Disables metrics for the server by setting "opt-out" to true in the config file and cancelling the metrics task
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
	 * Enables metrics for the server by setting "opt-out" to false in the config file and starting the metrics task
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
	 * Encode text as UTF-8
	 * 
	 * @param text the text to encode
	 * 
	 * @return the encoded text, as UTF-8
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private static String encode(final String text) throws UnsupportedEncodingException {
		return URLEncoder.encode(text, "UTF-8");
	}
	
	/**
	 * <p>
	 * Encode a key/value data pair to be used in a HTTP post request. This INCLUDES a '&' so the first
	 * key/value pair MUST be included manually, eg:
	 * </p>
	 * 
	 * <code>
	 * StringBuilder data = new StringBuffer();
	 * data.append(encode("guid")).append('=').append(encode(guid));
	 * encodeDataPair(data, "version", version);
	 * </code>
	 * 
	 * @param buffer the StringBuilder to append the data pair onto
	 * 
	 * @param key the key value
	 * 
	 * @param value the value
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private static void encodeDataPair(final StringBuilder buffer, final String key, final String value) throws UnsupportedEncodingException {
		buffer.append('&').append(encode(key)).append('=').append(encode(value));
	}
	
	/**
	 * Gets the file object of the configuration file that should be used to store data such as the GUID and opt-out status
	 * 
	 * @return the file object for the configuration file
	 */
	public File getConfigFile() {
		File pluginsFolder = plugin.getDataFolder().getParentFile();
		return new File(new File(pluginsFolder, "PluginMetrics"), "config.yml");
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
	 * Checks if the server owner has opted out of metrics
	 * 
	 * @return true if metrics should be opted out
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
	 * 
	 * @param isPing true if it's a ping
	 * 
	 * @throws IOException
	 */
	private void postPlugin(final boolean isPing) throws IOException {
		PluginDescriptionFile description = plugin.getDescription();
		String pluginName = description.getName();
		boolean onlineMode = Bukkit.getServer().getOnlineMode();
		String pluginVersion = description.getVersion();
		String serverVersion = Bukkit.getVersion();
		int playersOnline = Bukkit.getServer().getOnlinePlayers().length;
		
		final StringBuilder data = new StringBuilder();
		
		data.append(encode("guid")).append('=').append(encode(guid));
		encodeDataPair(data, "version", pluginVersion);
		encodeDataPair(data, "server", serverVersion);
		encodeDataPair(data, "players", Integer.toString(playersOnline));
		encodeDataPair(data, "revision", String.valueOf(REVISION));
		
		String osname = System.getProperty("os.name");
		String osarch = System.getProperty("os.arch");
		String osversion = System.getProperty("os.version");
		String java_version = System.getProperty("java.version");
		int coreCount = Runtime.getRuntime().availableProcessors();
		
		if (osarch.equals("amd64"))
			osarch = "x86_64";
		
		encodeDataPair(data, "osname", osname);
		encodeDataPair(data, "osarch", osarch);
		encodeDataPair(data, "osversion", osversion);
		encodeDataPair(data, "cores", Integer.toString(coreCount));
		encodeDataPair(data, "online-mode", Boolean.toString(onlineMode));
		encodeDataPair(data, "java_version", java_version);
		
		if (isPing)
			encodeDataPair(data, "ping", "true");
		
		synchronized (graphs) {
			final Iterator<Graph> graphs = this.graphs.iterator();
			
			while (graphs.hasNext()) {
				final Graph graph = graphs.next();
				
				for (Plotter plotter : graph.getPlotters()) {
					final String key = String.format("C%s%s%s%s", CUSTOM_DATA_SEPARATOR, graph.getName(), CUSTOM_DATA_SEPARATOR, plotter.getColumnName());
					final String value = Integer.toString(plotter.getValue());
					encodeDataPair(data, key, value);
				}
			}
		}
		
		URL url = new URL(BASE_URL + String.format(REPORT_URL, encode(pluginName)));
		
		URLConnection connection;
		
		if (isMineshafterPresent())
			connection = url.openConnection(Proxy.NO_PROXY);
		else
			connection = url.openConnection();
		
		connection.setDoOutput(true);
		
		final OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
		writer.write(data.toString());
		writer.flush();
		
		final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		final String response = reader.readLine();
		
		writer.close();
		reader.close();
		
		if (response == null || response.startsWith("ERR"))
			throw new IOException(response);
		
		if (response.contains("OK This is your first update this hour")) {
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
	
	/**
	 * Start measuring the statistics. This will immediately create an async repeating task as the plugin and send
	 * the initial data to the metrics backend, and then after that it will post in increments of
	 * PING_INTERVAL * 1200 ticks
	 * 
	 * @return true if statistics measuring is running, otherwise false
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
							
							postPlugin(!firstPost);
							
							firstPost = false;
						}
						
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
		 * Gets an <b>unmodifiable</b> {@link Set} of the {@link Plotter}s in the graph
		 * 
		 * @return an unmodifiable {@link Set} of {@link Plotter}s
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
		 * Removes a {@link Plotter} from the {@link Graph}
		 * 
		 * @param plotter the {@link Plotter} to remove from the {@link Graph}
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
		 * Construct a {@link Plotter} with the default plot name
		 */
		public Plotter() {
			this("Default");
		}
		
		/**
		 * Construct a {@link Plotter} with a specified plot name
		 * 
		 * @param name the name of the {@link Plotter} to use, which will show up on the website
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
			return name.hashCode();
		}
		
		/**
		 * Called after the website graphs have been updated
		 */
		public void reset() {}
	}
}