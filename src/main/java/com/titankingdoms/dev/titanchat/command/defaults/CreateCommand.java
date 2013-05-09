/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.command.defaults;

import java.io.File;
import java.io.IOException;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.event.ChannelCreationEvent;
import com.titankingdoms.dev.titanchat.vault.Vault;

/**
 * {@link CreateCommand} - Command for {@link Channel} creation
 * 
 * @author NodinChan
 *
 */
public final class CreateCommand extends Command {
	
	public CreateCommand() {
		super("Create");
		setAliases("c");
		setArgumentRange(1, 2);
		setDescription("Create a channel");
		setUsage("<channel> [type]");
	}

	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		if (plugin.getChannelManager().hasChannel(args[0])) {
			sendMessage(sender, "&4Channel already exists");
			return;
		}
		
		int limit = plugin.getChannelManager().getLimit();
		
		if (limit >= 0 && plugin.getChannelManager().getChannels().size() >= limit) {
			sendMessage(sender, "&4Limit reached");
			return;
		}
		
		File validate = new File(plugin.getChannelManager().getChannelDirectory(), args[0] + ".yml");
		
		try {
			validate.createNewFile();
			
		} catch (IOException e) {
			sendMessage(sender, "&4Invalid channel name");
			return;
		}
		
		String type = (args.length > 1) ? args[1] : "Standard";
		
		if (!plugin.getChannelManager().hasLoader(type)) {
			sendMessage(sender, "&4ChannelLoader does not exist");
			return;
		}
		
		ChannelLoader loader = plugin.getChannelManager().getLoader(type);
		channel = loader.construct(args[0]);
		plugin.getChannelManager().registerChannels(channel);
		
		ChannelCreationEvent event = new ChannelCreationEvent(channel);
		plugin.getServer().getPluginManager().callEvent(event);
		
		channel.getOperators().add(sender.getName());
		channel.join(plugin.getParticipantManager().getParticipant(sender));
		channel.getOperators().add(sender.getName());
		sendMessage(sender, "&6" + channel.getName() + " has been created");
		
		channel.getConfig().options().copyDefaults(true);
		channel.saveConfig();
	}

	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return Vault.hasPermission(sender, "TitanChat.create");
	}
}