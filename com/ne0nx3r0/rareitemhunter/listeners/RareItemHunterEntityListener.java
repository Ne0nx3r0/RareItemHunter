package com.ne0nx3r0.rareitemhunter.listeners;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import com.ne0nx3r0.rareitemhunter.bosses.Boss;
import com.ne0nx3r0.utils.FireworkVisualEffect;
import java.util.logging.Level;
import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
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
                //TODO: Snowballs or eggs do extra damage to certain vulnerable bosses. each boss has a weakness
                Entity eAttacker = e.getDamager();
                
                if((eAttacker instanceof Arrow))
                {
                    eAttacker = ((Arrow) eAttacker).getShooter();
                }            
                

                int iRemainingHP = boss.takeDamage(e.getDamage());

                if(iRemainingHP > 0)
                {
                    e.setDamage(1);

                    leBoss.setHealth(leBoss.getMaxHealth());
                    
                    boss.activateRandomSkill(e,eAttacker);
                    
                    if(eAttacker instanceof Player)
                    {
                        Player pAttacker = (Player) eAttacker;
                        
                        pAttacker.sendMessage(boss.getName()+" HP: "+iRemainingHP+"/"+boss.getMaxHP());
                    }
                }
                else //Dead
                {
                    leBoss.setHealth(1);
                    
                    try
                    {
                        new FireworkVisualEffect().playFirework(
                            leBoss.getWorld(), leBoss.getLocation(),
                            FireworkEffect
                                .builder()
                                .with(FireworkEffect.Type.CREEPER)
                                .withColor(Color.RED)
                                .build()
                        );
                    }
                    catch (Exception ex)
                    {
                        plugin.getLogger().log(Level.SEVERE, null, ex);
                    }
                    
                    //TODO: Add give rare essence
                }
            }
            

        }
    }
    
    @EventHandler(priority = EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent e)
    {
        //TODO: verify events to cancel, snow men melting, being in lava, etc.
        //http://jd.bukkit.org/apidocs/org/bukkit/event/entity/EntityDamageEvent.DamageCause.html
    }

    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityCombust(EntityCombustEvent e)
    {
        //TODO: fire immunity, rather than every creature being immune
        if(plugin.bossManager.isBoss(e.getEntity()))
        {
            e.setCancelled(true);
        }
    }

    @EventHandler(priority=EventPriority.NORMAL,ignoreCancelled = true)
    public void onEntityTame(EntityTameEvent e)
    {
        if(plugin.bossManager.isBoss(e.getEntity()))
        {
            e.setCancelled(true);
        }
    }
}
