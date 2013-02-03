package com.ne0nx3r0.rareitemhunter.listeners;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import com.ne0nx3r0.rareitemhunter.bosses.Boss;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class RareItemHunterPlayerListener implements Listener
{
    private final RareItemHunter plugin;

    public RareItemHunterPlayerListener(RareItemHunter plugin)
    {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent e)
    {
        if(e.hasBlock())
        { 
            if(e.getClickedBlock().getType() == Material.DRAGON_EGG)
            {
                Location lClicked = e.getClickedBlock().getLocation();

                if(plugin.bossManager.isBossEgg(lClicked))
                {
                    Boss boss = plugin.bossManager.hatchBoss(lClicked);
                    
                    e.setCancelled(true);
                }
            }
            else if(e.hasItem() && e.getItem().equals(plugin.recipeManager.getCompass()))
            {
                Location lBossEgg = plugin.bossManager.getNearestBossEggLocation(e.getPlayer().getLocation());
                
                if(lBossEgg != null)
                {
                    e.getPlayer().setCompassTarget(lBossEgg);
                    
                    e.getPlayer().sendMessage(ChatColor.DARK_GREEN+"The compass glows, then points sharply");
                }
                else
                {
                    e.getPlayer().sendMessage(ChatColor.DARK_GRAY+"The compass glows for a moment, then fades...");
                }
            }
        }
    }
}
