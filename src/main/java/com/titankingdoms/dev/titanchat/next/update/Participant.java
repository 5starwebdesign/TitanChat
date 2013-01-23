package com.titankingdoms.dev.titanchat.next.update;

public abstract class Participant extends ChatEntity {
	
	private ChatGroup group;
	
	public Participant(String name) {
		super(name);
	}
	
	public ChatGroup getChatGroup() {
		return group;
	}
	
	public void setChatGroup(ChatGroup group) {
		this.group = group;
	}
	
	@Override
	public String getEntityType() {
		return "Participant";
	}
}