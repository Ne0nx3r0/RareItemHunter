package com.ne0nx3r0.rareitemhunter.bosses;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import java.util.Random;
import org.bukkit.entity.Player;

public class RandomlyGenerateBossTask implements Runnable 
{
    private final RareItemHunter plugin;
    private final int maxChance;

    public RandomlyGenerateBossTask(RareItemHunter plugin,int maxChance)
    {
        this.plugin = plugin;
        this.maxChance = maxChance;
    }

    @Override
    public void run() 
    {
        Random random = new Random();
        
        int iRoll = random.nextInt(plugin.getServer().getMaxPlayers());
        int iDifficulty = plugin.getServer().getOnlinePlayers().length * (maxChance / 100);
        
        if(iRoll < iDifficulty)
        {
            for(Player player : plugin.getServer().getOnlinePlayers())
            {
                if(player.hasPermission("rareitemhunter.notifications.boss")
                && player.getInventory().contains(null))
                {
                    
                    
                    player.sendMessage("----------------------- RareItemHunter -------------------------");
                    player.sendMessage("         A legendary monster egg has been detected!");
                    player.sendMessage("----------------------------------------------------------------");
                }
            }
            
        }
    }
}
