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

package com.titankingdoms.dev.titanchat.info;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.logging.Level;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.info.commands.CommandsIndex;
import com.titankingdoms.dev.titanchat.info.general.GeneralIndex;
import com.titankingdoms.dev.titanchat.info.general.GeneralTopic;
import com.titankingdoms.dev.titanchat.info.permissions.PermissionsIndex;
import com.titankingdoms.dev.titanchat.util.Debugger;

/**
 * {@link TopicManager} - Manages topics
 * 
 * @author NodinChan
 *
 */
public final class TopicManager {
	
	private final TitanChat plugin;
	
	private final Debugger db = new Debugger(6, "InfoManager");
	
	private File configFile;
	private FileConfiguration config;
	
	private final GeneralIndex index;
	
	private final Map<String, Topic> topics;
	
	public TopicManager() {
		this.plugin = TitanChat.getInstance();
		this.index = new GeneralIndex();
		this.topics = new TreeMap<String, Topic>();
	}
	
	/**
	 * Gets the configuration for {@link Topic}s
	 * 
	 * @return The configuration
	 */
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	/**
	 * Gets the {@link GeneralIndex}
	 * 
	 * @return The {@link GeneralIndex}
	 */
	public GeneralIndex getGeneralIndex() {
		return index;
	}
	
	/**
	 * Gets the specified {@link Topic}
	 * 
	 * @param name The name of the {@link Topic}
	 * 
	 * @return The {@link Topic} if found, otherwise null
	 */
	public Topic getTopic(String name) {
		return topics.get(name.toLowerCase());
	}
	
	/**
	 * Gets all {@link Topic}s
	 * 
	 * @return All registered {@link Topic}s
	 */
	public List<Topic> getTopics() {
		return new ArrayList<Topic>(topics.values());
	}
	
	/**
	 * Checks if the {@link Topic} has been registered
	 * 
	 * @param name The name of the {@link Topic}
	 * 
	 * @return True if found
	 */
	public boolean hasTopic(String name) {
		return topics.containsKey((name != null) ? name.toLowerCase() : "");
	}
	
	/**
	 * Checks if the {@link Topic} has been registered
	 * 
	 * @param topic The {@link Topic}
	 * 
	 * @return True if found
	 */
	public boolean hasTopic(Topic topic) {
		return hasTopic((topic != null) ? topic.getName() : "");
	}
	
	/**
	 * Loads the manager
	 */
	public void load() {
		registerTopics(
				new CommandsIndex().index(),
				new PermissionsIndex().index()
		);
		
		ConfigurationSection topicSection = getConfig().getConfigurationSection("topics");
		
		if (topicSection != null) {
			for (String topicName : topicSection.getKeys(false)) {
				String description = topicSection.getString(topicName + ".description", "");
				String information = topicSection.getString(topicName + ".information", "");
				
				GeneralTopic topic = new GeneralTopic(topicName, description);
				topic.setInformation(information);
				registerTopics(topic);
			}
		}
	}
	
	/**
	 * Registers the {@link Topic}s
	 * 
	 * @param topics The {@link Topic}s the register
	 */
	public void registerTopics(Topic... topics) {
		if (topics == null)
			return;
		
		for (Topic topic : topics) {
			if (topic == null)
				continue;
			
			if (hasTopic(topic)) {
				plugin.log(Level.WARNING, "Duplicate topic: " + topic.getName());
				continue;
			}
			
			this.topics.put(topic.getName().toLowerCase(), topic);
			db.debug(Level.INFO, "Registered topic: " + topic.getName());
		}
	}
	
	/**
	 * Reloads the manager
	 */
	public void reload() {
		for (Topic topic : getTopics())
			unregisterTopic(topic);
		
		registerTopics(
				new CommandsIndex().index(),
				new PermissionsIndex().index()
		);
		
		ConfigurationSection topicSection = getConfig().getConfigurationSection("topics");
		
		if (topicSection != null) {
			for (String topicName : topicSection.getKeys(false)) {
				String description = topicSection.getString(topicName + ".description", "");
				String information = topicSection.getString(topicName + ".information", "");
				
				GeneralTopic topic = new GeneralTopic(topicName, description);
				topic.setInformation(information);
				registerTopics(topic);
			}
		}
	}
	
	/**
	 * Reloads the configuration for {@link Topic}s
	 */
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "topic.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("topic.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	/**
	 * Saves the configuration for {@link Topic}s
	 */
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
	
	/**
	 * Unloads the manager
	 */
	public void unload() {
		for (Topic topic : getTopics())
			unregisterTopic(topic);
	}
	
	/**
	 * Unregisters the {@link Topic}
	 * 
	 * @param topic The {@link Topic} to unregister
	 */
	public void unregisterTopic(Topic topic) {
		if (topic == null || !hasTopic(topic))
			return;
		
		this.topics.remove(topic.getName().toLowerCase());
		db.debug(Level.INFO, "Unregistered topic: " + topic.getName());
	}
}