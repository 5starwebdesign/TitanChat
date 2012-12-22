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

package com.titankingdoms.nodinchan.titanchat.help;

import java.util.Map;
import java.util.TreeMap;

public class HelpMap {
	
	private final Map<String, HelpTopic> topics;
	
	public HelpMap() {
		this.topics = new TreeMap<String, HelpTopic>();
	}
	
	public HelpTopic getHelpTopic(String topic) {
		return topics.get(topic.toLowerCase());
	}
	
	public void registerHelpTopics(HelpTopic... topics) {
		for (HelpTopic topic : topics) {
			if (this.topics.containsKey(topic.getName()))
				continue;
			
			this.topics.put(topic.getName().toLowerCase(), topic);
		}
	}
}