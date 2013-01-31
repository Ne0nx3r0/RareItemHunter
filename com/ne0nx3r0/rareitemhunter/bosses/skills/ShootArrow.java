package com.ne0nx3r0.rareitemhunter.bosses.skills;

import com.ne0nx3r0.rareitemhunter.bosses.Boss;
import com.ne0nx3r0.rareitemhunter.bosses.BossSkill;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class ShootArrow extends BossSkill
{
    public ShootArrow()
    {
        super("Shoot Arrow");
    }
    
    @Override
    public boolean activateSkill(Boss boss,EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {       
        LivingEntity leBoss = (LivingEntity) e.getEntity();   
        
        leBoss.launchProjectile(Arrow.class);
        
        return true;
    }
}
