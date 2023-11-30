package org.perdume.portable_furnace;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import static org.bukkit.Bukkit.getServer;

public class EvH implements Listener {


    private Portable_furnace main = Portable_furnace.getPlugin(Portable_furnace.class);
    @EventHandler
    public void RC(PlayerInteractEvent e){
        if (e.getHand() != EquipmentSlot.HAND) return;
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK){
            try {
                if (e.getItem().getType() == Material.FURNACE && !e.getItem().getItemMeta().getEnchants().isEmpty()) {
                    FuranceGUI gui = main.manager.getFuranceGUI(e.getPlayer());
                    getServer().getPluginManager().registerEvents(gui, main);
                    gui.openInventory(e.getPlayer());
                    e.setCancelled(true);
                }
            }
            catch (Exception ignored){}
        }
    }
}
