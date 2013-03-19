package com.titankingdoms.dev.titanchat.core.participant;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.core.ChatEntity;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.event.ChannelChatEvent;
import com.titankingdoms.dev.titanchat.format.Censor;
import com.titankingdoms.dev.titanchat.format.ChatUtils;
import com.titankingdoms.dev.titanchat.format.Format;
import com.titankingdoms.dev.titanchat.vault.Vault;

public class Participant extends ChatEntity {
	
	private volatile Channel current;
	
	private final Map<String, Channel> channels;
	
	public Participant(String name) {
		super("Participant", name);
		this.channels = new HashMap<String, Channel>();
	}
	
	public CommandSender asCommandSender() {
		return null;
	}
	
	public final void chat(Channel channel, String message) {
		if (channel == null)
			return;
		
		String format = channel.getFormat();
		
		if (format == null || format.isEmpty())
			format = Format.getFormat();
		
		Set<Participant> recipients = channel.getParticipants();
		
		ChannelChatEvent event = new ChannelChatEvent(this, recipients, channel, format, message);
		plugin.getServer().getPluginManager().callEvent(event);
		
		List<String> phrases = plugin.getConfig().getStringList("filtering.phrases");
		String censor = plugin.getConfig().getString("filtering.censor");
		
		message = Format.colourise(Censor.filter(event.getMessage(), phrases, censor));
		format = Format.colourise(event.getFormat());
		
		String[] lines = ChatUtils.wordWrap(format.replace("%message", message), 119);
		
		for (Participant recipient : event.getRecipients())
			recipient.sendMessage(lines);
		
		ConsoleCommandSender console = plugin.getServer().getConsoleSender();
		String log = event.getFormat().replace("%message", event.getMessage());
		
		if (plugin.getConfig().getBoolean("logging.colouring"))
			console.sendMessage(ChatUtils.wordWrap(Format.colourise(log), 119));
		else
			console.sendMessage(ChatUtils.wordWrap(Format.decolourise(log), 119));
	}
	
	public final void chat(String message) {
		chat(current, message);
	}
	
	public final void direct(Channel channel) {
		this.current = channel;
		
		if (channel != null && !isParticipating(channel))
			join(channel);
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof Participant)
			return ((Participant) object).getName().equals(getName());
		
		return false;
	}
	
	public final Set<Channel> getChannels() {
		return new HashSet<Channel>(channels.values());
	}
	
	@Override
	public FileConfiguration getConfig() {
		throw new UnsupportedOperationException("Participants do not have config files");
	}
	
	public final Channel getCurrent() {
		return current;
	}
	
	@Override
	public ConfigurationSection getDataSection() {
		FileConfiguration config = plugin.getParticipantManager().getConfig();
		return config.getConfigurationSection(getName().toLowerCase() + ".data");
	}
	
	public final boolean hasPermission(String node) {
		return Vault.hasPermission(asCommandSender(), node);
	}
	
	@Override
	public final void init() {
		
	}
	
	public final boolean isOnline() {
		return asCommandSender() != null;
	}
	
	public final boolean isParticipating(String channel) {
		return channels.containsKey(channel.toLowerCase());
	}
	
	public final boolean isParticipating(Channel channel) {
		return (channel != null) ? isParticipating(channel.getName()) : false;
	}
	
	public final void join(Channel channel) {
		if (channel == null)
			return;
		
		this.channels.put(channel.getName().toLowerCase(), channel);
		
		if (!channel.isParticipating(this))
			channel.join(this);
		
		if (!channel.equals(current))
			direct(channel);
	}
	
	public final void leave(Channel channel) {
		if (channel == null)
			return;
		
		this.channels.remove(channel.getName().toLowerCase());
		
		if (channel.isParticipating(this))
			channel.leave(this);
		
		if (channel.equals(current))
			direct(getChannels().iterator().hasNext() ? getChannels().iterator().next() : null);
	}
	
	@Override
	public void reloadConfig() {
		throw new UnsupportedOperationException("Participants do not have config files");
	}
	
	@Override
	public void save() {
		plugin.getParticipantManager().getConfig().set(getName() + ".channels.current", current.getName());
		plugin.getParticipantManager().getConfig().set(getName() + ".channels.all", channels.keySet());
	}
	
	@Override
	public void saveConfig() {
		throw new UnsupportedOperationException("Participants do not have config files");
	}
	
	@Override
	public void sendMessage(String... messages) {
		if (isOnline())
			asCommandSender().sendMessage(messages);
	}
	
	public Participant toParticipant() {
		Player player = plugin.getServer().getPlayer(getName());
		
		if (player == null)
			return this;
		
		return plugin.getParticipantManager().getParticipant(player.getName());
	}
}