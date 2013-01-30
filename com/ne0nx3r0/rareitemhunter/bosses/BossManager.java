package com.ne0nx3r0.rareitemhunter.bosses;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import java.util.HashMap;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public class BossManager
{
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
    
    
}
