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

package com.titankingdoms.dev.titanchat.legacy;

import java.util.Collection;

import com.titankingdoms.dev.titanchat.api.conversation.Conversation;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.conversation.Provider;

public final class LegacyChat implements Provider<LegacyChat>, Node {
	
	@Override
	public void attach(Node node) {}
	
	@Override
	public void detach(Node node) {}
	
	@Override
	public LegacyChat get(String name) {
		return this;
	}
	
	@Override
	public String getName() {
		return "Legacy";
	}
	
	@Override
	public Collection<Node> getTerminusNodes() {
		return null;
	}
	
	@Override
	public String getType() {
		return "Minecraft";
	}
	
	@Override
	public boolean has(String name) {
		return true;
	}
	
	@Override
	public boolean has(LegacyChat item) {
		return true;
	}
	
	@Override
	public boolean isConnected(Node node) {
		return false;
	}
	
	@Override
	public boolean sendConversation(Conversation conversation) {
		return false;
	}
	
	@Override
	public void sendRawLine(String line) {}
}