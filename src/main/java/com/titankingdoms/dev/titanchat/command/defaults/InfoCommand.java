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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.format.ChatUtils;
import com.titankingdoms.dev.titanchat.format.Format;
import com.titankingdoms.dev.titanchat.topic.Index;
import com.titankingdoms.dev.titanchat.topic.Topic;
import com.titankingdoms.dev.titanchat.topic.TopicManager;
import com.titankingdoms.dev.titanchat.vault.Vault;

public final class InfoCommand extends Command {
	
	public InfoCommand() {
		super("Info");
		setAliases("?", "help");
		setArgumentRange(0, 1024);
		setDescription("Information about TitanChat");
		setUsage("[topic/page]...");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		TopicManager manager = plugin.getTopicManager();
		
		Topic topic = manager.getGeneralIndex();
		int page = 1;
		int pageWidth = manager.getConfig().getInt("page.width", 55);
		int pageHeight = manager.getConfig().getInt("page.height", 9);
		
		if (args.length > 0) {
			for (String arg : args) {
				if (!NumberUtils.isNumber(arg)) {
					if (topic instanceof Index) {
						topic = ((Index) topic).getTopic(arg);
						
						if (!Vault.hasPermission(sender, "TitanChat.topic." + topic.getName())) {
							sendMessage(sender, "&4You do not have permission to view this topic");
							return;
						}
						
					} else {
						break;
					}
					
				} else {
					page = NumberUtils.toInt(arg);
					break;
				}
			}
		}
		
		String[][] pages = ChatUtils.paginate(Format.colourise(topic.getInformation()), pageWidth, pageHeight);
		String header = topic.getName() + " (" + page + "/" + pages.length + ")";
		
		if (page < 1)
			page = 1;
		
		if (page > pages.length)
			page = pages.length;
		
		sendMessage(sender, "&b" + StringUtils.center(" " + header + " ", 55, '='));
		sendMessage(sender, pages[page - 1]);
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}