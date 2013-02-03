package com.ne0nx3r0.rareitemhunter;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
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
        
        lore.add(ChatColor.DARK_GRAY+"When tapped against the ground, this compass will attune itself to the nearest legendary boss egg.");
        
        itemMeta.setLore(lore);
        
        compass.setItemMeta(itemMeta);
    }
    
    public ItemStack getCompass()
    {
        return this.compass;
    }
}
