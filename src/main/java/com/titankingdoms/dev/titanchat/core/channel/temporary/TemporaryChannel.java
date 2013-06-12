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

package com.titankingdoms.dev.titanchat.core.channel.temporary;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.channel.info.Range;
import com.titankingdoms.dev.titanchat.core.channel.info.Status;
import com.titankingdoms.dev.titanchat.format.Format;

/**
 * {@link TemporaryChannel} - Temporary channels for communication
 * 
 * @author NodinChan
 *
 */
public final class TemporaryChannel extends Channel {
	
	public TemporaryChannel(String name) {
		super(name, "Temporary", Status.TEMPORARY);
	}
	
	@Override
	public String[] getAliases() {
		return new String[0];
	}
	
	public String getDisplayColour() {
		return "";
	}
	
	public String getFormat() {
		return Format.getChatFormat();
	}
	
	@Override
	public Range getRange() {
		return Range.CHANNEL;
	}
	
	public String getTag() {
		return getName();
	}
	
	@Override
	public void init() {}
	
	@Override
	public void save() {}
}