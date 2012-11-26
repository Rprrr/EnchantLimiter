package com.guyag.enchantlimiter;

import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityShootBowEvent;

public class HitListener implements Listener {

	EnchantLimiter plugin;
	
	@EventHandler
	public void entityDamage(EntityDamageByEntityEvent event) {
		
		if(event.getDamager().getType().equals(EntityType.PLAYER)) {
			
			//The item they are doing damage with.
			if(((Player)event.getDamager()).getItemInHand() != null) {
				((Player)event.getDamager()).setItemInHand(plugin.fixItem(((Player)event.getDamager()).getItemInHand()));
			}
			
			//Check their armour, while we're at it. 
			plugin.fixArmour((Player)event.getDamager());
		}
		
		//Now check the damaged.
		if(event.getEntity().getType().equals(EntityType.PLAYER)) {
			plugin.fixArmour((Player)event.getEntity());
		}
	}
	
	@EventHandler
	public void bowFire(EntityShootBowEvent event) {
		if(event.getEntityType().equals(EntityType.PLAYER)) {
			((Player)event.getEntity()).getInventory().setItemInHand(plugin.fixItem(((Player)event.getEntity()).getItemInHand()));
		}
	}
	
	public HitListener(EnchantLimiter instance) {
		plugin = instance;
	}
	
}
