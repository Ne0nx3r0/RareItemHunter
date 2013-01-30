package com.ne0nx3r0.rareitemhunter.commands;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RareItemHunterCommandExecutor implements CommandExecutor {
    private final RareItemHunter plugin;

    public RareItemHunterCommandExecutor(RareItemHunter plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args)
    {
        if(cs instanceof Player && args.length > 0)
        {
            Player p = (Player) cs;
            
            plugin.bossManager.spawnBossEgg(args[0],p.getLocation());
        }
        
        return true;
    }
    
}
