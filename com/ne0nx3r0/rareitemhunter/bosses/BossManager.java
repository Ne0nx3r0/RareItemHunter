package com.ne0nx3r0.rareitemhunter.bosses;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import com.ne0nx3r0.rareitemhunter.bosses.skills.*;
import java.io.File;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import net.minecraft.server.v1_4_R1.EntityLiving;
import net.minecraft.server.v1_4_R1.EntitySkeleton;
import net.minecraft.server.v1_4_R1.Item;
import net.minecraft.server.v1_4_R1.PathfinderGoal;
import net.minecraft.server.v1_4_R1.PathfinderGoalSelector;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_4_R1.entity.CraftSkeleton;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BossManager
{
    private final RareItemHunter plugin;
    
    private Map<String,BossTemplate> bossTemplates;
    
    private Map<String,SpawnPoint> spawnPoints;
    private Map<Location,String> bossEggs;
    private Map<Integer,Boss> activeBosses;
    
    public BossManager(RareItemHunter plugin)
    {
        this.plugin = plugin;
        
        bossEggs = new HashMap<Location,String>();
        
        activeBosses = new HashMap<Integer,Boss>();
        
        spawnPoints = new HashMap<String,SpawnPoint>();
        
        List<BossSkill> availableBossSkills = new ArrayList<BossSkill>();
        
        availableBossSkills.add(new Burst());
        availableBossSkills.add(new GreaterBurst());
        availableBossSkills.add(new ShootArrow());
        availableBossSkills.add(new ShootFireball());
        availableBossSkills.add(new FakeWeb());
        availableBossSkills.add(new Blink());
        availableBossSkills.add(new JumpAttack());
        
        bossTemplates = new HashMap<String,BossTemplate>();

        File bossesFile = new File(plugin.getDataFolder(),"bosses.yml");

        if(!bossesFile.exists())
        {
            plugin.copy(plugin.getResource("bosses.yml"),bossesFile);
        }
        
        FileConfiguration bossesYml = YamlConfiguration.loadConfiguration(bossesFile);
        
        for(Iterator<String> it = bossesYml.getKeys(false).iterator(); it.hasNext();)
        {
            String sBossName = it.next();
            
            String sType = bossesYml.getString(sBossName+"."+"type");

            EntityType entityType = EntityType.fromName(sType);

            int hp = bossesYml.getInt(sBossName+"."+"hp");

            int attackPower = bossesYml.getInt(sBossName+"."+"attackPower");

            int essencesDropped = bossesYml.getInt(sBossName+"."+"essencesDropped");

            List<String> skillStrings = (List<String>) bossesYml.getList(sBossName+"."+"skills");

            BossTemplate bossTemplate = new BossTemplate(sBossName,entityType,hp,attackPower,essencesDropped);

            for(String skillString : skillStrings)
            {                    
                String skillName = skillString.substring(skillString.indexOf("chance ")+7,skillString.indexOf(" level")).toLowerCase();
                int chance = Integer.parseInt(skillString.substring(0,skillString.indexOf("%")));
                int level = Integer.parseInt(skillString.substring(skillString.lastIndexOf(" ")+1));

                for(BossSkill bossSkill : availableBossSkills)
                {
                    if(bossSkill.getName().equalsIgnoreCase(skillName))
                    {
                        bossTemplate.addSkill(bossSkill, level, chance);
                    }
                }
            }     
            
            this.bossTemplates.put(bossTemplate.name,bossTemplate);
        }
    }
    
    public boolean isBoss(Entity entity)
    {        
        if(this.activeBosses.containsKey(entity.getEntityId()))
        {
            return true;
        }
        
        return false;
    }

    public Boss getBoss(Entity entity)
    {
        return this.activeBosses.get(entity.getEntityId());
    }

    public boolean isBossEgg(Location eggLocation)
    {
        return this.bossEggs.containsKey(eggLocation);
    }

    public Boss hatchBoss(Location eggLocation)
    {
        String sBossName = bossEggs.get(eggLocation);
        
        eggLocation.getBlock().setType(Material.AIR);
        
        eggLocation.getBlock().getRelative(BlockFace.DOWN).setType(Material.AIR);

        Boss boss = this.spawnBoss(sBossName, eggLocation);
        
        bossEggs.remove(eggLocation);
        
        return boss;
    }

    public Boss spawnBoss(String sBossName, Location eggLocation)
    {
        Boss boss = new Boss(this.bossTemplates.get(sBossName));
        
        Entity ent = eggLocation.getWorld().spawnEntity(eggLocation, boss.getEntityType());

        boss.setEntity(ent);
        
        if(boss.getEntityType().equals(EntityType.SKELETON))
        {
            this.changeIntoNormal((Skeleton) ent, true);
        }
        if(boss.getEntityType().equals(EntityType.WITHER_SKULL))
        {
            this.changeIntoWither((Skeleton) ent);
        }
        
        ((LivingEntity) ent).addPotionEffect(new PotionEffect(PotionEffectType.SPEED,9999999,5));
        
        activeBosses.put(ent.getEntityId(), boss);
        
        return boss;
    }

    public boolean hasSpawnPoints()
    {
        return !this.spawnPoints.isEmpty();
    }
    
    public Location spawnBossEgg(String bossName,String sSpawnPointName)
    {
        return spawnBossEgg(this.bossTemplates.get(bossName), sSpawnPointName);
    }  
    
    public boolean spawnBossEgg(String sBossName,Block block)
    { 
        return spawnBossEgg(this.bossTemplates.get(sBossName), block);
    }

    public Location spawnBossEgg(BossTemplate bossTemplate,String sSpawnPointName)
    {
        Random random = new Random();
        
        SpawnPoint spawnPoint = this.spawnPoints.get(sSpawnPointName);
        
        for(int i=0;i<10;i++)
        {
            Location lSpawnPoint = spawnPoint.location.clone().add(
                random.nextInt(spawnPoint.radius*2)-spawnPoint.radius,
                0,
                random.nextInt(spawnPoint.radius*2)-spawnPoint.radius);

            Block block = lSpawnPoint.getBlock();

            World world = lSpawnPoint.getWorld();
            int mapHeight = world.getMaxHeight();
            int x = lSpawnPoint.getBlockX();
            int y = lSpawnPoint.getBlockY();
            int z = lSpawnPoint.getBlockZ();

            for(int j=0;j<spawnPoint.radius && j<mapHeight;j++)
            {
                if(y+j+1 < mapHeight)
                {
                    Block up1 = world.getBlockAt(x,y+j,z);
                    Block up2 = world.getBlockAt(x,y+j+1,z);
                    
                    if(up1 != null && up1.getType() != Material.AIR
                    && up2 != null && up2.getType() == Material.AIR
                    && !this.bossEggs.containsKey(up2.getLocation()))
                    {
                        if(this.spawnBossEgg(bossTemplate, up2))
                        {
                            return up2.getLocation();
                        }
                    }
                }

                if(y-j-1 > 0)
                {
                    Block down1 = world.getBlockAt(x,y-j,z);
                    Block down2 = world.getBlockAt(x,y-j-1,z);

                    if(down1 != null && down1.getType() != Material.AIR
                    && down2 != null && down2.getType() == Material.AIR
                    && !this.bossEggs.containsKey(down1.getLocation()))
                    {
                        if(this.spawnBossEgg(bossTemplate, down1))
                        {
                            return down1.getLocation();
                        }
                    }
                }
            }
        }
        
        return null;
    }
    
    public boolean spawnBossEgg(BossTemplate bossTemplate,Block block)
    { 
        if(!this.bossEggs.containsKey(block.getLocation()))
        {
            block.getRelative(BlockFace.DOWN).setType(Material.BEDROCK);

            block.setType(Material.DRAGON_EGG);

            this.bossEggs.put(block.getLocation(), bossTemplate.name);
            
            return true;
        }
        
        return false;
    }
    
    public Location spawnRandomBossEgg()
    {
        Random r = new Random();

        Object[] values = this.spawnPoints.keySet().toArray();
        String sRandomSpawnPointName = (String) values[r.nextInt(values.length)];

        Object[] bosses = this.bossTemplates.values().toArray();
        BossTemplate bossTemplate = (BossTemplate) bosses[r.nextInt(bosses.length)];

        Location lSpawned = spawnBossEgg(bossTemplate, sRandomSpawnPointName);

        if(lSpawned == null)
        {
            plugin.getServer().broadcast("-------------- RareItemHunter ----------------","rareitemhunter.admin");
            plugin.getServer().broadcast("Was unable to find a place to spawn a "+bossTemplate.name+" boss egg at spawn point "+sRandomSpawnPointName,"rareitemhunter.admin");
        }
        
        return lSpawned;
    }

    public void addSpawnPoint(String name, Location location, int radius)
    {
        this.spawnPoints.put(name, new SpawnPoint(name,location,radius));
    }

    public boolean isSpawnPoint(String name)
    {
        return this.spawnPoints.containsKey(name);
    }

    public void delSpawnPoint(String name)
    {
        this.spawnPoints.remove(name);
    }

    public Iterable<SpawnPoint> getSpawnPoints()
    {
        return this.spawnPoints.values();
    }

    public boolean isValidLocation(String sPointName)
    {
        return this.spawnPoints.containsKey(sPointName);
    }

    public boolean isValidBossName(String sBossName)
    {
        return this.bossTemplates.containsKey(sBossName);
    }

    public void spawnBoss(String sBossName, String sPointName)
    {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    void removeBossEgg(Location lSpawnedEgg)
    {
        if(this.bossEggs.containsKey(lSpawnedEgg))
        {
            Block block = lSpawnedEgg.getBlock();
            Block blockNeneath = block.getRelative(BlockFace.DOWN);

            if(block.getType() == Material.DRAGON_EGG)
            {
                block.setType(Material.AIR);
            }
            if(blockNeneath.getType() == Material.BEDROCK)
            { 
                blockNeneath.setType(Material.AIR);
            }

            this.bossEggs.remove(lSpawnedEgg);
        }
    }

    public Location getNearestBossEggLocation(Location l)
    {
        Location lClosest = null;
        
        World lWorld = l.getWorld();
        
        double dClosest = -1;
        
        double dCurrent;
        
        for(Location lEgg : bossEggs.keySet())
        {
            if(lEgg.getWorld().equals(lWorld))
            {
                dCurrent = l.distanceSquared(lEgg);
                
                if(dClosest == -1)
                {
                    dClosest = dCurrent;
                    
                    lClosest = lEgg;
                }
                else if(dCurrent < dClosest)
                {
                    dClosest = dCurrent;
                    
                    lClosest = lEgg;
                }
            }
        }
        
        return lClosest;
    }
    
// Misc helper methods
     public void changeIntoNormal(Skeleton skeleton, boolean giveRandomEnchantments)
     {
        EntitySkeleton ent = ((CraftSkeleton)skeleton).getHandle();
        try
        {
            ent.setSkeletonType(0);
            Method be = EntitySkeleton.class.getDeclaredMethod("bE");
            be.setAccessible(true);
            be.invoke(ent);
            if (giveRandomEnchantments)
            {
                Method bf = EntityLiving.class.getDeclaredMethod("bF");
                bf.setAccessible(true);
                bf.invoke(ent);
            }
            Field selector = EntityLiving.class.getDeclaredField("goalSelector");
            selector.setAccessible(true);
            Field d = EntitySkeleton.class.getDeclaredField("d");
            d.setAccessible(true);
            PathfinderGoalSelector goals = (PathfinderGoalSelector) selector.get(ent);
            goals.a(4, (PathfinderGoal) d.get(ent));
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }    
    
     public void changeIntoWither(Skeleton skeleton){
        EntitySkeleton ent = ((CraftSkeleton)skeleton).getHandle();
        try {
            ent.setSkeletonType(1);
            Field selector = EntityLiving.class.getDeclaredField("goalSelector");
            selector.setAccessible(true);
            Field e = EntitySkeleton.class.getDeclaredField("e");
            e.setAccessible(true);
            PathfinderGoalSelector goals = (PathfinderGoalSelector) selector.get(ent);
            goals.a(4, (PathfinderGoal) e.get(ent));
            ent.setEquipment(0, new net.minecraft.server.v1_4_R1.ItemStack(Item.STONE_SWORD));
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
