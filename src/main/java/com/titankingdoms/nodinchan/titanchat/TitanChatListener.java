/*     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat;

import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.titankingdoms.nodinchan.titanchat.event.chat.MessageSendEvent;

/**
 * TitanChatListener - Listens to events
 * 
 * @author NodinChan
 *
 */
public final class TitanChatListener implements Listener {

	private final TitanChat plugin;
	
	private long chars = 0;
	private long lines = 0;
	private long words = 0;
	
	private final double currentVer;
	private double newVer;
	
	public TitanChatListener() {
		this.plugin = TitanChat.getInstance();
		this.currentVer = Double.valueOf(plugin.getDescription().getVersion().trim().split(" ")[0].trim());
		this.newVer = updateCheck();
	}
	
	/**
	 * Gets the amount of characters sent
	 * 
	 * @return The amount of characters sent
	 */
	public long getCharacters() {
		return chars;
	}
	
	/**
	 * Gets the amount of lines sent
	 * 
	 * @return The amount of lines sent
	 */
	public long getLines() {
		return lines;
	}
	
	/**
	 * Gets the amount of words sent
	 * 
	 * @return The amount of words sent
	 */
	public long getWords() {
		return words;
	}
	
	/**
	 * Listens to MessageSendEvent
	 * 
	 * @param event MessageSendEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onMessageSend(MessageSendEvent event) {
		this.chars += event.getMessage().toCharArray().length;
		this.words += event.getMessage().split(" ").length;
		this.lines++;
	}
	
	/**
	 * Listens to PlayerJoinEvent
	 * 
	 * @param event PlayerJoinEvent
	 */
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onPlayerJoin(PlayerJoinEvent event) {
		
	}
	
	/**
	 * Listens to SignChangeEvent
	 * 
	 * @param event SignChangeEvent
	 */
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onSignChange(SignChangeEvent event) {
		for (int line = 0; line < 4; line++)
			event.setLine(line, plugin.getFormatHandler().colourise(event.getLine(line)));
	}
	
	/**
	 * Checks for an update
	 * 
	 * @return The newest version
	 */
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
	
	/**
	 * Checks for an update and tells the player if outdated
	 * 
	 * @param player The player to tell
	 */
	public void updateCheck(Player player) {
		if (updateCheck() <= currentVer || !player.hasPermission("TitanChat.update"))
			return;
		
		player.sendMessage("\u00A76" + newVer + " \u00A75is out! You are running \u00A76" + currentVer);
		player.sendMessage("\u00A75Update at \u00A79http://dev.bukkit.org/server-mods/titanchat");
	}
}