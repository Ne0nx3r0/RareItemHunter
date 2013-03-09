package com.ne0nx3r0.rareitemhunter.boss;

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
        for(Boss bTest : bm.activeBosses.values())
        {
            if(bTest != null)
            {
                if(bTest.entity.isDead() || !bTest.entity.isValid())
                {
                    bm.activeBosses.remove(bTest.entity.getEntityId());
                }
            }
        }
    }
}
