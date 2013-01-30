package com.ne0nx3r0.rareitemhunter;

import com.ne0nx3r0.rareitemhunter.bosses.BossManager;
import com.ne0nx3r0.rareitemhunter.listeners.*;
import org.bukkit.plugin.java.JavaPlugin;

public class RareItemHunter extends JavaPlugin
{
    public BossManager bossManager;
    
    @Override
    public void onEnable()
    {
        this.bossManager = new BossManager(this);
        
        getServer().getPluginManager().registerEvents(new RareItemHunterEntityListener(this), this);
        getServer().getPluginManager().registerEvents(new RareItemHunterPlayerListener(this), this);
    }
    
    @Override
    public void onDisable()
    {
        
    }
}
