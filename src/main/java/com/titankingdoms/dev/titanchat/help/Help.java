/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.help;

import java.io.File;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.core.command.Command;
import com.titankingdoms.dev.titanchat.help.topic.defaults.DefaultCommandTopic;
import com.titankingdoms.dev.titanchat.help.topic.defaults.DefaultIndex;
import com.titankingdoms.dev.titanchat.help.topic.yaml.YamlCommandTopic;
import com.titankingdoms.dev.titanchat.help.topic.yaml.YamlGeneralTopic;
import com.titankingdoms.dev.titanchat.help.topic.yaml.YamlIndex;

public final class Help {
	
	private final TitanChat plugin;
	
	private File configFile;
	private FileConfiguration config;
	
	private final DefaultIndex defaultIndex;
	
	private final Map<String, Topic> topics;
	
	public Help() {
		this.plugin = TitanChat.getInstance();
		this.defaultIndex = new DefaultIndex(this);
		this.topics = new TreeMap<String, Topic>();
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public Topic getDefaultTopic() {
		return defaultIndex;
	}
	
	public Topic getHelpTopic(String topic) {
		return topics.get(topic.toLowerCase());
	}
	
	public List<Topic> getHelpTopics() {
		return new LinkedList<Topic>(topics.values());
	}
	
	public boolean hasHelpTopic(Topic topic) {
		return hasHelpTopic(topic.getName());
	}
	
	public boolean hasHelpTopic(String topic) {
		return topics.containsKey(topic.toLowerCase());
	}
	
	public void load() {
		if (getConfig().get("topics.commands") != null) {
			ConfigurationSection commandTopicSection = getConfig().getConfigurationSection("topics.commands");
			
			for (String name : commandTopicSection.getKeys(false))
				registerHelpTopics(new YamlCommandTopic(commandTopicSection.getConfigurationSection(name)));
		}
		
		for (Command command : plugin.getCommandManager().getCommands())
			registerHelpTopics(new DefaultCommandTopic(command));
		
		if (getConfig().get("topics.general") != null) {
			ConfigurationSection generalTopicSection = getConfig().getConfigurationSection("topics.general");
			
			for (String name : generalTopicSection.getKeys(false))
				registerHelpTopics(new YamlGeneralTopic(generalTopicSection.getConfigurationSection(name)));
		}
		
		if (getConfig().get("indexes") != null) {
			ConfigurationSection indexSection = getConfig().getConfigurationSection("indexes");
			
			for (String name : indexSection.getKeys(false))
				registerHelpTopics(new YamlIndex(this, indexSection.getConfigurationSection(name)));
		}
	}
	
	public void registerHelpTopics(Topic... topics) {
		for (Topic topic : topics) {
			if (hasHelpTopic(topic))
				continue;
			
			this.topics.put(topic.getName().toLowerCase(), topic);
		}
	}
	
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "help.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("help.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
}