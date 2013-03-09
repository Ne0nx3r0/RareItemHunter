package com.ne0nx3r0.rareitemhunter.listener;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonExtendEvent;

public class RareItemHunterPistonListener implements Listener
{
    private RareItemHunter plugin;

    public RareItemHunterPistonListener(RareItemHunter p)
    {
        this.plugin = p;
    }

    @EventHandler(priority=EventPriority.LOWEST, ignoreCancelled=true)
    public void onPistonExtend(BlockPistonExtendEvent e)
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