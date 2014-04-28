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

package com.titankingdoms.dev.titanchat.conversation.console;

import com.titankingdoms.dev.titanchat.api.conversation.Connection;
import com.titankingdoms.dev.titanchat.api.conversation.Node;

public final class ConsoleConnection extends Connection {
	
	private final Console console;
	
	public ConsoleConnection(Console console) {
		super(console);
		this.console = console;
	}
	
	@Override
	public boolean connect(Node node) {
		if (!super.connect(node))
			return false;
		
		if (console.isViewing(node))
			return true;
		
		console.setViewing(node);
		return true;
	}
	
	@Override
	public boolean disconnect(Node node) {
		if (!super.disconnect(node))
			return false;
		
		if (!console.isViewing(node))
			return true;
		
		console.setViewing((getConnections().size() > 0) ? getConnections().iterator().next() : null);
		return true;
	}
}