package com.ne0nx3r0.rareitemhunter.listener;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonEvent;
import org.bukkit.event.entity.EntityExplodeEvent;

public class RareItemHunterBlockListener implements Listener
{
    private RareItemHunter plugin;

    public RareItemHunterBlockListener(RareItemHunter p)
    {
        this.plugin = p;
    }

    public void onPistonExtend(EntityExplodeEvent e)
    {
        for(Block b : e.blockList())
        {
            if(plugin.bossManager.isBossEgg(b))
            {
                plugin.bossManager.removeBossEgg(b.getLocation());
            }
        }
    }
    
    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void onPistonExtend(BlockPistonEvent e)
    {
        BlockFace bf = e.getDirection();

        for(int i=1;i<=12;i++)
        {
            if(plugin.bossManager.isBossEgg(e.getBlock().getRelative(bf,i)))
            {
                e.setCancelled(true);

                break;
            }
        }
    }
    
    
}