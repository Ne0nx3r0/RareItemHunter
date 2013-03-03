package com.ne0nx3r0.rareitemhunter.property.spells;

import com.ne0nx3r0.rareitemhunter.property.ItemProperty;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;

public class SummonMooshroom extends ItemProperty
{
    public SummonMooshroom()
    {
        super(ItemPropertyTypes.SPELL,"Summon Mooshroom","Creates one mooshroom / level",1,19);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        for(int i=0;i<level;i++)
        {
            e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(),EntityType.MUSHROOM_COW);
        }

        return true;
    }
}