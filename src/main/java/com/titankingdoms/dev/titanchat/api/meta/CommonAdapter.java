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

package com.titankingdoms.dev.titanchat.api.meta;

import org.apache.commons.lang.Validate;

import com.titankingdoms.dev.titanchat.api.meta.Metadata.Meta;

public final class CommonAdapter implements MetaAdapter {
	
	@Override
	public Meta fromString(String value) {
		Validate.notNull(value, "Value cannot be null");
		return new Meta(value);
	}
	
	@Override
	public String getKey() {
		return "*";
	}
	
	@Override
	public String toString(Meta meta) {
		Validate.notNull(meta, "Meta cannot be null");
		return meta.getValue();
	}
}