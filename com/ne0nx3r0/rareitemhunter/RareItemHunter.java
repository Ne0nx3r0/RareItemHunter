package com.ne0nx3r0.rareitemhunter;

import com.ne0nx3r0.rareitemhunter.bosses.BossManager;
import com.ne0nx3r0.rareitemhunter.commands.RareItemHunterCommandExecutor;
import com.ne0nx3r0.rareitemhunter.listeners.*;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyCostTypes;
import com.ne0nx3r0.rareitemhunter.property.PropertyManager;
import com.ne0nx3r0.rareitemhunter.recipe.RecipeManager;
import de.kumpelblase2.remoteentities.EntityManager;
import de.kumpelblase2.remoteentities.RemoteEntities;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.logging.Level;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class RareItemHunter extends JavaPlugin
{
    public EntityManager entityManager;
    public BossManager bossManager;
    public RecipeManager recipeManager;
    
    public boolean NIGHT_CRAFTING_ONLY = true;
    
    public static RareItemHunter self;
    
    public PropertyManager propertyManager;

    public ItemPropertyCostTypes COST_TYPE;
    public int COST_MULTIPLIER;
    public int COST_LEVEL_INCREMENT;
    public final String COMPONENT_STRING = ChatColor.DARK_PURPLE+"RareItem Component";
    public final String RAREITEM_HEADER_STRING = ChatColor.DARK_PURPLE+"RareItem";
    
    public Economy economy;
    
    @Override
    public void onEnable()
    {
        RareItemHunter.self = this;
        
        loadConfig(false);
        
        loadManagers();
        
        getServer().getPluginManager().registerEvents(new RareItemHunterEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new RareItemHunterPlayerListener(this), this);
        
        getCommand("ri").setExecutor(new RareItemHunterCommandExecutor(this));
    }
    
    public void reload()
    {
        getServer().getScheduler().cancelTasks(this);
        
        loadConfig(true);
        
        loadManagers();
    }
    
    private void loadConfig(boolean reloadConfig)
    {
        getDataFolder().mkdirs();
        
        File configFile = new File(getDataFolder(),"config.yml");
        
        if(!configFile.exists())
        {
            copy(getResource("config.yml"), configFile);
        }
        
        if(reloadConfig)
        {
            this.reloadConfig();
        }

        if(getConfig().getString("costType").equalsIgnoreCase("food"))
        {
            COST_TYPE = ItemPropertyCostTypes.FOOD;
        }
        else if(getConfig().getString("costType").equalsIgnoreCase("xp"))
        {
            COST_TYPE = ItemPropertyCostTypes.XP;
        }
        else if(getConfig().getString("costType").equalsIgnoreCase("money"))
        {
            RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
            
            if(economyProvider != null)
            {
                economy = economyProvider.getProvider();
            }

            this.getLogger().log(Level.SEVERE,"You specified money as your cost type, however you don't have Vault! Disabling...");
            
            this.getPluginLoader().disablePlugin(this);
            
            COST_TYPE = ItemPropertyCostTypes.MONEY;
        }
        
        COST_MULTIPLIER = getConfig().getInt("costMultiplier");
        
        COST_LEVEL_INCREMENT = getConfig().getInt("costLevelIncrement");
    }

    private void loadManagers()
    {
        this.entityManager = RemoteEntities.createManager(this);
        
        this.propertyManager = new PropertyManager(this);
        
        this.bossManager = new BossManager(this);

        this.recipeManager = new RecipeManager(this);
    }

// Public helper methods
    
    public void copy(InputStream in, File file)
    {
        try
        {
            OutputStream out = new FileOutputStream(file);
            byte[] buf = new byte[1024];
            int len;
            while((len=in.read(buf))>0)
            {
                out.write(buf,0,len);
            }
            out.close();
            in.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
