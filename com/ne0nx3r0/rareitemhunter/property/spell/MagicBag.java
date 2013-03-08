package com.ne0nx3r0.rareitemhunter.property.spell;

import com.ne0nx3r0.rareitemhunter.property.ItemProperty;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import org.bukkit.event.player.PlayerInteractEvent;

public class MagicBag extends ItemProperty
{
    public MagicBag()
    {
        super(ItemPropertyTypes.SPELL,"Magic Bag","Opens your ender chest",1,1);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        e.getPlayer().openInventory(e.getPlayer().getEnderChest());
        
        return true;
    }
}