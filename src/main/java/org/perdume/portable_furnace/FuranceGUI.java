package org.perdume.portable_furnace;

import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.TileEntityFurnace;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Furnace;
import org.bukkit.craftbukkit.v1_20_R3.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;



import java.util.Arrays;
import java.util.Iterator;

public class FuranceGUI implements Listener {
    private final Inventory inv;

    private Boolean isAct = false;

    private Double BurningTimes = 1.5;

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

                final Player p = (Player) e.getWhoClicked();

                ItemStack it0 = inv.getItem(0);
                ItemStack it1 = inv.getItem(1);
                if(inv.getItem(0) == null){
                    it0 = new ItemStack(Material.AIR);
                }
                if(inv.getItem(1) == null){
                    it1 = new ItemStack(Material.AIR);
                }
                if (isDone(it0, it1)){
                    if(inv.getItem(2) != null) return;
                    if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
                    if(it1.getType() == Material.LAVA_BUCKET) {
                        inv.setItem(1, new ItemStack(Material.BUCKET));
                    }
                    else{
                        int buntimes = (int) (getBurntime(new ItemStack(it1.getType(), 1))/(200 * BurningTimes));
                        int col = (int) Math.ceil((double) it0.getAmount()/buntimes);
                        it1.setAmount(it1.getAmount() - col);
                    }
                    isAct = true;
                    inv.setItem(0, new ItemStack(it0.getType(),it0.getAmount() - RevCount(it0.getAmount())));
                    if(inv.getItem(2) == null || e.getSlot() != 2) {
                        if (it0.getType() == Material.AIR) {
                            inv.setItem(2, ReturnItem(clickedItem2));
                        } else {
                            inv.setItem(2, ReturnItem(it0));
                        }
                    }
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
            return new ItemStack(recipe.getResult().getType(), RevCount(input.getAmount()));
        }
        return null;
    }

    private int RevCount(int it0count){
        if(inv.getItem(1) == null){
            return 0;
        }
        int Count = inv.getItem(1).getAmount(); //Get ItemCount
        int BurnCount = (int) (getBurntime(inv.getItem(1))/(200 * BurningTimes)); //Get Max Burncount
        if(BurnCount >= it0count){
            return it0count;
        }
        else if(Count * BurnCount < 64){
            return Count * BurnCount;
        }
        else{
            return 64;
        }
    }

    private Boolean isDone(ItemStack it0, ItemStack it1){
            if (TileEntityFurnace.f().containsKey(CraftItemStack.asNMSCopy(it1).d())) {
                if (ReturnItem(it0) != null && it0 != null && !it0.getType().isAir()) {
                    return true;
                }
            }
        return false;
    }

    private int getBurntime(ItemStack is){
        Item i = CraftItemStack.asNMSCopy(is).d(); //Minecarft Item
        if (TileEntityFurnace.f().containsKey(i)) {
            return TileEntityFurnace.f().get(i);
        }
        return 0;
    }
}
