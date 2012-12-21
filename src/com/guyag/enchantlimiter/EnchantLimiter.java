package com.guyag.enchantlimiter;

import java.io.File;
import java.util.Map;
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
	public int maxThorns = 3;
	public int maxSharp = 5;
	public int maxFire = 2;
	public int maxKB = 2;
	public int maxBowPower = 5;
	public int maxBowFire = 1;
	public int maxBowInfinity = 1;
	public int maxBowKB = 2;
	public boolean disableEnchanting = false;
	public int configVersion = 4;
	
	@Override
	public void onEnable() {
		log = this.getLogger();
		final int version = 4;
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
		//Only register the listener if they want enchanting disabled
		if(disableEnchanting) {
			this.getServer().getPluginManager().registerEvents(new EnchantListener(this), this);
		}	
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
		maxThorns = config.getInt("maxThorns");
		maxSharp = config.getInt("maxSharp");
		maxFire = config.getInt("maxFire");
		maxKB = config.getInt("maxKB");
		maxBowPower = config.getInt("maxBowPower");
		maxBowFire = config.getInt("maxBowFire");
		maxBowInfinity = config.getInt("maxBowInfinity");
		maxBowKB = config.getInt("maxBowKB");
		disableEnchanting = config.getBoolean("disableEnchanting");
		configVersion = config.getInt("configVersionDoNotTouch");
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(cmd.getName().equalsIgnoreCase("enchantlimiter")) {
			if(args.length > 0) {
				if(args[0].equalsIgnoreCase("reload")) {
					if(sender.hasPermission("enchantlimiter.reload")) {
						loadConfig();
						sender.sendMessage("Config reloaded");
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
	public int getMaxThorns() {
		return maxThorns;
	}
	public int getMaxSharp() {
		return maxSharp;
	}
	public int getMaxFire() {
		return maxFire;
	}
	public int getMaxKB() {
		return maxKB;
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
	public int getMaxBowKB() {
		return maxBowKB;
	}
	
	/**
	 * Fixes the inventory of the player specified, according to the config file.
	 * @param player  Player whose inventory should be fixed.
	 */
	public void fixInventory(Player player) {
		ItemStack[] inv = player.getInventory().getContents();
		for(int i=0;i<inv.length;i++) {
			if(inv[i] != null) {
				inv[i] = fixItem(inv[i]);
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
			player.getInventory().setHelmet(fixItem(player.getInventory().getHelmet()));
		}
		if(player.getInventory().getChestplate() != null) {
			player.getInventory().setChestplate(fixItem(player.getInventory().getChestplate()));
		}
		if(player.getInventory().getLeggings() != null) {
			player.getInventory().setLeggings(fixItem(player.getInventory().getLeggings()));
		}
		if(player.getInventory().getBoots() != null) {
			player.getInventory().setBoots(fixItem(player.getInventory().getBoots()));
		}	
	}
	
	/**
	 * Returns an ItemStack sans bad enchants
	 * @param item  ItemStack (potentially) with bad enchants.
	 * @return  ItemStack without bad enchants.
	 */
	public ItemStack fixItem(ItemStack item) {
		Map<Enchantment,Integer> enchants = fixEnchant(item.getEnchantments());
		for(Enchantment ench : item.getEnchantments().keySet()) {
			item.removeEnchantment(ench);
		}
		item.addEnchantments(enchants);
		return item;
	}
	

	/**
	 * Returns an enchantment map sans bad enchants.
	 * @param enchants  Map of enchants and levels (potentially) with bad enchants.
	 * @return  Enchantment map without bad enchants.
	 */
	public Map<Enchantment,Integer> fixEnchant(Map<Enchantment,Integer> enchants) {
		//###########################################
		//##############Armour enchants##############
		//###########################################
		if(enchants.containsKey(Enchantment.PROTECTION_ENVIRONMENTAL)) {
			if(enchants.get(Enchantment.PROTECTION_ENVIRONMENTAL) > getMaxProt()) {
				enchants.remove(Enchantment.PROTECTION_ENVIRONMENTAL);
				if(getMaxProt() > 0) {
					enchants.put(Enchantment.PROTECTION_ENVIRONMENTAL, getMaxProt());
				}
			}
		}
		if(enchants.containsKey(Enchantment.PROTECTION_FIRE)) {
			if(enchants.get(Enchantment.PROTECTION_FIRE) > getMaxFireProt()) {
				enchants.remove(Enchantment.PROTECTION_FIRE);
				if(getMaxFireProt() > 0) {
					enchants.put(Enchantment.PROTECTION_FIRE, getMaxFireProt());
				}
			}
		}
		if(enchants.containsKey(Enchantment.PROTECTION_PROJECTILE)) {
			if(enchants.get(Enchantment.PROTECTION_PROJECTILE) > getMaxProjProt()) {
				enchants.remove(Enchantment.PROTECTION_PROJECTILE);
				if(getMaxProjProt() > 0) {
					enchants.put(Enchantment.PROTECTION_PROJECTILE, getMaxProjProt());
				}
			}
		}
		if(enchants.containsKey(Enchantment.THORNS)) {
			if(enchants.get(Enchantment.THORNS) > getMaxThorns()) {
				enchants.remove(Enchantment.THORNS);
				if(getMaxThorns() > 0) {
					enchants.put(Enchantment.THORNS, getMaxThorns());
				}
			}
		}
		
		//###########################################
		//##############Sword enchants###############
		//###########################################
		if(enchants.containsKey(Enchantment.DAMAGE_ALL)) {
			if(enchants.get(Enchantment.DAMAGE_ALL) > getMaxSharp()) {
				enchants.remove(Enchantment.DAMAGE_ALL);
				if(getMaxSharp() > 0) {
					enchants.put(Enchantment.DAMAGE_ALL, getMaxSharp());
				}
			}
		}
		if(enchants.containsKey(Enchantment.FIRE_ASPECT)) {
			if(enchants.get(Enchantment.FIRE_ASPECT) > getMaxFire()) {
				enchants.remove(Enchantment.FIRE_ASPECT);
				if(getMaxFire() > 0) {
					enchants.put(Enchantment.FIRE_ASPECT, getMaxFire());
				}
			}
		}
		if(enchants.containsKey(Enchantment.KNOCKBACK)) {
			if(enchants.get(Enchantment.KNOCKBACK) > getMaxKB()) {
				enchants.remove(Enchantment.KNOCKBACK);
				if(getMaxKB() > 0) {
					enchants.put(Enchantment.KNOCKBACK, getMaxKB());
				}
			}
		}
		
		//###########################################
		//###############Bow enchants################
		//###########################################
		if(enchants.containsKey(Enchantment.ARROW_DAMAGE)) {
			if(enchants.get(Enchantment.ARROW_DAMAGE) > getMaxBowPower()) {
				enchants.remove(Enchantment.ARROW_DAMAGE);
				if(getMaxBowPower() > 0) {
					enchants.put(Enchantment.ARROW_DAMAGE, getMaxBowPower());
				}
			}
		}
		if(enchants.containsKey(Enchantment.ARROW_FIRE)) {
			if(enchants.get(Enchantment.ARROW_FIRE) > getMaxBowFire()) {
				enchants.remove(Enchantment.ARROW_FIRE);
				if(getMaxBowFire() > 0) {
					enchants.put(Enchantment.ARROW_FIRE, getMaxBowFire());
				}
			}
		}
		if(enchants.containsKey(Enchantment.ARROW_INFINITE)) {
			if(enchants.get(Enchantment.ARROW_INFINITE) > getMaxBowInfinity()) {
				enchants.remove(Enchantment.ARROW_INFINITE);
				if(getMaxBowInfinity() > 0) {
					enchants.put(Enchantment.ARROW_INFINITE, getMaxBowInfinity());
				}
			}
		}
		if(enchants.containsKey(Enchantment.ARROW_KNOCKBACK)) {
			if(enchants.get(Enchantment.ARROW_KNOCKBACK) > getMaxBowKB()) {
				enchants.remove(Enchantment.ARROW_KNOCKBACK);
				if(getMaxBowKB() > 0) {
					enchants.put(Enchantment.ARROW_KNOCKBACK, getMaxBowKB());
				}
			}
		}
		return enchants;
	}
	
}
