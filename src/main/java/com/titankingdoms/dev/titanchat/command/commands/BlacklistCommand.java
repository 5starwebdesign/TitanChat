/*
 *     Copyright (C) 2013  Nodin Chan
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
 *     along with this program.  If not, see {http://www.gnu.org/licenses/}.
 */

package com.titankingdoms.dev.titanchat.command.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelManager;
import com.titankingdoms.dev.titanchat.core.user.User;
import com.titankingdoms.dev.titanchat.core.user.UserManager;
import com.titankingdoms.dev.titanchat.util.Messaging;
import com.titankingdoms.dev.titanchat.util.VaultUtils;

public final class BlacklistCommand extends Command {
	
	public BlacklistCommand(String name) {
		super("Blacklist");
		setAliases("b");
		setArgumentRange(2, 1024);
		setDescription("Edit or view the blacklist");
		getLayer().registerAll(new Add(), new Remove(), new View());
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		getLayer().execute(sender, args);
	}
	
	@Override
	public boolean isPermitted(CommandSender sender, String[] args) {
		return VaultUtils.hasPermission(sender, "TitanChat.blacklist." + args[1]);
	}
	
	@Override
	public List<String> tab(CommandSender sender, String[] args) {
		return getLayer().tab(sender, args);
	}
	
	public final class Add extends Command {
		
		public Add() {
			super("Add");
			setAliases("a");
			setArgumentRange(2, 1024);
			setDescription("Add the user to the blacklist of the channel");
		}

		@Override
		public void execute(CommandSender sender, String[] args) {
			ChannelManager chManager = plugin.getManager(ChannelManager.class);
			
			if (!chManager.has(args[0])) {
				Messaging.sendMessage(sender, "&4" + args[0] + " does not exist");
				return;
			}
			
			Channel channel = chManager.get(args[0]);
			
			if (Pattern.compile("\\W").matcher(args[1]).find()) {
				Messaging.sendMessage(sender, "&4Users cannot have non-word characters in names");
				return;
			}
			
			User user = plugin.getManager(UserManager.class).get(args[1]);
			
			if (channel.getBlacklist().contains(user.getName())) {
				Messaging.sendMessage(sender, user.getDisplayName() + " &4is already on the blacklist");
				return;
			}
			
			channel.getBlacklist().add(user.getName());
			user.sendNotice("&4You have been added to the blacklist of " + channel.getName());
			
			if (args.length > 2)
				user.sendNotice("&4Reason: " + StringUtils.join(Arrays.copyOfRange(args, 2, args.length), ' '));
			
			channel.sendNotice(user.getDisplayName() + " &6has been added to the blacklist");
		}
		
		@Override
		public List<String> tab(CommandSender sender, String[] args) {
			switch (args.length) {
			
			case 1:
				return plugin.getManager(ChannelManager.class).match(args[0]);
				
			case 2:
				return plugin.getManager(UserManager.class).match(args[1]);
			}
			
			return new ArrayList<String>();
		}
	}
	
	public final class Remove extends Command {
		
		public Remove() {
			super("Remove");
			setAliases("r");
			setArgumentRange(2, 1024);
			setDescription("Remove the user from the blacklist of the channel");
		}
		
		@Override
		public void execute(CommandSender sender, String[] args) {
			ChannelManager chManager = plugin.getManager(ChannelManager.class);
			
			if (!chManager.has(args[0])) {
				Messaging.sendMessage(sender, "&4" + args[0] + " does not exist");
				return;
			}
			
			Channel channel = chManager.get(args[0]);
			
			if (Pattern.compile("\\W").matcher(args[1]).find()) {
				Messaging.sendMessage(sender, "&4Users cannot have non-word characters in names");
				return;
			}
			
			User user = plugin.getManager(UserManager.class).get(args[1]);
			
			if (!channel.getBlacklist().contains(user.getName())) {
				Messaging.sendMessage(sender, user.getDisplayName() + " &4is not on the blacklist");
				return;
			}
			
			channel.getBlacklist().remove(user.getName());
			user.sendNotice("&6You have been removed from the blacklist of " + channel.getName());
			
			if (args.length > 2)
				user.sendNotice("&6Reason: " + StringUtils.join(Arrays.copyOfRange(args, 2, args.length), ' '));
			
			channel.sendNotice(user.getDisplayName() + " &6has been removed from the blacklist");
		}
		
		@Override
		public List<String> tab(CommandSender sender, String[] args) {
			switch (args.length) {
			
			case 1:
				return plugin.getManager(ChannelManager.class).match(args[0]);
				
			case 2:
				return plugin.getManager(UserManager.class).match(args[1]);
			}
			
			return new ArrayList<String>();
		}
	}
	
	public final class View extends Command {
		
		public View() {
			super("View");
			setAliases("v");
			setArgumentRange(1, 1);
			setDescription("View the blacklist of the channel");
		}
		
		@Override
		public void execute(CommandSender sender, String[] args) {
			ChannelManager chManager = plugin.getManager(ChannelManager.class);
			
			if (!chManager.has(args[0])) {
				Messaging.sendMessage(sender, "&4" + args[0] + " does not exist");
				return;
			}
			
			Channel channel = chManager.get(args[0]);
			
			Messaging.sendMessage(sender, "Blacklist: " + StringUtils.join(channel.getBlacklist(), ", "));
		}
		
		@Override
		public List<String> tab(CommandSender sender, String[] args) {
			if (args.length < 2)
				return plugin.getManager(ChannelManager.class).match(args[0]);
			
			return new ArrayList<String>();
		}
	}
}