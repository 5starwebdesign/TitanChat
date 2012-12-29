/*
 *     TitanChat
 *     Copyright (C) 2012  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.format;

import com.titankingdoms.dev.titanchat.TitanChat;
import com.titankingdoms.dev.titanchat.format.variable.VariableManager;
import com.titankingdoms.dev.titanchat.util.Debugger;

public final class FormatHandler {
	
	private final TitanChat plugin;
	
	protected static final Debugger db = new Debugger(5, "FormatHandler");
	
	public static final String DEFAULT_FORMAT = "%tag %prefix%player%suffix&f: %message";
	
	private final VariableManager varManager;
	
	public FormatHandler() {
		this.plugin = TitanChat.getInstance();
		this.varManager = new VariableManager();
	}
	
	public String getDefaultFormat() {
		return DEFAULT_FORMAT;
	}
	
	public String getFormat() {
		return FormatUtils.colourise(plugin.getConfig().getString("formatting.format", DEFAULT_FORMAT));
	}
	
	public VariableManager getVariableManager() {
		return varManager;
	}
}