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

package com.titankingdoms.dev.titanchat.core.channel;

import org.bukkit.configuration.file.FileConfiguration;

import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.loading.Loadable;

/**
 * {@link ChannelLoader} - Loads {@link Channel}s
 * 
 * @author NodinChan
 *
 */
public abstract class ChannelLoader extends Loadable {
	
	public ChannelLoader(String name) {
		super(name);
	}
	
	/**
	 * Constructs the {@link Channel}
	 * 
	 * @param name The name of the {@link Channel}
	 * 
	 * @return The constructed {@link Channel}
	 */
	public abstract Channel construct(String name);
	
	/**
	 * Loads the {@link Channel} from configuration
	 * 
	 * @param name The name of the {@link Channel}
	 * 
	 * @param status The {@link Status} of the {@link Channel}
	 * 
	 * @param config The configuration to load from
	 * 
	 * @return The loaded {@link Channel}
	 */
	public abstract Channel load(String name, Status status, FileConfiguration config);
}