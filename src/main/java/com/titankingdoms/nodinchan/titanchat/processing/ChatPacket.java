package com.titankingdoms.nodinchan.titanchat.processing;

import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.TitanChat;

public final class ChatPacket {
	
	private final TitanChat plugin;
	
	private final String recipant;
	
	private final String[] chat;
	
	public ChatPacket(Player recipant, String[] chat) {
		this.plugin = TitanChat.getInstance();
		this.recipant = recipant.getName();
		this.chat = chat;
	}
	
	public void chat() {
		Player recipant = plugin.getPlayer(this.recipant);
		
		if (recipant != null)
			recipant.sendMessage(chat);
	}
}