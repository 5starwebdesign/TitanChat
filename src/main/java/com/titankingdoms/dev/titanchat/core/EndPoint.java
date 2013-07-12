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

package com.titankingdoms.dev.titanchat.core;

import java.util.Set;

import com.titankingdoms.dev.titanchat.event.ChatEvent;

public interface EndPoint {
	
	public String getConversationFormat();
	
	public String getDisplayName();
	
	public int getLinkedPointCount();
	
	public <T extends EndPoint> int getLinkedPointCountByClass(Class<T> pointClass);
	
	public Set<EndPoint> getLinkedPoints();
	
	public <T extends EndPoint> Set<T> getLinkedPointsByClass(Class<T> pointClass);
	
	public String getName();
	
	public String getPointType();
	
	public ChatEvent handleMessage(EndPoint sender, String format, String message);
	
	public boolean isLinked(EndPoint endpoint);
	
	public void link(EndPoint endpoint);
	
	public void notice(String... messages);
	
	public void unlink(EndPoint endpoint);
}