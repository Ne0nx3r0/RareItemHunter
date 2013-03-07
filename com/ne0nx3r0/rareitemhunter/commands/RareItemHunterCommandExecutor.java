package com.ne0nx3r0.rareitemhunter.commands;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import com.ne0nx3r0.rareitemhunter.bosses.BossEggSpawnPoint;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
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
        
            cs.sendMessage("Here are the commands you have access to:");
            cs.sendMessage("");
            
            if(cs.hasPermission("rareitemhunter.admin.spawnpoint"))
            {
                cs.sendMessage("- /ri spawnpoint - Manage spawn points");
            }
            if(cs.hasPermission("rareitemhunter.admin.egg"))
            {
                cs.sendMessage("- /ri egg - Egg commands");
            }
            if(cs.hasPermission("rareitemhunter.admin.boss"))
            {
                cs.sendMessage("- /ri boss - Boss commands");
            }
            if(cs.hasPermission("rareitemhunter.admin.compass"))
            {
                cs.sendMessage("- /ri compass - Give you or another a "+plugin.recipeManager.getCompass().getItemMeta().getDisplayName());
            }  
            if(cs.hasPermission("rareitemhunter.admin.essence"))
            {
                cs.sendMessage("- /ri essence - Give you or another a "+plugin.recipeManager.getEssenceItem().getItemMeta().getDisplayName());
            }
                
            return true;
        }
        else if((args[0].equalsIgnoreCase("spawnpoint") || args[0].equalsIgnoreCase("sp")) 
        && this.hasCommandPermission(cs,"rareitemhunter.spawnpoint","spawn point commands"))
        {
            return this._spawnPoint(cs,args);
        }
        else if((args[0].equalsIgnoreCase("boss") || args[0].equalsIgnoreCase("b"))
        && this.hasCommandPermission(cs,"rareitemhunter.spawnpoint","spawn boss commands"))
        {
            return this._spawnBoss(cs,args);
        }
        else if((args[0].equalsIgnoreCase("egg") || args[0].equalsIgnoreCase("e")) 
        && this.hasCommandPermission(cs,"rareitemhunter.spawnpoint","spawn egg commands"))
        {
            return this._spawnEgg(cs,args);
        }
        else if((args[0].equalsIgnoreCase("compass") || args[0].equalsIgnoreCase("c"))
        && this.hasCommandPermission(cs,"rareitemhunter.admin.compass","compass command"))
        {
            return this._compass(cs,args);
        }
        else if((args[0].equalsIgnoreCase("essence") || args[0].equalsIgnoreCase("e"))
        && this.hasCommandPermission(cs,"rareitemhunter.admin.essence","essence command"))
        {
            return this._essence(cs,args);
        }
        else if(args[0].equalsIgnoreCase("reload") 
        && this.hasCommandPermission(cs,"rareitemhunter.admin.reload","reload command"))
        {
            return this._reload(cs,args);
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
    
    private boolean _spawnPoint(CommandSender cs, String[] args)
    {
        if(args.length == 1)
        {
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri spawnpoint  ------");
            cs.sendMessage("/ri spawnpoint add  - add a spawn point");
            cs.sendMessage("/ri spawnpoint del  - delete a spawn point");
            cs.sendMessage("/ri spawnpoint list - List spawn points");
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
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri spawnpoint add <name> <radius> ------");
            cs.sendMessage("Creates a spawn point monsters can spawn from. Radius determines how far from the spawn point a legendary boss egg can appear.");
            cs.sendMessage("");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri spawnpoint add somePoint 50");
            
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
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri spawnpoint del <name>  ------");
            cs.sendMessage("Deletes a spawn point by name.");
            cs.sendMessage("");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri spawnpoint del somePoint");
            
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
        
        for(BossEggSpawnPoint point : plugin.bossManager.getSpawnPoints())
        {
            Location l = point.getLocation();
            
            cs.sendMessage(point.getName()+" (z:"+l.getBlockX()+",y:"+l.getBlockY()+",z:"+l.getBlockZ()+",radius:"+point.getRadius()+")");
        }
        
        return true;
    }    
    
    private boolean _spawnEgg(CommandSender cs, String[] args)
    {

        if(args.length < 3 || args[1].equalsIgnoreCase("?"))
        {
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri egg <bossName> <pointName> | here  ------");
            cs.sendMessage("Spawns a boss egg at a spawn point, or at your current location.");
            cs.sendMessage("");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri egg boss1 somePoint");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri egg boss1 here");
            
            return true;
        }
        else
        {
            String sBossName = args[1];      
            String sPointName = args[2];

            if(!plugin.bossManager.isValidLocation(sPointName) && !sPointName.equalsIgnoreCase("here"))
            {
                cs.sendMessage(ChatColor.RED+"Invalid spawn point name!");
                
                return true;
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

    private boolean _spawnBoss(CommandSender cs, String[] args)
    {

        if(args.length < 3 || args[1].equalsIgnoreCase("?"))
        {
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri boss <bossName> <pointName> | here  ------");
            cs.sendMessage("Spawns a boss at a spawn point, or at your current location.");
            cs.sendMessage("");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri boss boss1 somePoint");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri boss boss1 here");
            
            return true;
        }
        else
        {
            String sBossName = args[1];      
            String sPointName = args[2];

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

    private boolean _reload(CommandSender cs, String[] args)
    {
        cs.sendMessage(ChatColor.GREEN+"Reloading RareItemHunter...");
        
        plugin.reload();
        
        cs.sendMessage(ChatColor.GREEN+"RareItemHunter Reloaded!");
        
        return true;
    }

    private boolean _essence(CommandSender cs, String[] args)
    {     
        if(args.length < 2)
        {
            if(this.sentFromConsole(cs))
            {
                cs.sendMessage("You can use /ri essence <player>");
                
                return true;
            }
            
            Player player = (Player) cs;
            
            player.getWorld().dropItemNaturally(player.getLocation(), plugin.recipeManager.getEssenceItem());
            
            player.sendMessage("Giving you a "+plugin.recipeManager.getEssenceItem().getItemMeta().getDisplayName()+"!");
        }
        else if(args[1].equalsIgnoreCase("?"))
        {
            cs.sendMessage(ChatColor.DARK_GREEN+"------  /ri essence <player>  ------");
            cs.sendMessage("Gives you or a specified player a essence");
            cs.sendMessage("");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri essence");
            cs.sendMessage(ChatColor.DARK_GREEN+"Example:"+ChatColor.WHITE+" /ri essence <player>");
        }
        else if(plugin.getServer().getPlayer(args[1]) != null)
        {
            Player player = plugin.getServer().getPlayer(args[1]);

            player.getWorld().dropItemNaturally(player.getLocation(), plugin.recipeManager.getEssenceItem());
            
            String sEssenceName = plugin.recipeManager.getEssenceItem().getItemMeta().getDisplayName();
            
            cs.sendMessage("Giving "+player.getName()+" a "+sEssenceName+"!");
            
            player.sendMessage("You just got a "+sEssenceName+"!");
        }
        else
        {
            cs.sendMessage(ChatColor.RED+args[1]+" is not a valid player!");
        }
        
        return true;
    }
}
