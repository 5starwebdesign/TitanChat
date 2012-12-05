package com.titankingdoms.nodinchan.titanchat.format.variable;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.titankingdoms.nodinchan.titanchat.TitanChat;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public final class VariableManager {
	
	private final TitanChat plugin;
	
	private File configFile;
	private FileConfiguration config;
	
	private final Pattern format = Pattern.compile("(%)([a-z0-9]+)", Pattern.CASE_INSENSITIVE);
	
	private final Map<String, FormatVariable> variables;
	
	public VariableManager() {
		this.plugin = TitanChat.getInstance();
		this.variables = new HashMap<String, FormatVariable>();
	}
	
	public FileConfiguration getConfig() {
		if (config == null)
			reloadConfig();
		
		return config;
	}
	
	public String getDefaultVariableFormat() {
		return getConfig().getString("formatting.default", "%var%");
	}
	
	private FormatVariable getVariable(String formatTag) {
		return variables.get(formatTag);
	}
	
	public String getVariableFormat(String formatTag) {
		return getConfig().getString("formatting.variables." + formatTag, getDefaultVariableFormat());
	}
	
	private boolean existingVariable(String formatTag) {
		return variables.containsKey(formatTag);
	}
	
	private boolean existingVariable(FormatVariable variable) {
		return existingVariable(variable.getFormatTag());
	}
	
	public void load() {
		
	}
	
	public String parse(Participant sender, Channel channel, String format) {
		StringBuffer parsed = new StringBuffer();
		Matcher match = this.format.matcher(format);
		
		while (match.find()) {
			if (!existingVariable(match.group()))
				continue;
			
			match.appendReplacement(parsed, getVariable(match.group()).getVariable(sender, channel));
		}
		
		return match.appendTail(parsed).toString();
	}
	
	public void register(FormatVariable... variables) {
		for (FormatVariable variable : variables) {
			if (existingVariable(variable))
				continue;
			
			this.variables.put(variable.getFormatTag(), variable);
		}
	}
	
	public void reloadConfig() {
		if (configFile == null)
			configFile = new File(plugin.getDataFolder(), "variables.yml");
		
		config = YamlConfiguration.loadConfiguration(configFile);
		
		InputStream defConfigStream = plugin.getResource("variables.yml");
		
		if (defConfigStream != null)
			config.setDefaults(YamlConfiguration.loadConfiguration(defConfigStream));
	}
	
	public void saveConfig() {
		if (configFile == null || config == null)
			return;
		
		try { config.save(configFile); } catch (Exception e) {}
	}
}