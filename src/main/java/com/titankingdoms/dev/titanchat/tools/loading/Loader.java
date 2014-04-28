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

package com.titankingdoms.dev.titanchat.tools.loading;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
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
	
	private static final Logger log = Logger.getLogger("LoadingLog");
	
	public static <T extends Loadable> List<T> load(Class<T> clazz, File directory) {
		List<T> loadables = new ArrayList<T>();
		
		for (File file : directory.listFiles(new ExtensionFilter(".jar"))) {
			JarFile jar = null;
			
			try {
				jar = new JarFile(file);
				JarEntry entry = jar.getJarEntry("loadable.yml");
				
				if (entry == null)
					throw new Exception("The YAML file loadable.yml was not found");
				
				InputStream entryStream = jar.getInputStream(entry);
				
				if (entryStream == null)
					throw new Exception();
				
				ClassLoader loader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() });
				String main = YamlConfiguration.loadConfiguration(entryStream).getString("main-class");
				
				Class<?> loadedClass = Class.forName(main, true, loader);
				Class<? extends T> loadableClass = loadedClass.asSubclass(clazz);
				Constructor<? extends T> ctor = loadableClass.getConstructor();
				
				T loadable = ctor.newInstance();
				
				File dataFolder = new File(file.getParentFile(), loadable.getName());
				dataFolder.mkdirs();
				
				loadable.init(loader, file, dataFolder);
				loadable.onLoad();
				
				loadables.add(loadable);
				
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
	
	public static class ExtensionFilter implements FileFilter {
		
		private final String extension;
		
		public ExtensionFilter(String extension) {
			this.extension = (extension != null) ? extension : "";
		}
		
		@Override
		public boolean accept(File file) {
			return file != null && file.getName().endsWith(extension);
		}
	}
}