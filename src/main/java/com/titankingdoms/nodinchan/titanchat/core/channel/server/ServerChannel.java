/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.nodinchan.titanchat.core.channel.server;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.nodinchan.titanchat.core.channel.ChatHandler;
import com.titankingdoms.nodinchan.titanchat.core.channel.Range;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;

public final class ServerChannel extends Channel {
	
	private final ChannelLoader loader;
	private final ChatHandler chatHandler;
	
	public ServerChannel() {
		super("Server", Type.DEFAULT);
		this.loader = new ServerLoader(this);
		this.chatHandler = new ServerChatHandler(this);
	}
	
	@Override
	public String[] getAliases() {
		return getConfig().getStringList("aliases").toArray(new String[0]);
	}
	
	@Override
	public ChannelLoader getChannelLoader() {
		return loader;
	}
	
	@Override
	public ChatHandler getChatHandler() {
		return chatHandler;
	}
	
	@Override
	public String getFormat() {
		return plugin.getFormatHandler().getFormat();
	}
	
	@Override
	public String getPassword() {
		return "";
	}
	
	@Override
	public Range getRange() {
		return Range.GLOBAL;
	}
	
	@Override
	public String getTag() {
		return "";
	}
	
	@Override
	public void reload() {}
}