package com.titankingdoms.nodinchan.titanchat.support;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.channel.CustomChannel;
import com.titankingdoms.nodinchan.titanchat.command.Command;

public class Loader {
	
	private TitanChat plugin;
	
	private File addonDir;
	private File channelDir;
	private File commandDir;
	
	private ClassLoader addonLoader;
	private ClassLoader channelLoader;
	private ClassLoader commandLoader;
	
	private List<File> addonFiles;
	private List<File> channelFiles;
	private List<File> commandFiles;
	private List<Addon> addons;
	private List<CustomChannel> channels;
	private List<Command> commands;
	
	private Map<String, String> paJarNames;
	private Map<String, String> ccJarNames;
	
	public Loader(TitanChat plugin) {
		this.plugin = plugin;
		addonFiles = new ArrayList<File>();
		channelFiles = new ArrayList<File>();
		commandFiles = new ArrayList<File>();
		addons = new ArrayList<Addon>();
		channels = new ArrayList<CustomChannel>();
		commands = new ArrayList<Command>();
		addonDir = plugin.getAddonDir();
		channelDir = plugin.getCustomChannelDir();
		commandDir = plugin.getCommandDir();
		paJarNames = new HashMap<String, String>();
		ccJarNames = new HashMap<String, String>();

		List<URL> addonUrls = new ArrayList<URL>();
		List<URL> channelUrls = new ArrayList<URL>();
		List<URL> commandUrls = new ArrayList<URL>();
		
		for (String addonFile : addonDir.list()) {
			if (addonFile.endsWith(".jar")) {
				File file = new File(addonDir, addonFile);
				
				addonFiles.add(file);
				try {
					addonUrls.add(file.toURI().toURL());
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		for (String channelFile : channelDir.list()) {
			if (channelFile.endsWith(".jar")) {
				File file = new File(channelDir, channelFile);
				
				channelFiles.add(file);
				try {
					channelUrls.add(file.toURI().toURL());
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		for (String commandFile : commandDir.list()) {
			if (commandFile.endsWith(".jar")) {
				File file = new File(commandDir, commandFile);
				
				commandFiles.add(file);
				try {
					commandUrls.add(file.toURI().toURL());
					
				} catch (MalformedURLException e) {
					e.printStackTrace();
				}
			}
		}
		
		addonLoader = URLClassLoader.newInstance(addonUrls.toArray(new URL[addonUrls.size()]), plugin.getClass().getClassLoader());
		channelLoader = URLClassLoader.newInstance(channelUrls.toArray(new URL[channelUrls.size()]), plugin.getClass().getClassLoader());
		commandLoader = URLClassLoader.newInstance(commandUrls.toArray(new URL[commandUrls.size()]), plugin.getClass().getClassLoader());
	}
	
	public String getCustomChannelJar(String name) {
		return ccJarNames.get(name);
	}
	
	public String getPluginAddonJar(String name) {
		return paJarNames.get(name);
	}
	
	public List<Addon> loadAddons() throws Exception {
		for (File file : addonFiles) {
			try {
				JarFile jarFile = new JarFile(file);
				Enumeration<JarEntry> entries = jarFile.entries();
				
				String mainClass = null;
				
				while (entries.hasMoreElements()) {
					JarEntry element = entries.nextElement();
					
					if (element.getName().equalsIgnoreCase("path.yml")) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(element)));
						mainClass = reader.readLine().substring(12);
						break;
					}
				}
				
				if (mainClass != null) {
					Class<?> clazz = Class.forName(mainClass, true, addonLoader);
					Class<? extends Addon> addonClass = clazz.asSubclass(Addon.class);
					Constructor<? extends Addon> ctor = addonClass.getConstructor(plugin.getClass());
					Addon addon = ctor.newInstance(plugin);
					paJarNames.put(addon.getName(), file.getName());
					addon.init();
					addons.add(addon);
					
				} else {
					throw new Exception();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				plugin.log(Level.WARNING, "The plugin support " + file.getName() + " failed to load");
			}
		}
		
		if (!addons.isEmpty()) {
			StringBuilder str = new StringBuilder();
			
			for (Addon support : addons) {
				if (str.length() > 0)
					str.append(", ");
				
				str.append(support.getName());
			}
			
			plugin.log(Level.INFO, "Loaded plugin supports: " + str.toString());
		}
		
		return addons;
	}
	
	public List<CustomChannel> loadChannels() throws Exception {
		for (File file : channelFiles) {
			try {
				JarFile jarFile = new JarFile(file);
				Enumeration<JarEntry> entries = jarFile.entries();
				
				String mainClass = null;
				
				while (entries.hasMoreElements()) {
					JarEntry element = entries.nextElement();
					
					if (element.getName().equalsIgnoreCase("path.yml")) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(element)));
						mainClass = reader.readLine().substring(12);
						break;
					}
				}
				
				if (mainClass != null) {
					Class<?> clazz = Class.forName(mainClass, true, channelLoader);
					Class<? extends CustomChannel> channelClass = clazz.asSubclass(CustomChannel.class);
					Constructor<? extends CustomChannel> ctor = channelClass.getConstructor(plugin.getClass());
					CustomChannel channel = ctor.newInstance(plugin);
					ccJarNames.put(channel.getName(), file.getName());
					channel.init();
					channels.add(channel);
					
				} else {
					throw new Exception();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				plugin.log(Level.WARNING, "The custom channel " + file.getName() + " failed to load");
			}
		}
		
		if (!channels.isEmpty()) {
			StringBuilder str = new StringBuilder();
			
			for (CustomChannel channel : channels) {
				if (str.length() > 0)
					str.append(", ");
				
				str.append(channel.getName());
			}
			
			plugin.log(Level.INFO, "Loaded custom channels: " + str.toString());
		}
		
		return channels;
	}
	
	public List<Command> loadCommands() throws Exception {
		for (File file : commandFiles) {
			try {
				JarFile jarFile = new JarFile(file);
				Enumeration<JarEntry> entries = jarFile.entries();
				
				String mainClass = null;
				
				while (entries.hasMoreElements()) {
					JarEntry element = entries.nextElement();
					
					if (element.getName().equalsIgnoreCase("path.yml")) {
						BufferedReader reader = new BufferedReader(new InputStreamReader(jarFile.getInputStream(element)));
						mainClass = reader.readLine().substring(12);
						break;
					}
				}
				
				if (mainClass != null) {
					Class<?> clazz = Class.forName(mainClass, true, commandLoader);
					Class<? extends Command> commandClass = clazz.asSubclass(Command.class);
					Constructor<? extends Command> ctor = commandClass.getConstructor(plugin.getClass());
					Command command = ctor.newInstance(plugin);
					command.init();
					commands.add(command);
					
				} else {
					throw new Exception();
				}
				
			} catch (Exception e) {
				e.printStackTrace();
				plugin.log(Level.WARNING, "The command " + file.getName() + " failed to load");
			}
		}
		
		return commands;
	}
}