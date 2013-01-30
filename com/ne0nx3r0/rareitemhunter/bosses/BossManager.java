package com.ne0nx3r0.rareitemhunter.bosses;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import com.ne0nx3r0.rareitemhunter.bosses.skills.Burst;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class BossManager
{
    private Map<String,BossSkill> availableBossSkills;
    private Map<Location,Boss> inactiveBosses;
    private Map<Integer,Boss> activeBosses;
    private final RareItemHunter plugin;
    
    public BossManager(RareItemHunter plugin)
    {
        this.plugin = plugin;
        
        this.inactiveBosses = new HashMap<Location,Boss>();
        this.activeBosses = new HashMap<Integer,Boss>();
    }

    public boolean isBoss(Entity entity)
    {
        
        
        if(this.activeBosses.containsKey(entity.getEntityId()))
        {
            return true;
        }
        
        return false;
    }

    public Boss getBoss(Entity entity)
    {
        return this.activeBosses.get(entity.getEntityId());
    }

    public Boss getBoss(Location location)
    {
        return this.inactiveBosses.get(location);
    }

    public void spawnBossEgg(String bossName, Location location)
    {
        if(!this.inactiveBosses.containsKey(location))
        {
            //TODO: Persist inactive bosses
            
            //TODO: auto-Kill-off active bosses when appropriate
            
            location.getBlock().setType(Material.DRAGON_EGG);
            
            Boss boss = new Boss(bossName,100,100);
            boss.addSkill(new Burst(), 5, 25);
            
            this.inactiveBosses.put(location.getBlock().getLocation(), boss);
        }
    }

    public boolean isBossEgg(Location eggLocation)
    {
        return this.inactiveBosses.containsKey(eggLocation);
    }

    public Boss hatchBoss(Location eggLocation)
    {
        Boss boss = inactiveBosses.get(eggLocation);
        
        eggLocation.getBlock().setType(Material.AIR);
        
        Entity ent = eggLocation.getWorld().spawnEntity(eggLocation, EntityType.COW);
        
        boss.setEntity(ent);
        
        activeBosses.put(ent.getEntityId(), boss);
        
        inactiveBosses.remove(eggLocation);
        
        return boss;
    }
    
    
}
