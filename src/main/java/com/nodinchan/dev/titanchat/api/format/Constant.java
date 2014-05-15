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

package com.nodinchan.dev.titanchat.api.format;

import org.apache.commons.lang.Validate;

import com.nodinchan.dev.conversation.Node;

public final class Constant implements Variable {
	
	private final String tag;
	
	private final String value;
	
	public Constant(String tag, String value) {
		Validate.notEmpty(tag, "Tag cannot be empty");
		
		this.tag = tag;
		this.value = (value != null) ? value : "";
	}
	
	@Override
	public String getTag() {
		return tag;
	}
	
	@Override
	public String getValue(Node sender, Node recipient) {
		return value;
	}
}