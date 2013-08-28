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

package com.titankingdoms.dev.titanchat.channel.standard;

import java.util.HashSet;
import java.util.Set;

import com.titankingdoms.dev.titanchat.api.EndPoint;
import com.titankingdoms.dev.titanchat.channel.Channel;

public final class StandardChannel extends Channel {
	
	private Range range;
	
	private String password;
	
	public StandardChannel(String name) {
		super(name);
		this.range = Range.STANDARD;
		this.password = "";
	}
	
	public String getPassword() {
		return password;
	}
	
	public Range getRange() {
		return range;
	}
	
	@Override
	public Set<EndPoint> getRelayPoints() {
		return new HashSet<EndPoint>();
	}
	
	@Override
	public void sendRawLine(String line) {
		for (EndPoint point : getRelayPoints())
			point.sendRawLine(line);
	}
	
	public void setPassword(String password) {
		this.password = (password != null) ? password : "";
	}
	
	public void setRange(Range range) {
		this.range = (range != null) ? range : Range.STANDARD;
	}
	
	public void setRange(String range) {
		setRange(Range.fromName(range));
	}
}