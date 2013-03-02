package com.ne0nx3r0.rareitemhunter;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class RecipeManager
{
    private final RareItemHunter plugin;
    private ItemStack compass;

    public RecipeManager(RareItemHunter plugin)
    {
        this.plugin = plugin;

        this.compass = new ItemStack(Material.COMPASS);
        ItemMeta itemMeta = compass.getItemMeta();
        itemMeta.setDisplayName(ChatColor.DARK_GREEN+"Legendary Compass");
        
        List<String> lore = new ArrayList<String>();
        
        lore.add(ChatColor.DARK_GRAY+"When tapped against the ground, this compass");
        lore.add(ChatColor.DARK_GRAY+"will attune itself to the nearest legendary");
        lore.add(ChatColor.DARK_GRAY+"boss egg.");
        
        itemMeta.setLore(lore);
        
        compass.setItemMeta(itemMeta);
        
        ShapelessRecipe compassRecipe = new ShapelessRecipe(compass);
        
        compassRecipe.addIngredient(Material.COMPASS);
        compassRecipe.addIngredient(Material.GOLD_INGOT);
        compassRecipe.addIngredient(Material.EMERALD);
        compassRecipe.addIngredient(Material.IRON_INGOT);
        compassRecipe.addIngredient(Material.DIAMOND);
        
        plugin.getServer().addRecipe(compassRecipe);
    }
    
    public ItemStack getCompass()
    {
        return this.compass;
    }
}
