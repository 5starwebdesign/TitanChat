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

package com.titankingdoms.nodinchan.titanchat.core.participant;

import java.util.List;
import java.util.Map;

import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

public final class ParticipantConfig {
	
	private final ParticipantManager manager;
	
	private final ConfigurationSection section;
	
	public ParticipantConfig(Participant participant) {
		this.manager = TitanChat.getInstance().getParticipantManager();
		this.section = manager.getConfig().getConfigurationSection(participant.getName());
	}
	
	public Object get(String node) {
		return section.get(node);
	}
	
	public Object get(String node, Object def) {
		return section.get(node, def);
	}
	
	public boolean getBoolean(String node) {
		return section.getBoolean(node);
	}
	
	public boolean getBoolean(String node, boolean def) {
		return section.getBoolean(node, def);
	}
	
	public List<Byte> getByteList(String node) {
		return section.getByteList(node);
	}
	
	public List<Character> getCharacterList(String node) {
		return section.getCharacterList(node);
	}
	
	public ConfigurationSection getConfigurationSection(String path) {
		return section.getConfigurationSection(path);
	}
	
	public double getDouble(String node) {
		return section.getDouble(node);
	}
	
	public double getDouble(String node, double def) {
		return section.getDouble(node, def);
	}
	
	public List<Double> getDoubleList(String node) {
		return section.getDoubleList(node);
	}
	
	public List<Float> getFloatList(String node) {
		return section.getFloatList(node);
	}
	
	public int getInt(String node) {
		return section.getInt(node);
	}
	
	public int getInt(String node, int def) {
		return section.getInt(node, def);
	}
	
	public List<Integer> getIntegerList(String node) {
		return section.getIntegerList(node);
	}
	
	public ItemStack getItemStack(String node) {
		return section.getItemStack(node);
	}
	
	public ItemStack getItemStack(String node, ItemStack def) {
		return section.getItemStack(node, def);
	}
	
	public List<?> getList(String node) {
		return section.getList(node);
	}
	
	public List<?> getList(String node, List<?> def) {
		return section.getList(node, def);
	}
	
	public long getLong(String node) {
		return section.getLong(node);
	}
	
	public long getLong(String node, long def) {
		return section.getLong(node, def);
	}
	
	public List<Long> getLongList(String node) {
		return section.getLongList(node);
	}
	
	public List<Map<?, ?>> getMapList(String node) {
		return section.getMapList(node);
	}
	
	public OfflinePlayer getOfflinePlayer(String node) {
		return section.getOfflinePlayer(node);
	}
	
	public OfflinePlayer getOfflinePlayer(String node, OfflinePlayer def) {
		return section.getOfflinePlayer(node, def);
	}
	
	public List<Short> getShortList(String node) {
		return section.getShortList(node);
	}
	
	public String getString(String node) {
		return section.getString(node);
	}
	
	public String getString(String node, String def) {
		return section.getString(node, def);
	}
	
	public List<String> getStringList(String node) {
		return section.getStringList(node);
	}
	
	public Vector getVector(String node) {
		return section.getVector(node);
	}
	
	public Vector getVector(String node, Vector def) {
		return section.getVector(node, def);
	}
	
	public void set(String node, Object value) {
		section.set(node, value);
	}
}