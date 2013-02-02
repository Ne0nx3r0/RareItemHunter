package com.ne0nx3r0.rareitemhunter.bosses;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.entity.EntityType;

public class BossTemplate
{
    String name;
    int attackPower;
    List<BossSkillInstance> skills;
    int maxHP;
    EntityType entityType;
    int essencesDropped;
    
    public BossTemplate(String name,EntityType entityType,int maxHP,int attackPower,int essencesDropped)
    {
        this.name = name;
        this.attackPower = attackPower;
        this.maxHP = maxHP;
        this.entityType = entityType;
        this.essencesDropped = essencesDropped;
        
        this.skills = new ArrayList<BossSkillInstance>();
    }
    
    public void addSkill(BossSkill bs,int level,int chance)
    {
        this.skills.add(new BossSkillInstance(bs,level,chance));
    }
}
