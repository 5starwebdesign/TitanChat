package com.titankingdoms.dev.titanchat.util;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicesManager;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.economy.EconomyResponse.ResponseType;
import net.milkbowl.vault.permission.Permission;

public final class VaultUtils {
	
	private static Chat chat;
	private static Economy econ;
	private static Permission perm;
	
	public static EconomyResponse depositBank(String name, double amount) {
		if (name == null || name.isEmpty() || amount < 0 || econ == null)
			return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, "Cannot deposit");
		
		return econ.bankDeposit(name, amount);
	}
	
	public static EconomyResponse depositPlayer(Player player, double amount) {
		if (player == null || amount < 0 || econ == null)
			return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, "Cannot deposit");
		
		return econ.depositPlayer(player.getName(), player.getWorld().getName(), amount);
	}
	
	public static double getBalance(Player player) {
		if (player == null || econ == null)
			return 0.0D;
		
		return econ.getBalance(player.getName(), player.getWorld().getName());
	}
	
	public static EconomyResponse getBankBalance(String name) {
		if (name == null || name.isEmpty() || econ == null)
			return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, "Cannot find balance");
		
		return econ.bankBalance(name);
	}
	
	public static List<String> getBanks() {
		if (econ == null)
			return new ArrayList<String>();
		
		List<String> banks = econ.getBanks();
		return (banks != null) ? banks : new ArrayList<String>();
	}
	
	public static String getGroupInfo(Player player, String node, String def) {
		if (player == null || node == null || node.isEmpty() || chat == null)
			return def;
		
		String group = chat.getPrimaryGroup(player.getWorld(), player.getName());
		
		if (group == null || group.isEmpty())
			return def;
		
		return chat.getGroupInfoString(player.getWorld(), group, node, def);
	}
	
	public static String getGroup(Player player) {
		if (player == null || perm == null)
			return "";
		
		String group = perm.getPrimaryGroup(player.getWorld(), player.getName());
		return (group != null) ? group : "";
	}
	
	public static String getGroupPrefix(Player player) {
		if (player == null || chat == null)
			return "";
		
		String group = chat.getPrimaryGroup(player.getWorld(), player.getName());
		
		if (group == null || group.isEmpty())
			return "";
		
		String prefix = chat.getGroupPrefix(player.getWorld(), group);
		return (prefix != null) ? prefix : "";
	}
	
	public static String[] getGroups(Player player) {
		if (player == null || perm == null)
			return new String[0];
		
		String[] groups = perm.getPlayerGroups(player.getWorld(), player.getName());
		return (groups != null) ? groups : new String[0];
	}
	
	public static String[] getGroups() {
		if (perm == null)
			return new String[0];
		
		String[] groups = perm.getGroups();
		return (groups != null) ? groups : new String[0];
	}
	
	public static String getGroupSuffix(Player player) {
		if (player == null || chat == null)
			return "";
		
		String group = chat.getPrimaryGroup(player.getWorld(), player.getName());
		
		if (group == null || group.isEmpty())
			return "";
		
		String suffix = chat.getGroupSuffix(player.getWorld(), group);
		return (suffix != null) ? suffix : "";
	}
	
	public static String getPlayerInfo(Player player, String node, String def) {
		if (player == null || node == null || node.isEmpty() || chat == null)
			return def;
		
		return chat.getPlayerInfoString(player.getWorld(), player.getName(), node, def);
	}
	
	public static String getPlayerPrefix(Player player) {
		if (player == null || chat == null)
			return "";
		
		String prefix = chat.getPlayerPrefix(player.getWorld(), player.getName());
		return (prefix != null) ? prefix : "";
	}
	
	public static String getPlayerSuffix(Player player) {
		if (player == null || chat == null)
			return "";
		
		String suffix = chat.getPlayerSuffix(player.getWorld(), player.getName());
		return (suffix != null) ? suffix : "";
	}
	
	public static boolean hasAccount(Player player) {
		if (player == null || econ == null)
			return false;
		
		return econ.hasAccount(player.getName(), player.getWorld().getName());
	}
	
	public static boolean hasPermission(CommandSender sender, String node) {
		if (sender == null || node == null || node.isEmpty())
			return false;
		
		if (perm == null)
			return sender.hasPermission(node);
		
		if (sender instanceof Player)
			return hasPermission((Player) sender, node);
		
		return perm.has(sender, node);
	}
	
	public static boolean hasPermission(Player player, String node) {
		if (player == null || node == null || node.isEmpty())
			return false;
		
		if (perm == null)
			return player.hasPermission(node);
		
		return perm.playerHas(player.getWorld(), player.getName(), node);
	}
	
	public static boolean initialise(Server server) {
		if (server == null)
			return false;
		
		if (server.getPluginManager().getPlugin("Vault") == null)
			return false;
		
		ServicesManager services = server.getServicesManager();
		
		RegisteredServiceProvider<Chat> chatProvider = services.getRegistration(Chat.class);
		
		if (chatProvider != null)
			chat = chatProvider.getProvider();
		
		RegisteredServiceProvider<Economy> econProvider = services.getRegistration(Economy.class);
		
		if (econProvider != null)
			econ = econProvider.getProvider();
		
		RegisteredServiceProvider<Permission> permProvider = services.getRegistration(Permission.class);
		
		if (permProvider != null)
			perm = permProvider.getProvider();
		
		return true;
	}
	
	public static boolean isChatSetup() {
		return chat != null;
	}
	
	public static boolean isEconomySetup() {
		return econ != null;
	}
	
	public static boolean isPermissionSetup() {
		return perm != null;
	}
	
	public static EconomyResponse withdrawBank(String name, double amount) {
		if (name == null || name.isEmpty() || amount < 0 || econ == null)
			return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, "Cannot withdraw");
		
		return econ.bankWithdraw(name, amount);
	}
	
	public static EconomyResponse withdrawPlayer(Player player, double amount) {
		if (player == null || amount < 0 || econ == null)
			return new EconomyResponse(0.0D, 0.0D, ResponseType.FAILURE, "Cannot withdraw");
		
		return econ.withdrawPlayer(player.getName(), player.getWorld().getName(), amount);
	}
}