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

package com.titankingdoms.dev.titanchat.api.guide;

public abstract class AbstractChapter implements Chapter {
	
	private final String title;
	
	private String description;
	
	public AbstractChapter(String title) {
		this.title = title;
		this.description = "";
	}
	
	@Override
	public String getDescription() {
		return description;
	}
	
	@Override
	public String getTitle() {
		return title;
	}
	
	public void setDescription(String description) {
		this.description = (description != null) ? description : "";
	}
}