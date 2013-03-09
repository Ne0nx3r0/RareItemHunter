package com.ne0nx3r0.rareitemhunter.listener;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import java.util.Iterator;
import java.util.List;
import org.bukkit.block.Block;
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
    public void onPistonExtendd(BlockPistonExtendEvent e)
    {
        List blocks = e.getBlocks();
        for(Iterator it = blocks.iterator(); it.hasNext();)
        {
            Block b = (Block) it.next();

            if(plugin.bossManager.isBossEgg(b))
            {
                e.setCancelled(true);

                break;
            }
        }
    }
}