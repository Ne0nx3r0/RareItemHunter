package com.ne0nx3r0.rareitemhunter.listeners;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import com.ne0nx3r0.rareitemhunter.bosses.Boss;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTameEvent;

public class RareItemHunterEntityListener implements Listener
{
    private final RareItemHunter plugin;

    public RareItemHunterEntityListener(RareItemHunter plugin)
    {
        this.plugin = plugin;
    }
    
    @EventHandler(priority=EventPriority.NORMAL)
    public void Damage(EntityDamageByEntityEvent e)
    {
        if(e.getEntity() instanceof LivingEntity)
        {
            LivingEntity leBoss = (LivingEntity) e.getEntity();
            Boss boss = plugin.bossManager.getBoss(leBoss);
            
            if(boss != null)
            {
                //TODO: Add boss skills here before taking damage/ec.

                int iRemainingHP = boss.takeDamage(e.getDamage());

                if(iRemainingHP > 0)
                {
                    
                }
                else//Dead
                {
                    //TODO: Add visual effects

                    e.setDamage(1);

                    leBoss.setHealth(leBoss.getMaxHealth());
                }
            }
            

        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent e)
    {
        //TODO: verify events to cancel, snow men melting, etc.
        //http://jd.bukkit.org/apidocs/org/bukkit/event/entity/EntityDamageEvent.DamageCause.html
    }
/*
    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityCombust(EntityCombustEvent e)
    {
        //TODO: fire immunity, rather than every creature being immune
        if(plugin.bossManager.isBoss(e.getEntity()))
        {
            e.setCancelled(true);
        }
    }*/

    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityTame(EntityTameEvent e)
    {
        if(plugin.bossManager.isBoss(e.getEntity()))
        {
            e.setCancelled(true);
        }
    }
}
