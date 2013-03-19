package com.titankingdoms.dev.titanchat.addon;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.loading.Loadable;

public class ChatAddon extends Loadable {
	
	protected final TitanChat plugin;
	
	public ChatAddon(String name) {
		super(name);
		this.plugin = TitanChat.getInstance();
	}
	
	@Override
	public boolean equals(Object object) {
		if (object instanceof ChatAddon)
			return ((ChatAddon) object).getName().equals(getName());
		
		return false;
	}
	
	public final void register(ChatAddon... addons) {
		plugin.getAddonManager().registerAddons(addons);
	}
	
	public final void register(Channel... channels) {
		plugin.getChannelManager().registerChannels(channels);
	}
	
	public final void register(ChannelLoader... loaders) {
		plugin.getChannelManager().registerChannelLoaders(loaders);
	}
	
	public final void register(Command... commands) {
		plugin.getCommandManager().registerCommands(commands);
	}
	
	@Override
	public String toString() {
		return "ChatAddon:" + getName();
	}
}