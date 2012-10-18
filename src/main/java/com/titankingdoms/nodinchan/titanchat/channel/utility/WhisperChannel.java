package com.titankingdoms.nodinchan.titanchat.channel.utility;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.titankingdoms.nodinchan.titanchat.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.channel.ChannelInfo;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Access;
import com.titankingdoms.nodinchan.titanchat.channel.enumeration.Range;
import com.titankingdoms.nodinchan.titanchat.command.CommandBase;
import com.titankingdoms.nodinchan.titanchat.command.info.Aliases;
import com.titankingdoms.nodinchan.titanchat.command.info.Command;
import com.titankingdoms.nodinchan.titanchat.command.info.Description;
import com.titankingdoms.nodinchan.titanchat.command.info.Usage;
import com.titankingdoms.nodinchan.titanchat.participant.ChannelParticipant;
import com.titankingdoms.nodinchan.titanchat.participant.Participant;

public final class WhisperChannel extends UtilityChannel {
	
	private final Map<String, ChannelParticipant> targets;
	
	public WhisperChannel() {
		super("Whisper");
		this.targets = new HashMap<String, ChannelParticipant>();
	}
	
	@Override
	public ChannelInfo getInfo() {
		return null;
	}
	
	@Override
	public Range getRange() {
		return Range.CHANNEL;
	}
	
	@Override
	public boolean hasAccess(Player player, Access access) {
		return false;
	}
	
	@Override
	public void onCommand(CommandSender sender, String[] args) {
		
	}
	
	@Override
	public List<Participant> selectRecipants(Player sender, String message) {
		return null;
	}
	
	public final class WhisperCommand extends CommandBase {
		
		@Command
		@Aliases("w")
		@Description("Whispers to the player")
		@Usage("whisper [player] <message>")
		public void whisper(CommandSender sender, Channel channel, String[] args) {
			try {
				if (hasPermission(sender, "TitanChat.whisper")) {
					if (!plugin.getConfig().getBoolean("utilities.whisper.enable-" + ((sender instanceof Player) ? "player" : "console"), true)) {
						if (!args[0].equals("!#") && isOffline(sender, args[0]))
							return;
						
						CommandSender recipant = (args[0].equals("!#")) ? getConsoleCommandSender() : getPlayer(args[0]);
						
						if (args.length > 2) {
							
						} else {
							
						}
						
					} else { plugin.send(WARNING, sender, "Whisper Command Disabled"); }
					
				} else { plugin.send(WARNING, sender, "You do not have permission"); }
				
			} catch (IndexOutOfBoundsException e) { invalidArgLength(sender, "whisper"); }
		}
	}
}