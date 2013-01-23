package com.titankingdoms.dev.titanchat.next.update;

public class ChatGroup extends ChatEntity {
	
	public ChatGroup(String name) {
		super(name);
	}
	
	@Override
	public String getEntityType() {
		return "ChatGroup";
	}
	
	@Override
	public void recalculatePermissions() {
		
	}
	
	@Override
	public void sendMessage(String message) {
		
	}
}