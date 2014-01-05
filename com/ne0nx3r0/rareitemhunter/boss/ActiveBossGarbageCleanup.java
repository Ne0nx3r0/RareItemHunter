package com.ne0nx3r0.rareitemhunter.boss;

import java.util.Iterator;
import java.util.Map;
import org.bukkit.entity.Entity;

class ActiveBossGarbageCleanup implements Runnable
{
    private final BossManager bm;

    public ActiveBossGarbageCleanup(BossManager bm)
    {
        this.bm = bm;
    }

    @Override
    public void run()
    {
        Iterator<Map.Entry<Integer,Boss>> iter = bm.activeBosses.entrySet().iterator();
        
        while(iter.hasNext())
        {
            Entity eTest = iter.next().getValue().entity;
            
            if(eTest.isDead() || !eTest.isValid())
            {
                iter.remove();
            }
        }
    }
}
