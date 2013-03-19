package com.titankingdoms.dev.titanchat.core;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.configuration.ConfigurationSection;

import com.titankingdoms.dev.titanchat.data.DataValue;
import com.titankingdoms.dev.titanchat.loading.Loadable;

public abstract class ChatEntity extends Loadable {
	
	private final String type;
	
	private final Map<String, DataValue> data;
	
	public ChatEntity(String type, String name) {
		super(name);
		this.type = type;
		this.data = new HashMap<String, DataValue>();
	}
	
	public final DataValue getData(String key, DataValue def) {
		return (data.containsKey(key)) ? data.get(key) : def;
	}
	
	public final DataValue getData(String key, String def) {
		return getData(key, new DataValue(def));
	}
	
	public final Map<String, DataValue> getDataMap() {
		return new HashMap<String, DataValue>(data);
	}
	
	public abstract ConfigurationSection getDataSection();
	
	public final String getEntityType() {
		return type;
	}
	
	public void init() {}
	
	public final void loadData() {
		ConfigurationSection dataSection = getDataSection();
		
		if (dataSection == null)
			return;
		
		for (String key : dataSection.getKeys(false))
			setData(key, dataSection.getString(key));
	}
	
	public void save() {}
	
	public final void saveData() {
		ConfigurationSection dataSection = getDataSection();
		
		if (dataSection == null)
			return;
		
		for (String key : data.keySet())
			dataSection.set(key, data.get(key).asString());
	}
	
	public abstract void sendMessage(String... messages);
	
	public final void setData(String key, String value) {
		setData(key, new DataValue(value));
	}
	
	public final void setData(String key, DataValue value) {
		this.data.put(key, value);
	}
}