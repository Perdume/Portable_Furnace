package org.perdume.portable_furnace;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

public final class Portable_furnace extends JavaPlugin implements Listener {

    GUIManager manager;

    @Override
    public void onEnable() {
        manager = new GUIManager();
        getServer().getPluginManager().registerEvents(new EvH(), this);
        ItemStack bottle = new ItemStack(Material.FURNACE);
        ItemMeta itemMeta = bottle.getItemMeta();
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setDisplayName("간이 화로");
        bottle.setItemMeta(itemMeta);
        bottle.addUnsafeEnchantment(Enchantment.MENDING, 1);
        ShapedRecipe expBottle = new ShapedRecipe(bottle);
        expBottle.shape("***","*%*","***");
        expBottle.setIngredient('%', Material.FLINT_AND_STEEL);
        expBottle.setIngredient('*', Material.COBBLESTONE, 64);
        getServer().addRecipe(expBottle);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
