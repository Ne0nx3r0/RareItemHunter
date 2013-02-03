package com.ne0nx3r0.rareitemhunter.commands;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class RareItemHunterCommandExecutor implements CommandExecutor
{
    private final RareItemHunter plugin;

    public RareItemHunterCommandExecutor(RareItemHunter plugin)
    {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender cs, Command cmd, String alias, String[] args)
    {
        if(args.length == 0)
        {        
            cs.sendMessage(ChatColor.DARK_GREEN+"------  RareItemHunter  ------");
        
            if(cs.hasPermission("rareitemhunter.admin"))
            {
                cs.sendMessage("- /ri spawn - Boss commands");
                cs.sendMessage("- /ri compass - give yourself or others a "+plugin.recipeManager.getCompass().getItemMeta().getDisplayName());
            }
        }
        else if(args[0].equalsIgnoreCase("spawn") 
        && this.hasCommandPermission(cs,"rareitemhunter.admin","spawn commands"))
        {
            return this._spawn(cs,args);
        }
        else if(args[0].equalsIgnoreCase("compass") 
        && this.hasCommandPermission(cs,"rareitemhunter.admin","compass command"))
        {
            return this._compass(cs,args);
        }
        
        return false;
    }

    private boolean hasCommandPermission(CommandSender cs, String sPerm,String sAction)
    {
        if(cs.hasPermission(sPerm))
        {
            return true;
        }
        
        cs.sendMessage(ChatColor.RED+"You do not have permission to use "+ChatColor.WHITE+sAction+ChatColor.RED+".");
        cs.sendMessage(ChatColor.RED+"Permission node: "+ChatColor.WHITE+sPerm);
        
        return false;
    }
    
    private boolean sentFromConsole(CommandSender cs)
    {
        if(cs instanceof Player)
        { 
            return false;
        }
                   
        cs.sendMessage(ChatColor.RED+"This command cannot run from the console.");
        
        return true;
    }

    public String getHeader(String sHeader)
    {
        return ChatColor.DARK_GREEN + "-----  " + ChatColor.WHITE + sHeader + ChatColor.DARK_GREEN + " -----";
    }
    
    private boolean _spawn(CommandSender cs, String[] args)
    {
        if(args.length == 1)
        {
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri spawn  ------");
            cs.sendMessage("/ri spawn add  - add a spawn point");
            cs.sendMessage("/ri spawn del  - delete a spawn point");
            cs.sendMessage("/ri spawn list - List spawn points");
            cs.sendMessage("/ri spawn boss - Spawn a boss");
            cs.sendMessage("/ri spawn egg  - Spawn a boss egg");
        }
        else if(args[1].equalsIgnoreCase("add"))
        {
            return _spawn_add(cs, args);
        }
        else if(args[1].equalsIgnoreCase("del"))
        {
            return _spawn_del(cs, args);
        }
        else if(args[1].equalsIgnoreCase("list"))
        {
            return _spawn_list(cs, args);
        }
        else if(args[1].equalsIgnoreCase("boss"))
        {
            return _spawn_boss(cs, args);
        }
        else if(args[1].equalsIgnoreCase("egg"))
        {
            return _spawn_egg(cs, args);
        }
        
        return false;
    }
    
    private boolean _spawn_add(CommandSender cs, String[] args)
    {       
        if(sentFromConsole(cs))
        {
            return true;
        }
        
        if(args.length < 4 || args[2].equalsIgnoreCase("?"))
        {
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri spawn add <name> <radius> ------");
            cs.sendMessage("Creates a spawn point monsters can spawn from. Radius determines how far from the spawn point a legendary boss egg can appear.");
            cs.sendMessage("");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri spawn add somePoint 50");
            
            return true;
        }
        else
        {
            String sPointName = args[2];
            String sRadius = args[3];
            
            int iRadius = 0;
            
            try
            {
                iRadius = Integer.parseInt(sRadius);
            }
            catch(Exception e)
            {
                cs.sendMessage(ChatColor.RED+sRadius+" is not a valid number!");
                
                return true;
            }

            if(!plugin.bossManager.isSpawnPoint(sPointName))
            {
                plugin.bossManager.addSpawnPoint(sPointName,((Player) cs).getLocation(),iRadius);
                
                cs.sendMessage(ChatColor.DARK_GREEN+"-------------- RareItemHunter ----------------");
                cs.sendMessage("Added "+sPointName+" at your location with a radius of "+iRadius+" blocks!");
            }
            else
            {
                cs.sendMessage(ChatColor.RED+sPointName+" already exists!");
            }
        }
        
        return false;
    }


    private boolean _spawn_del(CommandSender cs, String[] args)
    {
        if(args.length < 3 || args[2].equalsIgnoreCase("?"))
        {
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri spawn del <name>  ------");
            cs.sendMessage("Deletes a spawn point by name.");
            cs.sendMessage("");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri spawn del somePoint");
            
            return true;
        }
        else
        {
            String sPointName = args[2];
            
            if(plugin.bossManager.isSpawnPoint(sPointName))
            {
                plugin.bossManager.delSpawnPoint(sPointName);
                
                return true;
            }
            else
            {
                cs.sendMessage(ChatColor.RED+sPointName+" is not a valid spawn point!");
            }
        }
        
        return true;
    }

    private boolean _spawn_list(CommandSender cs, String[] args)
    {
        cs.sendMessage(ChatColor.DARK_GREEN+"------  Boss Spawn Points  ------");
        
        for(String spawnName : plugin.bossManager.getSpawnPoints())
        {
            cs.sendMessage(spawnName);
        }
        
        return true;
    }    

    private boolean _spawn_boss(CommandSender cs, String[] args)
    {
        if(args.length < 4 || args[2].equalsIgnoreCase("?"))
        {
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri spawn boss <bossName> <pointName> | here  ------");
            cs.sendMessage("Spawns a boss at a spawn point, or at your current location.");
            cs.sendMessage("");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri spawn boss boss1 somePoint");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri spawn boss boss1 here");
            
            return true;
        }
        else
        {
            String sBossName = args[2];      
            String sPointName = args[3];

            if(!plugin.bossManager.isValidLocation(sPointName) && !sPointName.equalsIgnoreCase("here"))
            {
                cs.sendMessage(ChatColor.RED+"Invalid spawn point name!");
            }
            
            if(!plugin.bossManager.isValidBossName(sBossName))
            {
                cs.sendMessage(ChatColor.RED+"Invalid boss name");
                
                return true;
            }
            
            if(sPointName.equalsIgnoreCase("here"))
            {
                if(cs instanceof Player)
                {
                    plugin.bossManager.spawnBoss(sBossName,((Player) cs).getLocation());
                            
                    cs.sendMessage("Spawned a "+sBossName+" at your location!");
                }
                else
                {
                    cs.sendMessage("You cannot use 'here' from the console.");
                }
            }
            else
            {
                plugin.bossManager.spawnBoss(sBossName, sPointName);
                    
                cs.sendMessage("Spawned a "+sBossName+" at "+sPointName);
            }
                
            return true;
        }
    }

    private boolean _spawn_egg(CommandSender cs, String[] args)
    {
        if(args.length < 4 || args[2].equalsIgnoreCase("?"))
        {
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri spawn egg <bossName> <pointName> | here  ------");
            cs.sendMessage("Spawns a boss egg at a spawn point, or at your current location.");
            cs.sendMessage("");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri spawn egg boss1 somePoint");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri spawn egg boss1 here");
            
            return true;
        }
        else
        {
            String sBossName = args[2];      
            String sPointName = args[3];

            if(!plugin.bossManager.isValidLocation(sPointName) && !sPointName.equalsIgnoreCase("here"))
            {
                cs.sendMessage(ChatColor.RED+"Invalid spawn point name!");
            }
            
            if(!plugin.bossManager.isValidBossName(sBossName))
            {
                cs.sendMessage(ChatColor.RED+"Invalid boss name");
            }
            
            if(sPointName.equalsIgnoreCase("here"))
            {
                if(cs instanceof Player)
                {
                    plugin.bossManager.spawnBossEgg(sBossName,((Player) cs).getLocation().getBlock());
                            
                    cs.sendMessage("Spawned a "+sBossName+" egg at your location!");
                }
                else
                {
                    cs.sendMessage("You cannot use 'here' from the console.");
                }
            }
            else
            {
                plugin.bossManager.spawnBossEgg(sBossName, sPointName);
                    
                cs.sendMessage("Spawned a "+sBossName+" egg at "+sPointName);
            }
                
            return true;
        }
    }

    private boolean _compass(CommandSender cs, String[] args)
    {        
        if(args.length < 2)
        {
            if(this.sentFromConsole(cs))
            {
                cs.sendMessage("You can use /ri compass <player>");
                
                return true;
            }
            
            Player player = (Player) cs;
            
            player.getWorld().dropItemNaturally(player.getLocation(), plugin.recipeManager.getCompass());
            
            player.sendMessage("Giving you a "+plugin.recipeManager.getCompass().getItemMeta().getDisplayName()+"!");
        }
        else if(args[1].equalsIgnoreCase("?"))
        {
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri compass <player>  ------");
            cs.sendMessage("Gives you or a specified player a compass");
            cs.sendMessage("");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri compass");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri compass <player>");
        }
        else if(plugin.getServer().getPlayer(args[1]) != null)
        {
            Player player = plugin.getServer().getPlayer(args[1]);

            player.getWorld().dropItemNaturally(player.getLocation(), plugin.recipeManager.getCompass());
            
            String sCompassName = plugin.recipeManager.getCompass().getItemMeta().getDisplayName();
            
            cs.sendMessage("Giving "+player.getName()+" a "+sCompassName+"!");
            player.sendMessage("You just got a "+sCompassName+"!");
        }
        else
        {
            cs.sendMessage(ChatColor.RED+args[1]+" is not a valid player!");
        }
        
        return true;
    }
}
