
package com.ne0nx3r0.rareitemhunter.bosses;

public class BossEvent
{
    BossEventType type;
    int triggerValue;
    BossSkill skill;
    int level;
    
    public BossEvent(BossEventType type, int triggerValue, BossSkill skill, int level)
    {
        this.type = type;
        this.triggerValue = triggerValue;
        this.skill = skill;
        this.level = level;
    }
}
