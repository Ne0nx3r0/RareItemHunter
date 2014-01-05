package com.ne0nx3r0.rareitemhunter.property.enchantment;

import com.ne0nx3r0.rareitemhunter.property.ItemProperty;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import org.bukkit.Material;
import org.bukkit.event.player.PlayerInteractEvent;

public class Fertilize extends ItemProperty
{
    public Fertilize()
    {
        super(ItemPropertyTypes.ENCHANTMENT,"Fertilize","Turns clicked dirt blocks to grass",2,1);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        if(e.getClickedBlock() != null && e.getClickedBlock().getType() == Material.DIRT)
        {
            e.getClickedBlock().setType(Material.GRASS);
            
            return true;
        }
        
        return false;
    }
}
