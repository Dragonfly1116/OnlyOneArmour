package com.por.nattapat;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.InventoryType.SlotType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener {

	PluginDescriptionFile pdf = this.getDescription();
    static Main pl;
    static String cur;
    boolean updateFound;
    final int CONFIG_VERSION = 2;
    
    @Override
    public void onEnable() {
        pl = this;
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Plugin: " + this.pdf.getName() + " By " + this.pdf.getAuthors()+ " .");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "This plugin is not for"
        		+ " sale. And it is trial version.");
        Bukkit.getConsoleSender().sendMessage(ChatColor.YELLOW + "Version: " + this.pdf.getVersion());
        try {
        	Bukkit.getServer().getPluginManager().registerEvents(new ArmourListener(), this);
        } catch (Exception e) {
        	Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Error while register event please contact creator.");
        	e.printStackTrace();
        }
    }
    
    @EventHandler
    public void OnInventoryClick(InventoryClickEvent e) {
    	boolean shift = false, numberkey = false;
		if(e.isCancelled()) return;
		if(e.getAction() == InventoryAction.NOTHING) return;// Why does this get called if nothing happens??
		if(e.getClick().equals(ClickType.SHIFT_LEFT) || e.getClick().equals(ClickType.SHIFT_RIGHT)){
			shift = true;
		}
		if(e.getClick().equals(ClickType.NUMBER_KEY)){
			numberkey = true;
		}
		if(e.getSlotType() != SlotType.ARMOR && e.getSlotType() != SlotType.QUICKBAR
				&& e.getSlotType() != SlotType.CONTAINER) return;
		if(e.getClickedInventory() != null && !e.getClickedInventory().getType().equals(InventoryType.PLAYER)) return;
		if (!e.getInventory().getType().equals(InventoryType.CRAFTING) && !e.getInventory().getType().equals(InventoryType.PLAYER)) return;
		if(!(e.getWhoClicked() instanceof Player)) return;
		
    }
    
    
    @Override
    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.AQUA +
                pdf.getName() + " has been disabled! (V." + pdf.getVersion() + ")");
    }
    

}