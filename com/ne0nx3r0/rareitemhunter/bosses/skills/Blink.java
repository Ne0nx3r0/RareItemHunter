package com.ne0nx3r0.rareitemhunter.bosses.skills;

import com.ne0nx3r0.rareitemhunter.bosses.Boss;
import com.ne0nx3r0.rareitemhunter.bosses.BossSkill;
import java.util.Random;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Blink extends BossSkill
{
    public Blink()
    {
        super("Blink");
    }
    
    @Override
    public boolean activateSkill(Boss boss,EntityDamageByEntityEvent e, Entity eAttacker, int level)
    {       
        Location lBoss = e.getEntity().getLocation();
        
        Random random = new Random();
        
        lBoss.add(random.nextInt(10), 0, random.nextInt(10));
        
        e.getEntity().teleport(lBoss);
        
        return true;
    }
}
