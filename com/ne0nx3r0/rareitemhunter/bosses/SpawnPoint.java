package com.ne0nx3r0.rareitemhunter.bosses;

import org.bukkit.Location;

public class SpawnPoint
{
    Location location;
    int radius;
    
    public SpawnPoint(Location location,int radius)
    {
        this.location = location;
        this.radius = radius;
    }
}
