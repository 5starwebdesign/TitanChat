package com.titankingdoms.nodinchan.titanchat.core.command.defaults;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;

import com.titankingdoms.nodinchan.titanchat.core.channel.Channel;
import com.titankingdoms.nodinchan.titanchat.core.channel.Type;
import com.titankingdoms.nodinchan.titanchat.core.command.Command;
import com.titankingdoms.nodinchan.titanchat.help.HelpTopic;
import com.titankingdoms.nodinchan.titanchat.util.C;

public class BanCommand extends Command {
	
	public BanCommand() {
		super("Ban");
		setAliases("b", "blacklist");
		setDescription("Bans the player(s) from the channel");
		setUsage("[player]...");
		registerHelpTopic(new BanHelp());
	}
	
	@Override
	public Execution execute(CommandSender sender, Channel channel, String[] args) {
		if (args.length < 1)
			return Execution.end(sender, C.RED + "Invalid Argument Length", C.GOLD + getUsage());
		
		if (!channel.getType().equals(Type.NONE) && !channel.getType().equals(Type.CUSTOM))
			return Execution.end(sender, C.RED + "Command not available for default");
		
		if (!channel.isAdmin(sender.getName()))
			return Execution.end(sender, C.RED + "You do not have permission");
		
		if (!hasPermission(sender, "TitanChat.ban." + channel.getName()))
			return Execution.end(sender, C.RED + "You do not have permission");
		
		for (String playerName : args) {
			OfflinePlayer player = getOfflinePlayer(playerName);
			
			if (!channel.isBlacklisted(player)) {
				channel.getBlacklist().add(player.getName());
				
				if (channel.isAdmin(player))
					channel.getAdmins().remove(player.getName());
				
				if (channel.isParticipating(player))
					channel.leave(plugin.getParticipant(player.getName()));
				
				if (player.isOnline())
					msg(player.getPlayer(), C.RED + "You have been banned from " + channel.getName());
				
			} else { msg(sender, C.RED + getDisplayName(player) + " was already banned"); }
		}
		
		return Execution.end();
	}
	
	public final class BanHelp implements HelpTopic {
		
		public boolean canView(CommandSender sender) {
			return true;
		}
		
		public String getBriefDescription() {
			return "Bans the player(s) from the channel";
		}
		
		public String[] getFullDescription() {
			return new String[] {
					"Description: Bans the player(s) from the channel",
					"Aliases: 'b', 'blacklist'",
					"Usage: /titanchat <@[channel]> ban [player]..."
			};
		}
		
		public String getName() {
			return "Ban";
		}
	}
}