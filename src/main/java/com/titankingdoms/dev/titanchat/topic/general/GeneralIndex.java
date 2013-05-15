/*
 *     Copyright (C) 2013  Nodin Chan <nodinchan@live.com>
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

package com.titankingdoms.dev.titanchat.topic.general;

import java.util.List;

import com.titankingdoms.dev.titanchat.topic.Index;
import com.titankingdoms.dev.titanchat.topic.Topic;

/**
 * {@link GeneralIndex} - The general index for all topics
 * 
 * @author NodinChan
 *
 */
public final class GeneralIndex extends Index {
	
	public GeneralIndex() {
		super("General", "General");
	}
	
	@Override
	public void addTopic(Topic topic) {
		plugin.getTopicManager().registerTopics(topic);
	}
	
	@Override
	public Topic getTopic(String name) {
		return plugin.getTopicManager().getTopic(name);
	}
	
	@Override
	public List<Topic> getTopics() {
		return plugin.getTopicManager().getTopics();
	}
	
	@Override
	public void removeTopic(Topic topic) {
		plugin.getTopicManager().unregisterTopic(topic);
	}
}