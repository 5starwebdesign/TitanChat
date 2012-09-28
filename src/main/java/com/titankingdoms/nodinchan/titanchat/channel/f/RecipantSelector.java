package com.titankingdoms.nodinchan.titanchat.channel.f;

import java.util.List;

import org.bukkit.entity.Player;

public interface RecipantSelector {
	public List<Player> getRecipants(Player sender);
}