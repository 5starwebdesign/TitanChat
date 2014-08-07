/*
 *     Copyright (C) 2014  Nodin Chan
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

package com.nodinchan.dev.tools.loading;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.configuration.file.YamlConfiguration;

public final class Loader {
	
	private static final String DESCRIPTION = "loadable";
	private static final File DIRECTORY = new File("loadables");
	private static final JarFilter FILTER = new JarFilter();
	private static final Logger LOG = Logger.getLogger("Loader");
	
	public static <T> List<T> load(Class<T> type, File directory, String description, JarFilter filter, Logger log) {
		if (type == null)
			throw new IllegalArgumentException("Class cannot be null");
		
		List<T> loadables = new ArrayList<>();
		
		if (directory == null)
			directory = DIRECTORY;
		
		if (description == null || description.isEmpty())
			description = DESCRIPTION;
		
		if (filter == null)
			filter = FILTER;
		
		if (log == null)
			log = LOG;
		
		for (File file : directory.listFiles(filter)) {
			JarFile jar = null;
			
			try {
				jar = new JarFile(file);
				JarEntry entry = jar.getJarEntry(description + ".yml");
				
				if (entry == null)
					throw new IllegalStateException("The YAML file " + description + ".yml was not found");
				
				InputStreamReader reader = new InputStreamReader(jar.getInputStream(entry), "UTF-8");
				
				ClassLoader loader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() });
				String main = YamlConfiguration.loadConfiguration(reader).getString("main-class");
				
				Class<?> loadedClass = Class.forName(main, true, loader);
				Class<? extends T> loadableClass = loadedClass.asSubclass(type);
				Constructor<? extends T> loadableConstructor = loadableClass.getConstructor();
				
				loadables.add(loadableConstructor.newInstance());
				
			} catch (Exception e) {
				log.log(Level.WARNING, "The Jar file " + file.getName() + " failed to load");
				log.log(Level.WARNING, e.getMessage());
				
			} finally {
				if (jar != null)
					try { jar.close(); } catch (Exception e) {}
			}
		}
		
		return loadables;
	}
	
	public static <T> List<T> load(Class<T> type, File directory, String description, JarFilter filter) {
		return load(type, directory, description, filter, LOG);
	}
	
	public static <T> List<T> load(Class<T> type, File directory, String description, Logger log) {
		return load(type, directory, description, FILTER, log);
	}
	
	public static <T> List<T> load(Class<T> type, File directory, JarFilter filter, Logger log) {
		return load(type, directory, DESCRIPTION, filter, log);
	}
	
	public static <T> List<T> load(Class<T> type, String description, JarFilter filter, Logger log) {
		return load(type, DIRECTORY, description, filter, log);
	}
	
	public static <T> List<T> load(Class<T> type, File directory, String description) {
		return load(type, directory, description, FILTER, LOG);
	}
	
	public static <T> List<T> load(Class<T> type, File directory, JarFilter filter) {
		return load(type, directory, DESCRIPTION, filter, LOG);
	}
	
	public static <T> List<T> load(Class<T> type, File directory, Logger log) {
		return load(type, directory, DESCRIPTION, FILTER, log);
	}
	
	public static <T> List<T> load(Class<T> type, String description, JarFilter filter) {
		return load(type, DIRECTORY, description, filter, LOG);
	}
	
	public static <T> List<T> load(Class<T> type, String description, Logger log) {
		return load(type, DIRECTORY, description, FILTER, log);
	}
	
	public static <T> List<T> load(Class<T> type, JarFilter filter, Logger log) {
		return load(type, DIRECTORY, DESCRIPTION, filter, log);
	}
	
	public static <T> List<T> load(Class<T> type, File directory) {
		return load(type, directory, DESCRIPTION, FILTER, LOG);
	}
	
	public static <T> List<T> load(Class<T> type, String description) {
		return load(type, DIRECTORY, description, FILTER, LOG);
	}
	
	public static <T> List<T> load(Class<T> type, JarFilter filter) {
		return load(type, DIRECTORY, DESCRIPTION, filter, LOG);
	}
	
	public static <T> List<T> load(Class<T> type, Logger log) {
		return load(type, DIRECTORY, DESCRIPTION, FILTER, log);
	}
	
	public static <T> List<T> load(Class<T> type) {
		return load(type, DIRECTORY, DESCRIPTION, FILTER, LOG);
	}
	
	public static class JarFilter implements FileFilter {
		
		@Override
		public final boolean accept(File file) {
			return file != null && file.getName().endsWith(".jar") && valid(file);
		}
		
		public boolean valid(File file) {
			return true;
		}
	}
}