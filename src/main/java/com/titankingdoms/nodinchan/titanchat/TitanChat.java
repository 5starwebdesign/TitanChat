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

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.permissions.Permission;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.java.JavaPlugin;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

import com.titankingdoms.nodinchan.titanchat.command.CommandManager;
import com.titankingdoms.nodinchan.titanchat.core.addon.AddonManager;
import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelManager;
import com.titankingdoms.nodinchan.titanchat.event.EmoteEvent;
import com.titankingdoms.nodinchan.titanchat.event.util.Message;
import com.titankingdoms.nodinchan.titanchat.loading.Loader;
import com.titankingdoms.nodinchan.titanchat.metrics.Metrics;
import com.titankingdoms.nodinchan.titanchat.metrics.Metrics.Graph;
import com.titankingdoms.nodinchan.titanchat.metrics.Metrics.Plotter;
import com.titankingdoms.nodinchan.titanchat.participant.ChannelParticipant;
import com.titankingdoms.nodinchan.titanchat.participant.ParticipantManager;
import com.titankingdoms.nodinchan.titanchat.permission.Permissions;
import com.titankingdoms.nodinchan.titanchat.permissions.SimplePermissionsBridge;
import com.titankingdoms.nodinchan.titanchat.processing.ChatPacket;
import com.titankingdoms.nodinchan.titanchat.processing.ChatProcessor;
import com.titankingdoms.nodinchan.titanchat.util.Debugger;
import com.titankingdoms.nodinchan.titanchat.util.FormatHandler;
import com.titankingdoms.nodinchan.titanchat.util.info.InfoHandler;

/**
 * TitanChat - Main class of TitanChat
 * 
 * @author NodinChan
 *
 */
public final class TitanChat extends JavaPlugin {
	
	private static TitanChat instance;
	
	private String NAME;
	
	private static final Logger log = Logger.getLogger("TitanLog");
	private static final Debugger db = new Debugger(0, "TitanChat");
	
	private Loader loader;
	
	private ChatProcessor processor;
	
	private TitanChatListener listener;
	private AddonManager addonManager;
	private ChannelManager channelManager;
	private CommandManager commandManager;
	private ParticipantManager participantManager;
	private InfoHandler info;
	private Permissions perms;
	private FormatHandler format;
	private SimplePermissionsBridge bridge;
	
	private boolean silenced = false;
	
	/**
	 * Logs the line to the console
	 * 
	 * @param line The line to log
	 */
	public void chatLog(String line) {
		if (getConfig().getBoolean("logging.colouring"))
			getServer().getConsoleSender().sendMessage(line.trim());
		else
			getServer().getConsoleSender().sendMessage(line.replaceAll("(?i)(\u00A7)([0-9a-fk-or])", "").trim());
	}
	
	/**
	 * Creates a list in String form seperated by commas
	 * 
	 * @param list The String list to create with
	 * 
	 * @return The created list
	 */
	public String createList(List<String> list) {
		StringBuilder str = new StringBuilder();
		
		for (String item : list) {
			if (str.length() > 0)
				str.append(", ");
			
			str.append(item);
		}
		
		db.i("TitanChat: Created list in String form: " + str.toString());
		return str.toString();
	}
	
	/**
	 * Checks if channels are enabled
	 * 
	 * @return True if channels are enabled
	 */
	public boolean enableChannels() {
		return getConfig().getBoolean("channels.enable");
	}
	
	/**
	 * Checks if join messages are enabled
	 * 
	 * @return True if join messages are enabled
	 */
	public boolean enableJoinMessage() {
		return getConfig().getBoolean("channels.messages.join");
	}
	
	/**
	 * Checks if leave messages are enabled
	 * 
	 * @return True if leave messages are enabled
	 */
	public boolean enableLeaveMessage() {
		return getConfig().getBoolean("channels.messages.leave");
	}
	
	public AddonManager getAddonManager() {
		return addonManager;
	}
	
	public ChannelManager getChannelManager() {
		return channelManager;
	}
	
	/**
	 * Gets the chat processor
	 * 
	 * @return The chat processor
	 */
	public ChatProcessor getChatProcessor() {
		return processor;
	}
	
	public CommandManager getCommandManager() {
		return commandManager;
	}
	
	/**
	 * Gets the format handler
	 * 
	 * @return The format handler
	 */
	public FormatHandler getFormatHandler() {
		return format;
	}
	
	/**
	 * Gets the info handler
	 * 
	 * @return The info handler
	 */
	public InfoHandler getInfoHandler() {
		return info;
	}
	
	/**
	 * Gets an instance of this
	 * 
	 * @return The TitanChat instance
	 */
	public static TitanChat getInstance() {
		return instance;
	}
	
	public Loader getLoader() {
		return loader;
	}
	
	@Override
	public Logger getLogger() {
		return log;
	}
	
	/**
	 * Gets the offline player with the specified name
	 * 
	 * @param name The name of the player
	 * 
	 * @return The offline player with the name whether the player had player before or not
	 */
	public OfflinePlayer getOfflinePlayer(String name) {
		OfflinePlayer player = getServer().getOfflinePlayer(name);
		return player;
	}
	
	public ChannelParticipant getParticipant(String name) {
		return participantManager.getParticipant(name);
	}
	
	public ParticipantManager getParticipantManager() {
		return participantManager;
	}
	
	/**
	 * Gets the permissions loader
	 * 
	 * @return The permissions loader
	 */
	public Permissions getPermissions() {
		return perms;
	}
	
	public SimplePermissionsBridge getPermBridge() {
		return bridge;
	}
	
	/**
	 * Gets the player with the specified name
	 * 
	 * @param name The name of the player
	 * 
	 * @return The player with the specified name if online, otherwise null
	 */
	public Player getPlayer(String name) {
		return getServer().getPlayer(name);
	}
	
	/**
	 * Initialise Metrics
	 * 
	 * @return True if Metrics is successfully initialised
	 */
	private boolean initMetrics() {
		log(Level.INFO, "Hooking Metrics");
		
		try {
			Metrics metrics = new Metrics(this);
			
			if (metrics.isOptOut())
				return true;
			
			Graph metricsStats = metrics.createGraph("Stats");
			
			metricsStats.addPlotter(new Plotter("Characters") {
				
				@Override
				public int getValue() {
					return (int) listener.getCharacters();
				}
			});
			
			metricsStats.addPlotter(new Plotter("Lines") {
				
				@Override
				public int getValue() {
					return (int) listener.getLines();
				}
			});
			
			metricsStats.addPlotter(new Plotter("Words") {
				
				@Override
				public int getValue() {
					return (int) listener.getWords();
				}
			});
			
			metrics.addGraph(metricsStats);
			
			return metrics.start();
			
		} catch (Exception e) { return false; }
	}
	
	/**
	 * Checks if the entire server is silenced
	 * 
	 * @return True if the server is silenced
	 */
	public boolean isSilenced() {
		return silenced;
	}
	
	/**
	 * Checks if the player has TitanChat.staff
	 * 
	 * @param player The player to check
	 * 
	 * @return True if the player has TitanChat.staff
	 */
	public boolean isStaff(Player player) {
		return player.hasPermission("TitanChat.staff");
	}
	
	/**
	 * Logs to console
	 * 
	 * @param level The level
	 * 
	 * @param msg The message
	 */
	public void log(Level level, String msg) {
		log.log(level, "[" + NAME + "] " + msg);
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equals("titanchat")) {
			if (args.length < 1 || (args[0].startsWith("@") && args.length < 2)) {
				db.i("/TitanChat");
				
				sender.sendMessage(ChatColor.AQUA + "You are running " + this);
				send(MessageLevel.INFO, sender, "\"/titanchat commands [page]\" for command list");
				return true;
			}
			
			String command = args[0];
			String chName = null;
			
			if (args[0].startsWith("@")) {
				command = args[1];
				chName = args[0].substring(1);
			}
			
			String[] arguments = new String[0];
			
			if (args[0].startsWith("@"))
				arguments = Arrays.copyOfRange(args, 2, args.length);
			else
				arguments = Arrays.copyOfRange(args, 1, args.length);
			
			db.i("TitanChat: Passing " + command + " to CommandManager");
			commandManager.execute(sender, command, chName, arguments);
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("broadcast")) {
			db.i("TitanChat: Command confirmed to be /Broadcast");
			
			StringBuilder str = new StringBuilder();
			
			for (String arg : args) {
				if (str.length() > 0)
					str.append(" ");
				
				str.append(arg);
			}
			
			getServer().dispatchCommand(sender, "titanchat broadcast " + str.toString());
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("emote")) {
			db.i("TitanChat: Command confirmed to be /Emote");
			
			if (sender instanceof Player && !sender.hasPermission("TitanChat.emote")) {
				send(MessageLevel.WARNING, sender, "You do not have permission");
				return true;
			}
			
			if (!getConfig().getBoolean("chat." + ((sender instanceof Player) ? "player" : "server") + ".enable")) {
				send(MessageLevel.WARNING, sender, "Emote Command Disabled");
				return true;
			}
			
			StringBuilder str = new StringBuilder();
			
			for (String word : args) {
				if (str.length() > 0)
					str.append(" ");
				
				str.append(word);
			}
			
			String format = getFormatHandler().emoteFormat(sender, "");
			
			EmoteEvent event = new EmoteEvent(sender, new Message(format, str.toString()));
			getServer().getPluginManager().callEvent(event);
			
			String[] lines = this.format.splitAndFormat(event.getFormat(), "%message", event.getMessage());
			
			for (Player recipant : getServer().getOnlinePlayers())
				processor.sendPacket(new ChatPacket(recipant, lines));
			
			return true;
		}
		
		if (cmd.getName().equalsIgnoreCase("whisper")) {
			db.i("TitanChat: Command confirmed to be /Whisper");
			
			StringBuilder str = new StringBuilder();
			
			for (String arg : args) {
				if (str.length() > 0)
					str.append(" ");
				
				str.append(arg);
			}
			
			getServer().dispatchCommand(sender, "titanchat whisper " + str.toString());
			return true;
		}
		
		return false;
	}
	
	@Override
	public void onDisable() {
		log(Level.INFO, "is now disabling...");
		log(Level.INFO, "Unloading managers...");
		
		addonManager.unload();
		commandManager.unload();
		info.unload();
		
		log(Level.INFO, "is now disabled");
	}
	
	@Override
	public void onEnable() {
		log(Level.INFO, "is now enabling...");
		
		for (int id : getConfig().getIntegerList("logging.debug"))
			Debugger.startDebug(id);
		
		register(
				listener = new TitanChatListener(),
				processor = new ChatProcessor()
		);
		
		if (!initMetrics())
			log(Level.WARNING, "Failed to hook into Metrics");
		
		info = new InfoHandler();
		perms = new Permissions();
		format = new FormatHandler();
		
		InputStream permissionStream = getResource("permissions.yml");
		
		if (permissionStream != null) {
			Map<?, ?> yamlMap = (Map<?, ?>) new Yaml(new SafeConstructor()).load(permissionStream);
			Map<?, ?> permissionMap = (Map<?, ?>) yamlMap.get("permissions");
			
			List<Permission> permissions = new LinkedList<Permission>();
			
			if (permissionMap != null)
				permissions.addAll(Permission.loadPermissions(permissionMap, "Permission node '%s' in plugin description file for " + getDescription().getFullName() + " is invalid", PermissionDefault.OP));
			
			for (Permission permission : permissions)
				try { getServer().getPluginManager().addPermission(permission); } catch (Exception e) {}
		}
		
		addonManager.load();
		channelManager.load();
		commandManager.load();
		
		info.loadLoadedInfo();
		info.loadPlayerInfo();
		
		for (Player player : getServer().getOnlinePlayers())
			info.loadCachedInfo(player);
		
		if (channelManager.getDefaultChannels().isEmpty()) {
			log(Level.SEVERE, "A default channel is not defined");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		log(Level.INFO, "is now enabled");
	}
	
	@Override
	public void onLoad() {
		instance = this;
		NAME = "TitanChat " + instance.toString().split(" ")[1];
		
		File config = new File(getDataFolder(), "config.yml");
		
		if (!config.exists()) {
			log(Level.INFO, "Loading default config...");
			saveResource("config.yml", false);
		}
		
		this.addonManager = new AddonManager();
		this.channelManager = new ChannelManager();
		this.commandManager = new CommandManager();
		
		if (channelManager.getChannelDirectory().mkdirs()) {
			log(Level.INFO, "Creating channel directory...");
			saveResource("channels/Default.yml", false);
			saveResource("channels/Global.yml", false);
			saveResource("channels/Local.yml", false);
			saveResource("channels/Password.yml", false);
			saveResource("channels/Private.yml", false);
			saveResource("channels/Public.yml", false);
			saveResource("channels/README.txt", false);
			saveResource("channels/Staff.yml", false);
			saveResource("channels/World.yml", false);
		}
		
		File info = new File(getDataFolder(), "info.yml");
		
		if (!info.exists()) {
			log(Level.INFO, "Loading default info.yml...");
			saveResource("info.yml", false);
		}
	}
	
	/**
	 * Registers the listeners
	 * 
	 * @param listeners the listeners to register
	 */
	public void register(Listener... listeners) {
		for (Listener listener : listeners)
			getServer().getPluginManager().registerEvents(listener, this);
	}
	
	/**
	 * Sends the message to the sender
	 * 
	 * @param level The message level
	 * 
	 * @param sender The sender to send to
	 * 
	 * @param msg The message to send
	 */
	public void send(MessageLevel level, CommandSender sender, String msg) {
		db.i("@" + sender.getName() + ": " + msg);
		String format = "[" + level.getColour() + "TitanChat" + ChatColor.WHITE + "] " + level.getColour() + "%msg";
		sender.sendMessage(this.format.splitAndFormat(format, "%msg", msg));
	}
	
	/**
	 * Sends the message to the channel
	 * 
	 * @param level The message level
	 * 
	 * @param channel The channel to send to
	 * 
	 * @param msg The message to send
	 */
	public void send(MessageLevel level, Channel channel, String msg) {
		db.i("@Channel:" + channel.getName() + ": " + msg);
		channel.broadcast(level, msg);
	}
	
	/**
	 * Sends the message to the list of players
	 * 
	 * @param level The message level
	 * 
	 * @param players The players to send to
	 * 
	 * @param msg The message to send
	 */
	public void send(MessageLevel level, List<Player> players, String msg) {
		for (Player player : players)
			send(level, player, msg);
	}
	
	/**
	 * Sets the server to silenced
	 * 
	 * @param silenced
	 */
	public void setSilenced(boolean silenced) {
		db.i("Setting silenced to " + silenced);
		this.silenced = silenced;
	}
	
	/**
	 * Checks if the player can speak in the channel
	 * 
	 * @param player The player to check
	 * 
	 * @param channel The channel to speak in
	 * 
	 * @param message Whether the player should be told about the check
	 * 
	 * @return True if the player can't speak
	 */
	public boolean voiceless(Player player, Channel channel, boolean message) {
		if (player.hasPermission("TitanChat.voice." + channel.getName()))
			return false;
		
		if (!player.hasPermission("TitanChat.speak." + channel.getName())) {
			if (message)
				send(MessageLevel.WARNING, player, "You do not have permission");
			
			return true;
		}
		
		if (isSilenced()) {
			if (message)
				send(MessageLevel.WARNING, player, "The server is silenced");
			
			return true;
		}
		
		if (getParticipantManager().getParticipant(player).isMutedOn(channel)) {
			if (message)
				send(MessageLevel.WARNING, player, "You have been muted");
			
			return true;
		}
		
		return false;
	}
	
	/**
	 * MessageLevel - Level of the message
	 * 
	 * @author NodinChan
	 *
	 */
	public enum MessageLevel {
		INFO(ChatColor.GOLD),
		NONE(ChatColor.WHITE),
		PLUGIN(ChatColor.AQUA),
		WARNING(ChatColor.RED);
		
		private ChatColor colour;
		
		private MessageLevel(ChatColor colour) {
			this.colour = colour;
		}
		
		public ChatColor getColour() {
			return colour;
		}
	}
}