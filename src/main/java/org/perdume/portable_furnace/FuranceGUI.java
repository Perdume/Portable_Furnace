package org.perdume.portable_furnace;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.FurnaceRecipe;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Iterator;

public class FuranceGUI implements Listener {
    private final Inventory inv;

    private Boolean isAct = false;

    private Portable_furnace main = Portable_furnace.getPlugin(Portable_furnace.class);

    public FuranceGUI(Player p) {
        // Create a new inventory, with no owner (as this isn't a real inventory), a size of nine, called example
        inv = Bukkit.createInventory(null, InventoryType.FURNACE);

        // Put the items into the inventory
    }

    // You can open the inventory with this
    public void openInventory(final HumanEntity ent) {
        ent.openInventory(inv);
    }

    // Check for clicks on items
    @EventHandler
    public void onInventoryClick(final InventoryClickEvent e) {
        if (!e.getInventory().equals(inv)) return;

        if(e.getSlot() == -999){
            if (e.getCursor().getType() == Material.FURNACE && !e.getCursor().getItemMeta().getEnchants().isEmpty()){
                e.setCancelled(true);
            }
        }

        new BukkitRunnable() {


            @Override
            public void run() {

                final ItemStack clickedItem = e.getCurrentItem();
                final ItemStack clickedItem2 = e.getCursor();
//        Bukkit.broadcastMessage("clid: " + clickedItem );
//        Bukkit.broadcastMessage("Cursor: " + clickedItem2 );

                final Player p = (Player) e.getWhoClicked();

                ItemStack it0 = inv.getItem(0);
                ItemStack it1 = inv.getItem(1);
                if(inv.getItem(0) == null){
                    it0 = new ItemStack(Material.AIR);
                }
                if(inv.getItem(1) == null){
                    it1 = new ItemStack(Material.AIR);
                }
                if (e.getSlot() == 2){
                    if(inv.getItem(2) == null && !it1.getType().isAir()&& !it0.getType().isAir()) {
                        it1.setAmount((int) (it1.getAmount() - Math.ceil((double) it0.getAmount() / 8)));
                        inv.setItem(0, new ItemStack(Material.AIR));
                        it0 = new ItemStack(Material.AIR);
                        isAct = true;
                    }
                    if(inv.getItem(2) != null && !it1.getType().isAir()&& !it0.getType().isAir()) {
                        int takenAmount = it0.getAmount() - inv.getItem(2).getAmount();
                        it1.setAmount((int) (it1.getAmount() - Math.ceil((double) takenAmount / 8)));
                        inv.setItem(0, new ItemStack(it0.getType(), it0.getAmount() - takenAmount));
                        it0 = inv.getItem(0);
                        isAct = true;
                    }
                }
                Boolean isdon = isDone(it0, it1);
                if (isdon){
                    if(inv.getItem(2) == null) {
                        if (it0.getType() == Material.AIR) {
                            inv.setItem(2, ReturnItem(clickedItem2));
                        } else {
                            inv.setItem(2, ReturnItem(it0));
                        }
                    }
                }
                else{
                    inv.setItem(2, new ItemStack(Material.AIR));
                }

            }
        }.runTaskLater(main, 1);
    }

    @EventHandler
    public void InvClose(InventoryCloseEvent e){
        if (!e.getInventory().equals(inv)) return;
        if(inv.getItem(0) != null){
            e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), inv.getItem(0));
        }
        if(inv.getItem(1) != null){
            e.getPlayer().getWorld().dropItem(e.getPlayer().getLocation(), inv.getItem(1));
        }
        for(ItemStack item: e.getPlayer().getInventory()){
            try {
                if (item.getType() == Material.FURNACE && !item.getItemMeta().getEnchants().isEmpty()) {
                    if(isAct) {
                        item.setAmount(item.getAmount() - 1);
                        return;
                    }
                }
            }
            catch (Exception ignored){}
        }
    }

    private ItemStack ReturnItem(ItemStack input){
        Iterator<Recipe> iter = Bukkit.recipeIterator();
        while (iter.hasNext()) {
            Recipe recipe = iter.next();
            if (!(recipe instanceof FurnaceRecipe)) continue;
            if (((FurnaceRecipe) recipe).getInput().getType() != input.getType()) continue;
            return new ItemStack(recipe.getResult().getType(), input.getAmount());
        }
        return null;
    }

    private Boolean isDone(ItemStack it0, ItemStack it1){
            if (it1.getType() == Material.COAL) {
                if (ReturnItem(it0) != null && it0 != null && !it0.getType().isAir()) {
                    return true;
                }
            }
        return false;
    }
}
