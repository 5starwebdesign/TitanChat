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