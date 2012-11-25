package com.guyag.enchantlimiter;

import java.io.File;
import java.util.logging.Logger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class EnchantLimiter extends JavaPlugin{

	Logger log;
	FileConfiguration config;
	public int maxProt = 4;
	public int maxFireProt = 4;
	public int maxProjProt = 4;
	public int maxSharp = 5;
	public int maxFire = 2;
	public int maxBowPower = 5;
	public int maxBowFire = 1;
	public int maxBowInfinity = 1;
	public int configVersion = 1;
	
	@Override
	public void onEnable() {
		log = this.getLogger();
		final int version = 1;
		//listeners
		this.getServer().getPluginManager().registerEvents(new InventoryListener(this), this);
		this.getServer().getPluginManager().registerEvents(new HitListener(this), this);
		
		//Config stuff
		//Gen one if it doesn't exist
		if( !( new File(getDataFolder(), "config.yml").exists() ) ) {
			this.saveDefaultConfig();
		}
		
		//get config options
		loadConfig();
		
		if(version != configVersion) {
			log.warning("Outdated config version!");
			log.warning("Some values will default to maximum possible.");
		}
	}
	
	@Override
	public void onDisable() {
		
	}
	
	public void loadConfig() {
		config = this.getConfig();
		maxProt = config.getInt("maxProt");
		maxFireProt = config.getInt("maxFireProt");
		maxProjProt = config.getInt("maxProjProt");
		maxSharp = config.getInt("maxSharp");
		maxFire = config.getInt("maxFire");
		maxBowPower = config.getInt("maxBowPower");
		maxBowFire = config.getInt("maxBowFire");
		maxBowInfinity = config.getInt("maxBowInfinity");
		configVersion = config.getInt("configVersionDoNotTouch");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("enchantlimiter")) {
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("reload")) {
					if(sender.hasPermission("enchantlimiter.reload")) {
						loadConfig();
						sender.sendMessage("Config reloaded");
						log.info(""+maxProt);
					}
					else {
						sender.sendMessage("No permission");
					}
				}
			}
			else {
				sender.sendMessage("Unknown command");
			}
		}
		return true;
	}
	
	public int getMaxProt() {
		return maxProt;
	}
	public int getMaxFireProt() {
		return maxFireProt;
	}
	public int getMaxProjProt() {
		return maxProjProt;
	}
	public int getMaxSharp() {
		return maxSharp;
	}
	public int getMaxFire() {
		return maxFire;
	}
	public int getMaxBowPower() {
		return maxBowPower;
	}
	public int getMaxBowFire() {
		return maxBowFire;
	}
	public int getMaxBowInfinity() {
		return maxBowInfinity;
	}
	
	/**
	 * Fixes the inventory of the player specified, according to the config file.
	 * @param player  Player whose inventory should be fixed.
	 */
	public void fixInventory(Player player) {
		ItemStack[] inv = player.getInventory().getContents();
		for(int i=0;i<inv.length;i++) {
			if(inv[i] != null) {
				inv[i] = fixEnchant(inv[i]);
			}
		}
		player.getInventory().setContents(inv);
	}
	
	/**
	 * Fixes the armour of the player specified, according to the config file.
	 * @param player  Player whose armour should be fixed.
	 */
	public void fixArmour(Player player) {
		if(player.getInventory().getHelmet() != null) {
			player.getInventory().setHelmet(fixEnchant(player.getInventory().getHelmet()));
		}
		if(player.getInventory().getChestplate() != null) {
			player.getInventory().setChestplate(fixEnchant(player.getInventory().getChestplate()));
		}
		if(player.getInventory().getLeggings() != null) {
			player.getInventory().setLeggings(fixEnchant(player.getInventory().getLeggings()));
		}
		if(player.getInventory().getBoots() != null) {
			player.getInventory().setBoots(fixEnchant(player.getInventory().getBoots()));
		}	
	}
	
	/**
	 * Returns an ItemStack sans bad enchants
	 * @param item  ItemStack (potentially) with bad enchants.
	 * @return  ItemStack without bad enchants.
	 */
	public ItemStack fixEnchant(ItemStack item) {
		//###########################################
		//##############Armour enchants##############
		//###########################################
		if(item.containsEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL)) {
			if(item.getEnchantmentLevel(Enchantment.PROTECTION_ENVIRONMENTAL) > getMaxProt()) {
				item.removeEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL);
				if(getMaxProt() > 0) {
					item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, getMaxProt());
				}
			}
		}
		if(item.containsEnchantment(Enchantment.PROTECTION_FIRE)) {
			if(item.getEnchantmentLevel(Enchantment.PROTECTION_FIRE) > getMaxFireProt()) {
				item.removeEnchantment(Enchantment.PROTECTION_FIRE);
				if(getMaxFireProt() > 0) {
					item.addEnchantment(Enchantment.PROTECTION_FIRE, getMaxFireProt());
				}
			}
		}
		if(item.containsEnchantment(Enchantment.PROTECTION_PROJECTILE)) {
			if(item.getEnchantmentLevel(Enchantment.PROTECTION_PROJECTILE) > getMaxProjProt()) {
				item.removeEnchantment(Enchantment.PROTECTION_PROJECTILE);
				if(getMaxProjProt() > 0) {
					item.addEnchantment(Enchantment.PROTECTION_PROJECTILE, getMaxProjProt());
				}
			}
		}
		
		//###########################################
		//##############Sword enchants###############
		//###########################################
		if(item.containsEnchantment(Enchantment.DAMAGE_ALL)) {
			if(item.getEnchantmentLevel(Enchantment.DAMAGE_ALL) > getMaxSharp()) {
				item.removeEnchantment(Enchantment.DAMAGE_ALL);
				if(getMaxSharp() > 0) {
					item.addEnchantment(Enchantment.DAMAGE_ALL, getMaxSharp());
				}
			}
		}
		if(item.containsEnchantment(Enchantment.FIRE_ASPECT)) {
			if(item.getEnchantmentLevel(Enchantment.FIRE_ASPECT) > getMaxFire()) {
				item.removeEnchantment(Enchantment.FIRE_ASPECT);
				if(getMaxFire() > 0) {
					item.addEnchantment(Enchantment.FIRE_ASPECT, getMaxFire());
				}
			}
		}
		
		//###########################################
		//###############Bow enchants################
		//###########################################
		if(item.containsEnchantment(Enchantment.ARROW_DAMAGE)) {
			if(item.getEnchantmentLevel(Enchantment.ARROW_DAMAGE) > getMaxBowPower()) {
				item.removeEnchantment(Enchantment.ARROW_DAMAGE);
				if(getMaxBowPower() > 0) {
					item.addEnchantment(Enchantment.ARROW_DAMAGE, getMaxBowPower());
				}
			}
		}
		if(item.containsEnchantment(Enchantment.ARROW_FIRE)) {
			if(item.getEnchantmentLevel(Enchantment.ARROW_FIRE) > getMaxBowFire()) {
				item.removeEnchantment(Enchantment.ARROW_FIRE);
				if(getMaxBowFire() > 0) {
					item.addEnchantment(Enchantment.ARROW_FIRE, getMaxBowFire());
				}
			}
		}
		if(item.containsEnchantment(Enchantment.ARROW_INFINITE)) {
			if(item.getEnchantmentLevel(Enchantment.ARROW_INFINITE) > getMaxBowInfinity()) {
				item.removeEnchantment(Enchantment.ARROW_INFINITE);
				if(getMaxBowInfinity() > 0) {
					item.addEnchantment(Enchantment.ARROW_INFINITE, getMaxBowInfinity());
				}
			}
		}
		return item;
	}
	
}
