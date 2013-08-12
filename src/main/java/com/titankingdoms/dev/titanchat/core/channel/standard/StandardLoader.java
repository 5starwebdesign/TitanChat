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

package com.titankingdoms.dev.titanchat.core.channel.standard;

import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.ChannelLoader;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;

/**
 * {@link StandardLoader} - Loads {@link StandardChannel}s
 * 
 * @author NodinChan
 *
 */
public final class StandardLoader extends ChannelLoader {
	
	public StandardLoader() {
		super("Standard");
	}
	
	@Override
	public Channel construct(String name) {
		return new StandardChannel(name, Status.NONE);
	}

	@Override
	public Channel load(String name, Status status, FileConfiguration config) {
		return new StandardChannel(name, status);
	}
}