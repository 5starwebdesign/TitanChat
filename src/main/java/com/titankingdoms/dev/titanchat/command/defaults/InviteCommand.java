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

package com.titankingdoms.dev.titanchat.command.defaults;

import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.command.Command;
import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.participant.Participant;

public final class InviteCommand extends Command {
	
	public InviteCommand() {
		super("Invite");
		setAliases("inv");
		setArgumentRange(1, 1);
		setUsage("[player]");
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
	
	public final class Invitation {
		
		private final Channel channel;
		private final Participant participant;
		
		public Invitation(Channel channel, Participant participant) {
			this.channel = channel;
			this.participant = participant;
		}
		
		public Channel getChannel() {
			return channel;
		}
		
		public Participant getParticipant() {
			return participant;
		}
	}
}