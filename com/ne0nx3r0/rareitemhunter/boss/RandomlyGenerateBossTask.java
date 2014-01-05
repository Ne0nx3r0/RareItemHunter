package com.ne0nx3r0.rareitemhunter.boss;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import java.util.Random;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class RandomlyGenerateBossTask implements Runnable 
{
    private final RareItemHunter plugin;
    private final int maxChance;
    private final int timer;
    private final int expiration;

    public RandomlyGenerateBossTask(RareItemHunter plugin,int maxChance,int timer,int expiration)
    {
        this.plugin = plugin;
        this.maxChance = maxChance;
        this.timer = timer;
        this.expiration = expiration;
    }

    @Override
    public void run() 
    {
        Random random = new Random();
        
        int iRoll = random.nextInt(plugin.getServer().getMaxPlayers());
        int iDifficulty = (int) (((float) plugin.getServer().getOnlinePlayers().length) * (((float) maxChance) / 100f));

        if(iRoll < iDifficulty)
        {
            if(plugin.bossManager.hasSpawnPoints())
            {
                final Location lSpawnedEgg = plugin.bossManager.spawnRandomBossEgg();
                
                if(lSpawnedEgg == null)
                {
                    //alerts on this issue are handled in plugin.bossManager.spawnRandomBossEgg
                    return;
                }

                for(Player player : lSpawnedEgg.getWorld().getPlayers())
                {                
                    player.sendMessage(ChatColor.DARK_GREEN+"A legendary monster egg has appeared!");
                }
                
                plugin.getServer().getScheduler().runTaskLater(plugin, new Runnable(){
                    @Override
                    public void run()
                    {
                        plugin.bossManager.removeBossEgg(lSpawnedEgg);

                        for(Player player : plugin.getServer().getOnlinePlayers())
                        {
                            if(player.getCompassTarget().equals(lSpawnedEgg))
                            {
                                plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN+"The egg you were tracking has hatched!");

                                player.setCompassTarget(player.getWorld().getSpawnLocation());
                            }
                        }
                    }
                },expiration);
            }
            else
            {
                plugin.getServer().broadcast(ChatColor.DARK_GREEN+"-------------- RareItemHunter ----------------", "rareitemhunter.admin.notify");
                plugin.getServer().broadcast(ChatColor.RED+"Tried to spawn a boss, but no boss spawn points are defined!", "rareitemhunter.admin.notify");
                plugin.getServer().broadcast("Use /ri spointpoint to add some points", "rareitemhunter.admin.notify");
            }
            
        }
    }
}
