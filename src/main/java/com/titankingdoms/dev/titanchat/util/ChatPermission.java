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

package com.titankingdoms.dev.titanchat.util;

import com.titankingdoms.dev.titanchat.core.channel.Channel;

public enum ChatPermission {
	AUTO_JOIN("auto.join"),
	AUTO_LEAVE("auto.leave"),
	BLACKLIST("blacklist"),
	KICK("kick"),
	MUTE("mute"),
	PARTICIPATE("participate"),
	RANK("rank"),
	SPEAK("speak");
	
	private String name;
	
	private ChatPermission(String name) {
		this.name = name;
	}
	
	public String getChannelPermission(Channel channel) {
		return "TitanChat." + name + "." + channel.getName();
	}
	
	public String getGlobalPermission() {
		return "TitanChat." + name + ".*";
	}
}