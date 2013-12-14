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

package com.titankingdoms.dev.titanchat.api.user;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.api.conversation.Node;
import com.titankingdoms.dev.titanchat.api.user.meta.Metadata;

public abstract class User implements Node {
	
	protected final TitanChat plugin;
	
	private final String name;
	
	private Metadata metadata;
	
	private final Set<Node> terminus = new HashSet<Node>();
	
	public User(String name) {
		Validate.notEmpty(name, "Name cannot be empty");
		Validate.isTrue(!Pattern.compile("\\W").matcher(name).find(), "Name cannot contain non-word characters");
		
		this.plugin = TitanChat.getInstance();
		this.name = name;
		this.terminus.add(this);
	}
	
	public final Metadata getMetadata() {
		if (metadata == null)
			metadata = plugin.getManager(UserManager.class).loadMetadata(this);
		
		return metadata;
	}
	
	@Override
	public final String getName() {
		return name;
	}
	
	@Override
	public String getPrefix() {
		return "*";
	}
	
	@Override
	public Set<Node> getTerminusNodes() {
		return Collections.unmodifiableSet(terminus);
	}
	
	@Override
	public final String getType() {
		return "User";
	}
	
	public final void saveMetadata() {
		plugin.getManager(UserManager.class).saveMetadata(getMetadata());
	}
}