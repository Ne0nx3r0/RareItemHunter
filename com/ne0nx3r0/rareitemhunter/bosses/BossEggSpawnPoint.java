package com.ne0nx3r0.rareitemhunter.bosses;

import org.bukkit.Location;

public class BossEggSpawnPoint
{
    String name;
    Location location;
    int radius;
    
    public BossEggSpawnPoint(String name,Location location,int radius)
    {
        this.name = name;
        this.location = location;
        this.radius = radius;
    }

    public Location getLocation()
    {
        return this.location;
    }
    
    public String getName()
    {
        return this.name;
    }
}
