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

package com.titankingdoms.dev.titanchat.api.sequence;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

public abstract class Sequence<T> implements Comparator<T> {
	
	public abstract int compare(T item, T against);
	
	public final boolean follow(T item, T against) {
		return compare(item, against) < 0;
	}
	
	public final boolean lead(T item, T against){
		return compare(item, against) > 0;
	}
	
	public final List<T> reverse(Collection<T> items) {
		List<T> reversed = sort(items);
		Collections.reverse(reversed);
		return reversed;
	}
	
	public final List<T> sort(Collection<T> items) {
		List<T> sorted = new LinkedList<T>(items);
		Collections.sort(sorted, this);
		return sorted;
	}
}