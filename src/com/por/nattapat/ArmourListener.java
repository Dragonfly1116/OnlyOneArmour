package com.por.nattapat;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import net.md_5.bungee.api.ChatColor;



public class ArmourListener implements Listener {
	
	final String messageWarning = "You can equip only one armour :P";
	
	@EventHandler
	public final void onInventoryClick(InventoryClickEvent e) {
		boolean shift = false, numberkey = false;
		if(e.isCancelled()) return;
		if(e.getAction() == InventoryAction.NOTHING) return;// Why does this get called if nothing happens??
		if(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)){
			shift = true;
		}
		if(e.getClick().equals(ClickType.NUMBER_KEY)){
			numberkey = true;
		}
		// Get rid of other slottype and inventory
		if(e.getSlotType() != SlotType.ARMOR && e.getSlotType() != SlotType.QUICKBAR && e.getSlotType() != SlotType.CONTAINER) return;
		if(e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
		if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
		if(!(e.getWhoClicked() instanceof Player)) return;

		ArmorType newArmorType = ArmorType.matchType(shift ? e.getCurrentItem() : e.getCursor());
		
		if(!shift && newArmorType != null && e.getRawSlot() != newArmorType.getSlot()){
			// Used for drag and drop checking to make sure you aren't trying to place a helmet in the boots slot.
			return;
		}
		if(shift) {
			newArmorType = ArmorType.matchType(e.getCurrentItem());
			if(newArmorType != null) {
				boolean equipping = true;
				if(e.getRawSlot() == newArmorType.getSlot()) {
					// it is unequipping 
					equipping = false;
				}
				if(equipping && hasArmour(e.getWhoClicked()) ) {
					e.getWhoClicked().sendMessage(ChatColor.RED + messageWarning);
					e.setCancelled(true);
				}
			}
		} else {
			ItemStack newArmorPiece = e.getCursor();
			ItemStack oldArmorPiece = e.getCurrentItem();
			if(numberkey) {
				if(e.getClickedInventory().getType().equals(InventoryType.PLAYER)) {
					ItemStack hotbaritem = e.getClickedInventory().getItem(e.getHotbarButton());
					if(!isAirOrNull(hotbaritem)) {
						//equip
						newArmorType = ArmorType.matchType(hotbaritem);
						newArmorPiece = hotbaritem;
						oldArmorPiece = e.getClickedInventory().getItem(e.getSlot());
					} else {
						//unequip
						newArmorType = ArmorType.matchType(!isAirOrNull(e.getCurrentItem()) ? e.getCurrentItem(): e.getCursor());
					}
				}
			} else {
				if(isAirOrNull(e.getCursor()) && !isAirOrNull(e.getCurrentItem())){
					// unequip with no new item going into the slot.
					newArmorType = ArmorType.matchType(e.getCurrentItem());
				}
			}
			if(newArmorType != null && e.getRawSlot() == newArmorType.getSlot()){
				if(hasArmour(e.getWhoClicked())){
					e.getWhoClicked().sendMessage(ChatColor.RED + messageWarning);
					e.setCancelled(true);
				}
			}
		}
	}
	
	@EventHandler
	public void playInteractEvent(PlayerInteractEvent e) {
		if(e.getAction() == Action.PHYSICAL) return;
		if(e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
			final Player player = e.getPlayer();
			ArmorType newArmorType = ArmorType.matchType(e.getItem());
			if(newArmorType != null){
				if(newArmorType.equals(ArmorType.HELMET) && isAirOrNull(e.getPlayer().getInventory().getHelmet()) || newArmorType.equals(ArmorType.CHESTPLATE) && isAirOrNull(e.getPlayer().getInventory().getChestplate()) || newArmorType.equals(ArmorType.LEGGINGS) && isAirOrNull(e.getPlayer().getInventory().getLeggings()) || newArmorType.equals(ArmorType.BOOTS) && isAirOrNull(e.getPlayer().getInventory().getBoots())){
					if(hasArmour(player)){
						player.sendMessage(ChatColor.RED + messageWarning);
						e.setCancelled(true);
						player.updateInventory();
					}
				}
			}
		}
	}
	
	private final boolean hasArmour(HumanEntity h) {
		PlayerInventory inventory = h.getInventory();
		if(!isAirOrNull(inventory.getBoots()) ||
		   !isAirOrNull(inventory.getChestplate()) ||
		   !isAirOrNull(inventory.getLeggings()) ||
		   !isAirOrNull(inventory.getHelmet())) return true;
		return false;
	}
	
	private boolean isAirOrNull(ItemStack item) {
		return (item == null || item.getType().equals(Material.AIR)) ;
	}
	
}
