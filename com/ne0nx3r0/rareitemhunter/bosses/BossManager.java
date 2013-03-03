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
import java.util.logging.Level;
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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Skeleton;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class BossManager
{
    private final RareItemHunter plugin;
    
    private Map<String,BossTemplate> bossTemplates;
    
    private Map<String,BossEggSpawnPoint> spawnPoints;
    private Map<Location,String> bossEggs;
    private Map<Integer,Boss> activeBosses;
    
    public BossManager(RareItemHunter plugin)
    {
        this.plugin = plugin;
        
        bossEggs = new HashMap<Location,String>();
        
        activeBosses = new HashMap<Integer,Boss>();
        
        spawnPoints = new HashMap<String,BossEggSpawnPoint>();
        
        List<BossSkill> availableBossSkills = new ArrayList<BossSkill>();
        
        availableBossSkills.add(new Burst());
        availableBossSkills.add(new GreaterBurst());
        availableBossSkills.add(new ShootArrow());
        availableBossSkills.add(new ShootFireball());
        availableBossSkills.add(new FakeWeb());
        availableBossSkills.add(new Blink());
        availableBossSkills.add(new JumpAttack());
        availableBossSkills.add(new SpawnZombiePig());
        availableBossSkills.add(new SpawnSkeleton());
        availableBossSkills.add(new SpawnZombie());
        availableBossSkills.add(new SpawnCreeper());
        
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

            int hp = bossesYml.getInt(sBossName+".hp");

            int attackPower = bossesYml.getInt(sBossName+".attackPower");

            int essencesDropped = bossesYml.getInt(sBossName+".essencesDropped");

            List<ItemStack> equipment = new ArrayList<ItemStack>();
      
            ItemStack weapon = null; 
// Add equipment if it has any
            
            if(bossesYml.isSet(sBossName+".equipment"))
            {
                //Kludgey :/    
                List<Material> mWeaponTypes = new ArrayList<Material>();
                    mWeaponTypes.add(Material.DIAMOND_SWORD);
                    mWeaponTypes.add(Material.WOOD_SWORD);
                    mWeaponTypes.add(Material.IRON_SWORD);
                    mWeaponTypes.add(Material.GOLD_SWORD);
                    mWeaponTypes.add(Material.DIAMOND_SPADE);
                    mWeaponTypes.add(Material.WOOD_SPADE);
                    mWeaponTypes.add(Material.IRON_SPADE);
                    mWeaponTypes.add(Material.GOLD_SPADE);
                    mWeaponTypes.add(Material.DIAMOND_HOE);
                    mWeaponTypes.add(Material.WOOD_HOE);
                    mWeaponTypes.add(Material.IRON_HOE);
                    mWeaponTypes.add(Material.GOLD_SWORD);
                    mWeaponTypes.add(Material.DIAMOND_PICKAXE);
                    mWeaponTypes.add(Material.WOOD_PICKAXE);
                    mWeaponTypes.add(Material.IRON_PICKAXE);
                    mWeaponTypes.add(Material.GOLD_PICKAXE);
                    mWeaponTypes.add(Material.DIAMOND_AXE);
                    mWeaponTypes.add(Material.WOOD_AXE);
                    mWeaponTypes.add(Material.IRON_AXE);
                    mWeaponTypes.add(Material.GOLD_AXE);
                    mWeaponTypes.add(Material.DIAMOND);

                List<String> bossEquipmentStrings = (List<String>) bossesYml.getList(sBossName+".equipment");
                
                for(String sItem : bossEquipmentStrings)
                {
                    String[] equipValues = sItem.split(" ");
                    
                    Material equipMaterial = Material.matchMaterial(equipValues[0]);
                    
                    if(equipMaterial != null)
                    {
                        ItemStack is = new ItemStack(equipMaterial);
                        
                        if(equipValues.length > 1)
                        {
                            for(String sEnchantment : equipValues[1].split(","))
                            {
                                String[] enchantmentPair = sEnchantment.split(":");
                                
                                Enchantment en = Enchantment.getByName(enchantmentPair[0]);
                                int level = 0;
                                
                                try
                                {
                                    level = Integer.parseInt(enchantmentPair[1]);
                                }
                                catch(Exception e)
                                {
                                    plugin.getLogger().log(Level.WARNING,"'"+enchantmentPair[1]+"' is not a valid enchantment level on boss '"+sBossName+"'. Skipping.");
                                    
                                    continue;
                                }
                                
                                if(en == null)
                                {
                                    plugin.getLogger().log(Level.WARNING,"'"+enchantmentPair[0]+"' is not a valid enchantment name on boss '"+sBossName+"'. Skipping.");
                                    
                                    continue;
                                }
                                
                                is.addEnchantment(en, level);
                            }
                        }
                        
                        if(mWeaponTypes.contains(is.getType()))
                        {
                            weapon = is;
                        }
                        else if(equipment.size() < 4)
                        {
                            equipment.add(is);
                        }
                        else
                        {
                            plugin.getLogger().log(Level.WARNING,sBossName+" has too many equipments, skipping '"+sItem+"'");
                        }
                    }
                    else
                    {
                        plugin.getLogger().log(Level.WARNING,"'"+equipValues[0]+"' is not a valid material on boss '"+sBossName+"'. Skipping.");
                    }
                }
            }

// Create the template
            BossTemplate bossTemplate = new BossTemplate(sBossName,entityType,hp,attackPower,essencesDropped,equipment,weapon);
            
// Add any skills
            if(bossesYml.isSet(sBossName+".skills"))
            {
                List<String> skillStrings = (List<String>) bossesYml.getList(sBossName+".skills");

                for(String skillString : skillStrings)
                {           
                    String[] skillValues = skillString.split(" ");

                    String skillName = skillValues[2].replace("_", " ");
                    int chance = Integer.parseInt(skillValues[0].substring(0,skillString.indexOf("%")));
                    int level = Integer.parseInt(skillValues[4]);

                    for(BossSkill bossSkill : availableBossSkills)
                    {
                        if(bossSkill.getName().equalsIgnoreCase(skillName))
                        {
                            bossTemplate.addSkill(bossSkill, level, chance);
                        }
                    }
                }  
            }
            
// Add any events
            if(bossesYml.isSet(sBossName+".events"))
            {
                List<String> eventStrings = (List<String>) bossesYml.getList(sBossName+".events");

                for(String eventString : eventStrings)
                {           
                    String[] eventValues = eventString.split(" ");

                    String sEventType = eventValues[0];

                    BossEventType eventType = null;
                    
                    for(BossEventType bet : BossEventType.values())
                    {
                        if(bet.name().equalsIgnoreCase(sEventType))
                        {
                            eventType = BossEventType.valueOf(sEventType);
                        }
                    }

                    if(eventType == null)
                    {
                        plugin.getLogger().log(Level.WARNING,"'"+sEventType+"' is not a valid event type on boss '"+sBossName+"'. Skipping.");

                        continue;
                    }

                    int iEventValue = -1;

                    try
                    {
                        iEventValue = Integer.parseInt(eventValues[1]);
                    }
                    catch(Exception e)
                    {
                        plugin.getLogger().log(Level.WARNING,"'"+eventValues[1]+"' is not a valid event value on boss '"+sBossName+"'. Skipping.");

                        continue;
                    }                

                    String skillName = eventValues[2].replace("_", " ");
                    int level = Integer.parseInt(eventValues[4]);

                    for(BossSkill bossSkill : availableBossSkills)
                    {
                        if(bossSkill.getName().equalsIgnoreCase(skillName))
                        {
                            bossTemplate.addEvent(new BossEvent(eventType,iEventValue,bossSkill,level));
                        }
                    }
                }  
            }
            
// Save the template
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
        
        if(boss.getEntityType().equals(EntityType.SKELETON))
        {
            this.changeIntoNormal((Skeleton) ent, true);
        }
        if(boss.getEntityType().equals(EntityType.WITHER_SKULL))
        {
            this.changeIntoWither((Skeleton) ent);
        }
        
        LivingEntity lent = (LivingEntity) ent;
        
        lent.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,9999999,5));

        EntityEquipment lequips = lent.getEquipment();
            
        if(boss.template.equipment != null)
        {                    
            lequips.setArmorContents(boss.template.equipment.toArray(new ItemStack[4]));

            lequips.setBootsDropChance(0f);
            lequips.setLeggingsDropChance(0f);
            lequips.setChestplateDropChance(0f);
            lequips.setHelmetDropChance(0f);
        }
            
        if(boss.template.weapon != null)
        {
            lequips.setItemInHand(boss.template.weapon);

            lequips.setItemInHandDropChance(0f);
        }
        
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
        
        BossEggSpawnPoint spawnPoint = this.spawnPoints.get(sSpawnPointName);
        
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
        this.spawnPoints.put(name, new BossEggSpawnPoint(name,location,radius));
    }

    public boolean isSpawnPoint(String name)
    {
        return this.spawnPoints.containsKey(name);
    }

    public void delSpawnPoint(String name)
    {
        this.spawnPoints.remove(name);
    }

    public Iterable<BossEggSpawnPoint> getSpawnPoints()
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
