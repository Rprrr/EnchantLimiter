package com.guyag.enchantlimiter;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;

public class InventoryListener implements Listener {

	EnchantLimiter plugin;
	
	/**
	 * Constructor
	 * @param instance  Instance of main plugin
	 */
	public InventoryListener(EnchantLimiter instance) {
		plugin = instance;
	}
	
	@EventHandler
	public void inventoryClick(final InventoryClickEvent event) {
		
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public void run() {
				plugin.fixArmour( (Player)event.getWhoClicked() );		
				plugin.fixInventory( (Player)event.getWhoClicked() );
			}
		}, 1L);		
		
	}
	
	@EventHandler
	public void inventoryOpen(InventoryOpenEvent event) {
		
		plugin.fixInventory((Player)event.getPlayer());		

		plugin.fixArmour( (Player)event.getPlayer() );
		
	}
	
	@EventHandler
	public void inventoryClose(InventoryCloseEvent event) {
		
		plugin.fixInventory((Player)event.getPlayer());
		
		plugin.fixArmour((Player)event.getPlayer());
		
	}

	/**
	 * Tests whether a given item is any armour
	 * @param id  ID of item to be tested
	 * @return  Whether it is armour or not
	 */
	public boolean isArmour(int id) {
		if(id >= 298 && id <= 317) {
			return true;
		}
		else {
			return false;
		}
	}
}