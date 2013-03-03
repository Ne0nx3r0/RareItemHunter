package com.ne0nx3r0.rareitemhunter.property.spells;

import com.ne0nx3r0.rareitemhunter.property.ItemProperty;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import org.bukkit.event.player.PlayerInteractEvent;

public class CraftItem extends ItemProperty
{
    public CraftItem()
    {
        super(ItemPropertyTypes.SPELL,"Craft Item","Opens a virtual crafting table",1,1);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        e.getPlayer().openWorkbench(null, true);
        
        return true;
    }
}