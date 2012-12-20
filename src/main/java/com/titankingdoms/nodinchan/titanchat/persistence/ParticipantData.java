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