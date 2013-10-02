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

package com.titankingdoms.dev.titanchat.core.command;

import java.util.Arrays;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.api.command.Command;
import com.titankingdoms.dev.titanchat.core.user.User;
import com.titankingdoms.dev.titanchat.core.user.UserManager;
import com.titankingdoms.dev.titanchat.tools.Messaging;

public final class PMCommand extends Command {
	
	public PMCommand(String name) {
		super("PM");
		setAliases("privmsg", "whisper", "tell");
		setArgumentRange(2, 1024);
		setDescription("Start a private conversation with the User");
	}
	
	@Override
	public void execute(CommandSender sender, String[] args, CommandData data) {
		UserManager manager = plugin.getManager(UserManager.class);
		
		if (!manager.isValid(args[0])) {
			Messaging.sendNotice(sender, "&4Invalid User name");
			return;
		}
		
		if (!manager.has(args[0])) {
			Messaging.sendNotice(sender, manager.getOfflineUser(args[0]).getDisplayName() + " &4is not online");
			return;
		}
		
		User user = manager.getUser(sender);
		User target = manager.get(args[0]);
		String format = "";
		String message = StringUtils.join(Arrays.copyOfRange(args, 1, args.length), ' ');
		
		plugin.converse(user, target, format, message);
	}
}