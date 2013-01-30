package com.ne0nx3r0.rareitemhunter.bosses;

import java.util.HashMap;
import java.util.Map;

public class Boss
{
    private String name;
    private int hp;
    private int damage;
    private Map<BossSkill,Integer> skills;
    private int maxHP;
    
    public Boss(String name, int hp, int damage)
    {
        this.name = name;
        this.hp = hp;
        this.maxHP = hp;
        this.damage = damage;
        this.skills = new HashMap<BossSkill,Integer>();
    }
    
    public void addSkill(BossSkill bs,int level)
    {
        this.skills.put(bs,level);
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
}
