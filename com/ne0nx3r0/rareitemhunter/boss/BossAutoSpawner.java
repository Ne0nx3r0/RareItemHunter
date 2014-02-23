package com.ne0nx3r0.rareitemhunter.boss;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class BossAutoSpawner implements Runnable{
    private final BossManager bm;
    private final double AUTOSPAWN_DISTANCE;
    private final RareItemHunter plugin;

    public BossAutoSpawner(RareItemHunter plugin, BossManager bm, double autoSpawnDistance)
    {
        this.plugin = plugin;
        this.bm = bm;
        
        // So don't square before you hand it to the object!
        this.AUTOSPAWN_DISTANCE = autoSpawnDistance * autoSpawnDistance;
    }

    @Override
    public void run()
    {
        for(BossEgg egg : this.bm.bossEggs.values()) {
            if(egg.getAutoSpawn()) {                
                for(Player p : Bukkit.getServer().getOnlinePlayers()) {
                    if(p.getLocation().distanceSquared(egg.getLocation()) < this.AUTOSPAWN_DISTANCE)
                    {
                        final Location lEgg = egg.getLocation();
                        
                        // Jump back into sync
                        plugin.getServer().getScheduler().runTask(plugin, new Runnable() {
                            @Override
                            public void run() {
                                bm.hatchBoss(lEgg);
                            }
                        });
                        
                        // only spawn one egg per attempt, could help with lag if a mass of eggs occurs
                        return;
                    }
                }
            }
        }
    }
}
