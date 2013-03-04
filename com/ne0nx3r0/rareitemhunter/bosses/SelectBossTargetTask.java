package com.ne0nx3r0.rareitemhunter.bosses;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import org.bukkit.entity.Entity;

public class SelectBossTargetTask implements Runnable 
{
    private final RareItemHunter plugin;
    private final BossManager bossManager;
    private final double SIGHT_DISTANCE;
    
    public SelectBossTargetTask(RareItemHunter plugin,BossManager bossManager)
    {
        this.plugin = plugin;
        this.bossManager = bossManager;
        
        this.SIGHT_DISTANCE = plugin.getConfig().getDouble("bossAttackRange",50);
    }

    @Override
    public void run() 
    {
        if(!bossManager.activeBosses.isEmpty())
        {
            for(Boss boss : bossManager.activeBosses.values())
            {
                Entity eBoss = boss.getEntity();

                if(!eBoss.isValid() || eBoss.isDead())
                {
                    bossManager.activeBosses.remove(eBoss.getEntityId());
                }
                /* does not work, at all
                else if(eBoss instanceof Creature)
                {
                   Creature cBoss = (Creature) eBoss;

                   if(!(cBoss.getTarget() instanceof Player))
                   {
                        Location l = eBoss.getLocation();
                        double maxDistance = Math.pow(SIGHT_DISTANCE, 2);
                        double curDist;

                        Player pClosest = null;

                        for(Player p : eBoss.getWorld().getPlayers())
                        {
                            curDist = l.distanceSquared(p.getLocation());

                            System.out.println(p.getName() + " distance " + curDist);

                            if(curDist < maxDistance)
                            {
                                maxDistance = curDist;
                                pClosest = p;
                            }
                        }

                        if(pClosest != null)
                        {
                            cBoss.setTarget(pClosest);
                        }
                   }
                }*/
            }
        }
    }
}
