package com.ne0nx3r0.rareitemhunter.bosses;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class Boss
{
    private String name;
    private int entityId;
    private int hp;
    private int damage;
    private Map<BossSkill,Integer> skills;
    private Map<BossSkill,Integer> skillChances;
    private int maxHP;
    
    public Boss(String name, int hp, int damage)
    {
        this.name = name;
        this.hp = hp;
        this.maxHP = hp;
        this.damage = damage;
        this.skills = new HashMap<BossSkill,Integer>();
    }
    
    public void addSkill(BossSkill bs,int level,int chance)
    {
        this.skills.put(bs,level);
        this.skillChances.put(bs, chance);
    }

    public int takeDamage(int damage)
    {
        return this.hp = this.hp - damage; 
    }

    public String getName()
    {
        return this.name;
    }

    public int getMaxHP()
    {
        return this.maxHP;
    }

    void setEntity(Entity ent)
    {
        this.entityId = ent.getEntityId();
    }

    public void activateRandomSkill(EntityDamageByEntityEvent e, Entity eAttacker)
    {
        Random random = new Random();
        
        for(BossSkill bossSkill : this.skillChances.keySet())
        {
            if(random.nextInt(100) < this.skillChances.get(bossSkill))
            {
                if(bossSkill.activateSkill(this, e, eAttacker, this.skills.get(bossSkill)))
                {                    
                    if(eAttacker instanceof Player)
                    {
                        ((Player) eAttacker).sendMessage(this.name+" used "+bossSkill.getName()+"!");
                    }
                    
                    break;
                }
            }
        }
    }
}
