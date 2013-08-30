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

package com.titankingdoms.dev.titanchat.channel.standard;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import com.titankingdoms.dev.titanchat.api.EndPoint;
import com.titankingdoms.dev.titanchat.channel.Channel;
import com.titankingdoms.dev.titanchat.user.User;
import com.titankingdoms.dev.titanchat.user.UserManager;

public final class StandardChannel extends Channel {
	
	private Range range;
	private int radius;
	private String password;
	
	public StandardChannel(String name) {
		super(name, "Standard");
		this.range = Range.STANDARD;
		this.radius = 15;
		this.password = "";
	}
	
	public String getPassword() {
		return password;
	}
	
	public int getRadius() {
		return radius;
	}
	
	public Range getRange() {
		return range;
	}
	
	@Override
	public Set<EndPoint> getRelayPoints(EndPoint sender) {
		Set<EndPoint> points = new HashSet<EndPoint>();
		
		UserManager manager = plugin.getManager(UserManager.class);
		
		switch (range) {
		
		case GLOBAL:
			points.addAll(manager.getAll());
			break;
			
		case LOCAL:
		case WORLD:
			if (!sender.getType().equals("User"))
				break;
			
			CommandSender cmdSender = ((User) sender).getCommandSender();
			
			if (!(cmdSender instanceof Player))
				break;
			
			switch (range) {
			
			case LOCAL:
				for (Entity entity : ((Player) cmdSender).getNearbyEntities(radius, radius, radius)) {
					if (!entity.getType().equals(EntityType.PLAYER))
						continue;
					
					points.add(manager.getUser((Player) entity));
				}
				break;
				
			case WORLD:
				for (Player player : ((Player) cmdSender).getWorld().getPlayers())
					points.add(manager.getUser(player));
				break;
				
			default:
				break;
			}
			break;
			
		case STANDARD:
			break;
		}
		
		return points;
	}
	
	@Override
	public void sendRawLine(String line) {}
	
	public void setPassword(String password) {
		this.password = (password != null) ? password : "";
	}
	
	public void setRadius(int radius) {
		this.radius = (radius >= 0) ? radius : 0;
	}
	
	public void setRange(Range range) {
		this.range = (range != null) ? range : Range.STANDARD;
	}
	
	public void setRange(String range) {
		setRange((range != null) ? Range.fromName(range) : Range.STANDARD);
	}
}