package com.ne0nx3r0.rareitemhunter.boss;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BossSkillSpawn extends BossSkill
{
    private final EntityType entityType;
    
    public BossSkillSpawn(String sName,EntityType e)
    {
        super(sName);
        
        this.entityType = e;
    }

    @Override
    public boolean activateSkill(Boss boss,EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {       
        System.out.println(e.getCause());
        
        Location l = e.getEntity().getLocation();
        
        World w = l.getWorld();
        
        for(int i=0;i<level;i++)
        {
            System.out.println(i+" level:"+level);
            
            w.spawnEntity(l, this.entityType);
        }
        
        return true;
    }
}
