package com.ne0nx3r0.rareitemhunter.bosses;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import com.ne0nx3r0.rareitemhunter.bosses.skills.*;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;

public class BossManager
{
    private Map<String,BossSkill> availableBossSkills;
    private Map<Location,Boss> inactiveBosses;
    private Map<Integer,Boss> activeBosses;
    private final RareItemHunter plugin;
    
    public BossManager(RareItemHunter plugin)
    {
        this.plugin = plugin;
        
        this.inactiveBosses = new HashMap<Location,Boss>();
        this.activeBosses = new HashMap<Integer,Boss>();
        
        this.availableBossSkills = new HashMap<String,BossSkill>();
        
        this.addBossSkill(new Burst());
        this.addBossSkill(new GreaterBurst());
        this.addBossSkill(new ShootArrow());
        this.addBossSkill(new ShootFireball());
        this.addBossSkill(new FakeWeb());
        
        File bossesFile = new File(plugin.getDataFolder(),"bosses.yml");

        if(!bossesFile.exists())
        {
            plugin.copy(plugin.getResource("bosses.yml"),bossesFile);
        }
    }

    private void addBossSkill(BossSkill bossSkill)
    {
        this.availableBossSkills.put(bossSkill.getName().toLowerCase(), bossSkill);
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

    public Boss getBoss(Location location)
    {
        return this.inactiveBosses.get(location);
    }

    public boolean spawnBossEgg(String bossName, Location location)
    {
        if(!this.inactiveBosses.containsKey(location))
        {
            File bossesFile = new File(plugin.getDataFolder(), "bosses.yml");
            
            if(!bossesFile.exists())
            {
                plugin.copy(plugin.getResource("bosses.yml"),bossesFile);
            }

            FileConfiguration bossesYml = YamlConfiguration.loadConfiguration(bossesFile);

            if(bossesYml.getKeys(false).contains(bossName))
            {
                //TODO: Add support for boss armors
                
                String sType = bossesYml.getString(bossName+"."+"type");
                
                EntityType entityType = EntityType.fromName(sType);
                
                int hp = bossesYml.getInt(bossName+"."+"hp");
                
                int attackPower = bossesYml.getInt(bossName+"."+"attackPower");
                
                int essencesDropped = bossesYml.getInt(bossName+"."+"essencesDropped");
                
                List<String> skillStrings = (List<String>) bossesYml.getList(bossName+"."+"skills");
                
                Boss boss = new Boss(bossName,entityType,hp,attackPower,essencesDropped);
                
                for(String skillString : skillStrings)
                {                    
                    String skillName = skillString.substring(skillString.indexOf("chance ")+7,skillString.indexOf(" level")).toLowerCase();
                    
                    if(this.availableBossSkills.containsKey(skillName))
                    {
                        int chance = Integer.parseInt(skillString.substring(0,skillString.indexOf("%")));
                        int level = Integer.parseInt(skillString.substring(skillString.lastIndexOf(" ")+1));

                        boss.addSkill(this.availableBossSkills.get(skillName), level, chance);
                    }
                }
            
                //TODO: Persist inactive bosses

                //TODO: auto-Kill-off active bosses when appropriate

                location.getBlock().setType(Material.DRAGON_EGG);

                this.inactiveBosses.put(location.getBlock().getLocation(), boss);
                
                return true;
            }
        }
        
        return false;
    }

    public boolean isBossEgg(Location eggLocation)
    {
        return this.inactiveBosses.containsKey(eggLocation);
    }

    public Boss hatchBoss(Location eggLocation)
    {
        Boss boss = inactiveBosses.get(eggLocation);
        
        eggLocation.getBlock().setType(Material.AIR);
        
        Entity ent = eggLocation.getWorld().spawnEntity(eggLocation, boss.getEntityType());
        
        boss.setEntity(ent);
        
        activeBosses.put(ent.getEntityId(), boss);
        
        inactiveBosses.remove(eggLocation);
        
        return boss;
    }
}
