/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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
 *     along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.titankingdoms.dev.titanchat.loading;

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

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.loading.Loadable.InitResult;

/**
 * {@link Loader} - Loads {@link Loadable}s
 * 
 * @author NodinChan
 *
 */
public final class Loader {
	
	private static final Logger log = Logger.getLogger("LoadingLog");
	
	/**
	 * Loads the {@link Loadable}s in the specified directory
	 * 
	 * @param clazz The {@link Class} to load as
	 * 
	 * @param directory The directory to load from
	 * 
	 * @return The list of loaded {@link Loadable}s
	 */
	public static <T extends Loadable> List<T> load(Class<T> clazz, File directory) {
		List<T> loadables = new ArrayList<T>();
		
		for (File file : directory.listFiles(new JarFilter())) {
			JarFile jar = null;
			
			try {
				ClassLoader loader = URLClassLoader.newInstance(new URL[] { file.toURI().toURL() });
				
				jar = new JarFile(file);
				JarEntry entry = jar.getJarEntry("path.yml");
				
				if (entry == null)
					throw new Exception();
				
				InputStream entryStream = jar.getInputStream(entry);
				
				if (entryStream == null)
					throw new Exception();
				
				Configuration pathYaml = YamlConfiguration.loadConfiguration(entryStream);
				String path = pathYaml.getString("main-class");
				
				Class<?> loadedClass = Class.forName(path, true, loader);
				Class<? extends T> loadableClass = loadedClass.asSubclass(clazz);
				Constructor<? extends T> ctor = loadableClass.getConstructor();
				
				T loadable = ctor.newInstance();
				
				File dataFolder = new File(file.getParentFile(), loadable.getName());
				dataFolder.mkdirs();
				
				loadable.initialise(loader, file, dataFolder);
				
				InitResult result = loadable.onInitialise();
				
				Level level = Level.INFO;
				
				if (result.isSuccessful())
					loadables.add(loadable);
				else
					level = Level.WARNING;
				
				if (!result.getMessage().isEmpty())
					log.log(level, result.getMessage());
				
				if (!result.isSuccessful())
					throw new Exception();
				
			} catch (Exception e) {
				log.log(Level.WARNING, "The Jar file " + file.getName() + " failed to load");
			}
			
			if (jar != null)
				try { jar.close(); } catch (Exception e) {}
		}
		
		return loadables;
	}
	
	public static class ExtensionFilter implements FileFilter {
		
		private final String extension;
		
		public ExtensionFilter(String extension) {
			this.extension = extension;
		}
		
		public final boolean accept(File file) {
			return file.getName().endsWith(extension);
		}
	}
	
	public static final class JarFilter extends ExtensionFilter {
		
		public JarFilter() {
			super(".jar");
		}
	}
}