package com.guyag.enchantlimiter;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.enchantment.EnchantItemEvent;

public class EnchantListener implements Listener {

	EnchantLimiter plugin;
	
	@EventHandler
	public void enchantEvent(EnchantItemEvent event) {
		if(!event.getEnchantsToAdd().equals(plugin.fixEnchant(event.getEnchantsToAdd()))) {
			event.setCancelled(true);
		}
	}
	
	/**
	 * Constructor
	 * @param instance  Instance of main plugin
	 */
	public EnchantListener(EnchantLimiter instance) {
		plugin = instance;
	}
	
}
