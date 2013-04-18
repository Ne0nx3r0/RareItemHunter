package com.ne0nx3r0.rareitemhunter.boss;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BossSkillSpawn extends BossSkill
{
    private final EntityType entity;
    
    public BossSkillSpawn(String sName,EntityType e)
    {
        super(sName);
        
        this.entity = e;
    }

    @Override
    public boolean activateSkill(Boss boss,EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {       
        Location l = e.getEntity().getLocation();
        
        World w = l.getWorld();
        
        for(int i=0;i<level;i++)
        {
            w.spawnEntity(l, entity);
        }
        
        return true;
    }
}
