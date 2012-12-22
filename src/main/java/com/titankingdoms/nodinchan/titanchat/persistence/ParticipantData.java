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

package com.titankingdoms.nodinchan.titanchat.persistence;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.persistence.*;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "participant_data")
public class ParticipantData {
	
	@Id
	@NotEmpty
	@NotNull
	private String name;
	
	private String current;
	
	private Set<String> channels;
	private Set<String> ignoreList;
	
	private Map<String, Boolean> muted;
	
	public ParticipantData() {
		this("");
	}
	
	public ParticipantData(String name) {
		this.name = name;
		this.channels = new HashSet<String>();
		this.ignoreList = new HashSet<String>();
		this.muted = new HashMap<String, Boolean>();
	}
	
	public Set<String> getChannels() {
		return channels;
	}
	
	public String getCurrent() {
		return current;
	}
	
	public Set<String> getIgnoreList() {
		return ignoreList;
	}
	
	public Map<String, Boolean> getMuted() {
		return muted;
	}
	
	public String getName() {
		return name;
	}
	
	public void setChannels(Set<String> channels) {
		this.channels = channels;
	}
	
	public void setCurrent(String current) {
		this.current = current;
	}
	
	public void setIgnoreList(Set<String> ignoreList) {
		this.ignoreList = ignoreList;
	}
	
	public void setMuted(Map<String, Boolean> muted) {
		this.muted = muted;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}