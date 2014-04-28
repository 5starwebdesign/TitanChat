/*
 *     Copyright (C) 2014  Nodin Chan
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

package com.titankingdoms.dev.titanchat.command.titanchat;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.command.Command;
import com.titankingdoms.dev.titanchat.api.command.guide.GenericAssistCommand;
import com.titankingdoms.dev.titanchat.api.conversation.Connection;
import com.titankingdoms.dev.titanchat.api.conversation.Network;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.conversation.user.UserManager;
import com.titankingdoms.dev.titanchat.utility.FormatUtils.Format;

public final class ConnectCommand extends Command {
	
	public ConnectCommand() {
		super("Connect");
		setAliases("c");
		setArgumentRange(2, 2);
		setDescription("Connects to a node with the given name and type");
		setSyntax("<name> <type>");
		register(new GenericAssistCommand(this));
	}
	
	@Override
	protected void execute(CommandSender sender, String[] args) {
		UserManager manager = plugin.getManager(UserManager.class);
		
		if (manager == null) {
			message(sender, Format.RED + "UserManager not found");
			return;
		}
		
		Node node = manager.get(sender);
		
		if (node == null) {
			message(sender, Format.RED + "Failed to find node representation");
			return;
		}
		
		Network network = plugin.getManager(Network.class);
		
		if (network == null) {
			message(sender, Format.RED + "Network not found");
			return;
		}
		
		if (!network.hasNode(args[1], args[0])) {
			message(sender, Format.RED + "The node [" + args[0] + "::" + args[1] + "] cannot be found");
			return;
		}
		
		Node target = network.getNode(args[1], args[0]);
		
		Connection connection = node.getConnection();
		
		if (connection.connect(target))
			message(sender, Format.AZURE + "You have connected to " + Format.GOLD + target.getName());
		else
			message(sender, Format.RED + "Failed to connect to " + Format.GOLD + target.getName());
	}
}