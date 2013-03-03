package com.ne0nx3r0.rareitemhunter.property.skills;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import com.ne0nx3r0.rareitemhunter.property.ItemProperty;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import java.util.Random;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class CallLightning extends ItemProperty
{
    public CallLightning()
    {
        super(ItemPropertyTypes.SKILL,"Call Lightning","10% chance to strike an opponent with lightning per level",5,8);
    }
    
    @Override
    public boolean onDamageOther(final EntityDamageByEntityEvent e,Player p,int level)
    {
        if(new Random().nextInt(100) > level * 10
        && e.getEntity() instanceof LivingEntity)
        {
            final Location l = e.getEntity().getLocation();
            
            Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(RareItemHunter.self, new Runnable()
            {
                @Override
                public void run()
                {
                    l.getWorld().spawnEntity(l, EntityType.LIGHTNING);
                }
            },20);
            
            return true;
        }
        return false;
    }
}