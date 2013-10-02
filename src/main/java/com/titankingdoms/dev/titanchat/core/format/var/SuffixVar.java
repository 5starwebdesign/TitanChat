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

package com.titankingdoms.dev.titanchat.core.format.var;

import com.titankingdoms.dev.titanchat.api.Point;
import com.titankingdoms.dev.titanchat.api.event.ConverseEvent;
import com.titankingdoms.dev.titanchat.api.format.var.Var;
import com.titankingdoms.dev.titanchat.core.user.Participant;
import com.titankingdoms.dev.titanchat.core.user.User;
import com.titankingdoms.dev.titanchat.tools.util.VaultUtils;

public final class SuffixVar extends Var {
	
	public SuffixVar() {
		super("suffix");
	}
	
	@Override
	public String getValue(ConverseEvent event) {
		Point sender = event.getSender();
		
		if (!sender.getType().equals("User"))
			return "";
		
		User user = (User) sender;
		
		if (user.getName().equals("CONSOLE"))
			return user.getMetadata("suffix", "").getValue();
		
		String suffix = VaultUtils.getPlayerSuffix(((Participant) user).getPlayer());
		return (!suffix.isEmpty()) ? suffix : user.getMetadata("suffix", "").getValue();
	}
}