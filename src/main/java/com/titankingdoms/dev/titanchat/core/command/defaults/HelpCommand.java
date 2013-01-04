package com.titankingdoms.dev.titanchat.core.command.defaults;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandSender;

import com.titankingdoms.dev.titanchat.core.channel.Channel;
import com.titankingdoms.dev.titanchat.core.command.Command;
import com.titankingdoms.dev.titanchat.help.Help;
import com.titankingdoms.dev.titanchat.help.topic.Topic;
import com.titankingdoms.dev.titanchat.util.C;
import com.titankingdoms.dev.titanchat.util.ChatUtils;
import com.titankingdoms.dev.titanchat.util.Messaging;

public final class HelpCommand extends Command {
	
	public HelpCommand() {
		super("Help");
		setAliases("?");
		setArgumentRange(0, 1);
		setBriefDescription("Displays the help menu");
		setFullDescription(
				"Description: Displays the help menu and help topics\n" +
				"Aliases: 'help', '?'\n" +
				"Usage: /titanchat help <page/topic> <page>");
		setUsage("<topic> <page>");
	}
	
	private String createHeader(String header, char decor) {
		return StringUtils.center(" " + header + " ", 119, decor);
	}
	
	@Override
	public void execute(CommandSender sender, Channel channel, String[] args) {
		Help help = plugin.getHelp();
		
		Topic topic = help.getDefaultTopic();
		int page = 1;
		
		switch (args.length) {
			
		case 1:
			try { page = Integer.parseInt(args[0]); } catch (NumberFormatException e) { topic = help.getHelpTopic(args[0]); }
			break;
			
		case 2:
			topic = help.getHelpTopic(args[0]);
			try { page = Integer.parseInt(args[1]); } catch (NumberFormatException e) {}
			break;
		}
		
		if (topic == null) {
			Messaging.sendMessage(sender, C.RED + "Invalid Help Topic");
			return;
		}
		
		String[][] pages = getPages(topic, help.getConfig().getInt("settings.page-height", 7));
		
		String header = createHeader(topic.getName() + ((pages.length > 1) ? " (" + page + "/" + pages.length + ")" : ""), '=');
		Messaging.sendMessage(sender, header);
		Messaging.sendMessage(sender, pages[page - 1]);
	}
	
	public String[][] getPages(Topic topic, int pageHeight) {
		return ChatUtils.paginate(topic.getFullDescription(), 119, pageHeight);
	}
	
	@Override
	public boolean permissionCheck(CommandSender sender, Channel channel) {
		return true;
	}
}