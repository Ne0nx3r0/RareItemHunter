package com.ne0nx3r0.rareitemhunter.bosses;

import java.util.Random;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Boss
{
    private int hp;
    private BossTemplate template;
    private int entityId;
    
    Boss(BossTemplate bossTemplate)
    {
        this.template = bossTemplate;
        this.hp = bossTemplate.maxHP;
    }
    
    public int takeDamage(int damage)
    {
        return this.hp = this.hp - damage; 
    }

    public String getName()
    {
        return this.template.name;
    }

    public int getMaxHP()
    {
        return this.template.maxHP;
    }

    void setEntity(Entity ent)
    {
        this.entityId = ent.getEntityId();
    }

    public void activateRandomSkill(EntityDamageByEntityEvent e, Entity eAttacker)
    {
        Random random = new Random();
        
        for(BossSkillInstance bsi : this.template.skills)
        {
            if(random.nextInt(100) < bsi.chance)
            {
                if(bsi.bossSkill.activateSkill(this, e, eAttacker, hp))
                {
                    if(eAttacker instanceof Player)
                    {
                        ((Player) eAttacker).sendMessage(this.template.name+" used "+bsi.bossSkill.name+"!");
                    }
                    
                    break;
                }
            }
        }
    }

    EntityType getEntityType()
    {
        return this.template.entityType;
    }
}
