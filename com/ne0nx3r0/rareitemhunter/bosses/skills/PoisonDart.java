package com.ne0nx3r0.rareitemhunter.bosses.skills;

import com.ne0nx3r0.rareitemhunter.bosses.Boss;
import com.ne0nx3r0.rareitemhunter.bosses.BossSkill;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PoisonDart extends BossSkill
{
    public PoisonDart()
    {
        super("Poison Dart");
    }
    
    @Override
    public boolean activateSkill(Boss boss,EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {       
        if(e.getEntity() instanceof LivingEntity)
        {        
            LivingEntity le = (LivingEntity) eAttacker;

            le.addPotionEffect(new PotionEffect(PotionEffectType.POISON,20*level*3,level));

            return true;
        }
        return false;
    }
}
