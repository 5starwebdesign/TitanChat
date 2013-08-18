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
import java.util.List;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.ChannelManager;
import com.titankingdoms.dev.titanchat.core.user.UserManager;

public final class WhitelistCommand extends Command {
	
	public WhitelistCommand(String name) {
		super("Whitelist");
		setAliases("w");
		setArgumentRange(2, 1024);
		setDescription("Edit or view the whitelist");
		getLayer().registerAll(new Add(), new Remove(), new View());
	}
	
	@Override
	public void execute(CommandSender sender, String[] args) {
		getLayer().execute(sender, args);
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
			setDescription("Add the user to the whitelist of the channel");
		}

		@Override
		public void execute(CommandSender sender, String[] args) {}
		
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
			setDescription("Remove the user from the whitelist of the channel");
		}
		
		@Override
		public void execute(CommandSender sender, String[] args) {}
		
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
			setDescription("View the whitelist of the channel");
		}
		
		@Override
		public void execute(CommandSender sender, String[] args) {}
		
		@Override
		public List<String> tab(CommandSender sender, String[] args) {
			if (args.length < 2)
				return plugin.getManager(ChannelManager.class).match(args[0]);
			
			return new ArrayList<String>();
		}
	}
}