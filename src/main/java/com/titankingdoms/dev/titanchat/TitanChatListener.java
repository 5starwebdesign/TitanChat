package com.titankingdoms.dev.titanchat;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;
import com.titankingdoms.dev.titanchat.core.participant.PlayerParticipant;
import com.titankingdoms.dev.titanchat.format.Format;
import com.titankingdoms.dev.titanchat.util.Messaging;

public final class TitanChatListener implements Listener {
	
	private final TitanChat plugin;
	
	private final String site = "http://dev.bukkit.org/server-mods/titanchat/";
	private final double currentVer = 4.0;
	private double newVer;
	
	public TitanChatListener() {
		this.plugin = TitanChat.getInstance();
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onAsyncPlayerChat(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		
		Participant participant = plugin.getParticipantManager().getParticipant(event.getPlayer());
		String message = event.getMessage();
		
		if (message.startsWith("@") && message.split(" ").length > 1) {
			Channel channel = plugin.getChannelManager().getChannel(message.split(" ")[0].substring(1));
			
			if (channel == null) {
				Messaging.sendMessage(event.getPlayer(), "§4Channel does not exist");
				return;
			}
			
			participant.chat(channel, message.substring(message.indexOf(" ") + 1, message.length()));
			
		} else { participant.chat(message); }
	}
	
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		if (plugin.getParticipantManager().hasParticipant(event.getPlayer().getName()))
			return;
		
		Participant participant = new PlayerParticipant(event.getPlayer());
		plugin.getParticipantManager().registerParticipants(participant);
		
		if (participant.hasPermission("TitanChat.update")) {
			if (updateCheck() > currentVer) {
				participant.sendMessage("§6" + newVer + " §5is out!");
				participant.sendMessage("§5You are running §6" + currentVer);
				participant.sendMessage("§5Update at §9" + site);
			}
		}
	}
	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		for (int line = 0; line < 4; line++)
			event.setLine(line, Format.colourise(event.getLine(line)));
	}
	
	private double updateCheck() {
		try {
			URL url = new URL("http://dev.bukkit.org/server-mods/titanchat/files.rss");
			
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(url.openStream());
			doc.getDocumentElement().normalize();
			
			Node node = doc.getElementsByTagName("item").item(0);
			
			if (node.getNodeType() == 1) {
				Node name = ((Element) node).getElementsByTagName("title").item(0);
				Node version = name.getChildNodes().item(0);
				this.newVer = Double.valueOf(version.getNodeValue().split(" ")[1].trim().substring(1));
			}
			
		} catch (Exception e) { this.newVer = currentVer; }
		
		return this.newVer;
	}
}