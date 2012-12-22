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

package com.titankingdoms.nodinchan.titanchat.core.participant.util;

import java.util.Set;

import javax.persistence.*;

import com.avaje.ebean.validation.NotEmpty;
import com.avaje.ebean.validation.NotNull;

@Entity
@Table(name = "participant_data")
public class ParticipantData {
	
	@Id
	@GeneratedValue
	private int id;
	
	@NotEmpty
	@NotNull
	private String name;
	
	@NotNull
	private String displayName;
	
	private String currentChannel;
	
	private Set<String> channels;
	
	public Set<String> getChannels() {
		return channels;
	}
	
	public String getCurrentChannel() {
		return currentChannel;
	}
	
	public String getDisplayName() {
		return displayName;
	}
	
	public int getId() {
		return id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setChannels(Set<String> channels) {
		this.channels = channels;
	}
	
	public void setCurrentChannel(String currentChannel) {
		this.currentChannel = currentChannel;
	}
	
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}