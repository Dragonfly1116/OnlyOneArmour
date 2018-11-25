package com.por.nattapat;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ArmorType {
	HELMET(5), CHESTPLATE(6), LEGGINGS(7), BOOTS(8);
	
	
	private final int slot; // slot number
	
	ArmorType(int slot) {
		this.slot = slot;
	}
	
	public final static ArmorType matchType(final ItemStack item) {
		if(item == null || item.getType().equals(Material.AIR)) return null;
		String type = item.getType().name();
		if(type.endsWith("_HELMET") || type.endsWith("_SKULL")) return HELMET;
		else if(type.endsWith("_CHESTPLATE")) return CHESTPLATE;
		else if(type.endsWith("_LEGGINGS")) return LEGGINGS;
		else if(type.endsWith("_BOOTS")) return BOOTS;
		else return null;
	}
	
	public int getSlot() {
		return this.slot;
	}
}
