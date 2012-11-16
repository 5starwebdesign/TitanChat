package com.titankingdoms.nodinchan.titanchat.loading;

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

import com.titankingdoms.nodinchan.titanchat.loading.Loadable.InitResult;

public final class Loader {
	
	private static final Logger log = Logger.getLogger("LoadingLog");
	
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
				
				InitResult result = loadable.init();
				
				Level level = Level.INFO;
				
				if (result.getResult())
					loadables.add(loadable);
				else
					level = Level.WARNING;
				
				if (!result.getMessage().isEmpty())
					log.log(level, result.getMessage());
				
				if (!result.getResult())
					throw new Exception();
				
			} catch (Exception e) {
				log.log(Level.WARNING, "The Jar file " + file.getName() + " failed to load");
			}
			
			if (jar != null)
				try { jar.close(); } catch (Exception e) {}
		}
		
		return loadables;
	}
	
	public static final class JarFilter implements FileFilter {
		
		public boolean accept(File file) {
			return file.getName().endsWith(".jar");
		}
	}
}