package com.ne0nx3r0.rareitemhunter.property.spells;

import com.ne0nx3r0.rareitemhunter.property.ItemProperty;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import org.bukkit.entity.EntityType;
import org.bukkit.event.player.PlayerInteractEvent;

public class SummonOcelot extends ItemProperty
{
    public SummonOcelot()
    {
        super(ItemPropertyTypes.SPELL,"Summon Ocelot","Creates one ocelot / level",1,16);
    }
    
    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        for(int i=0;i<level;i++)
        {
            e.getPlayer().getWorld().spawnEntity(e.getPlayer().getLocation(),EntityType.OCELOT);
        }

        return true;
    }
}