package com.ne0nx3r0.rareitemhunter.bosses;

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
        int iDifficulty = plugin.getServer().getOnlinePlayers().length * (maxChance / 100);

        if(iRoll < iDifficulty || 1==1)
        {
            if(plugin.bossManager.hasSpawnPoints())
            {
                final Location lSpawnedEgg = plugin.bossManager.spawnRandomBossEgg();
                
                if(lSpawnedEgg == null)
                {
                    //alerts on this issue are handled in plugin.bossManager.spawnRandomBossEgg
                    return;
                }

                for(Player player : plugin.getServer().getOnlinePlayers())
                {                
                    //TODO: custom compass conditional
                    //if(player.hasPermission("rareitemhunter.admin"))
                    //{
                        player.sendMessage(ChatColor.DARK_GREEN+"-------------- RareItemHunter ----------------");
                        player.sendMessage("A legendary monster egg has been created!");
                    //}
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
                                plugin.getServer().broadcastMessage(ChatColor.DARK_GRAY+"The egg you were tracking has faded away...");

                                player.setCompassTarget(player.getWorld().getSpawnLocation());
                            }
                        }
                    }
                },expiration);
            }
            else
            {
                plugin.getServer().broadcastMessage(ChatColor.DARK_GREEN+"-------------- RareItemHunter ----------------");
                plugin.getServer().broadcastMessage(ChatColor.RED+"Tried to spawn a boss, but no boss spawn points are defined!");//, "rareitemhunter.admin");
                plugin.getServer().broadcastMessage("Use /ri spawn add to add some points");//, "rareitemhunter.admin");
            }
            
        }
    }
}
