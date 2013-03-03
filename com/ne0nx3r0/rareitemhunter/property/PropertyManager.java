package com.ne0nx3r0.rareitemhunter.property;

import com.ne0nx3r0.rareitemhunter.RareItemHunter;
import com.ne0nx3r0.rareitemhunter.property.abilities.*;
import com.ne0nx3r0.rareitemhunter.property.enchantments.*;
import com.ne0nx3r0.rareitemhunter.property.skills.*;
import com.ne0nx3r0.rareitemhunter.property.spells.*;
import com.ne0nx3r0.utils.FireworkVisualEffect;
import com.ne0nx3r0.utils.RomanNumeral;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;

public class PropertyManager
{
    private RareItemHunter plugin;
    private EnumMap<ItemPropertyTypes, String> TYPE_PREFIXES;
    private Map<String,ItemProperty> properties;
    private Map<String,Map<ItemProperty,Integer>> activePlayerEffects;
    private Map<String,Map<ItemProperty,BukkitTask>> playerTemporaryEffectTaskIds;
    private final FireworkVisualEffect fireworks;
    
    public PropertyManager(RareItemHunter plugin)
    {
        this.plugin = plugin;
        
        this.fireworks = new FireworkVisualEffect();
        
        this.activePlayerEffects = new HashMap<String,Map<ItemProperty,Integer>>();
        this.playerTemporaryEffectTaskIds = new HashMap<String,Map<ItemProperty,BukkitTask>>();

        TYPE_PREFIXES = new EnumMap<ItemPropertyTypes,String>(ItemPropertyTypes.class);
        TYPE_PREFIXES.put(ItemPropertyTypes.SKILL, ChatColor.GRAY+"Skill: "+ChatColor.RED);
        TYPE_PREFIXES.put(ItemPropertyTypes.ENCHANTMENT, ChatColor.GRAY+"Enchantment: "+ChatColor.GREEN);
        TYPE_PREFIXES.put(ItemPropertyTypes.SPELL, ChatColor.GRAY+"Spell: "+ChatColor.LIGHT_PURPLE);
        TYPE_PREFIXES.put(ItemPropertyTypes.ABILITY, ChatColor.GRAY+"Ability: "+ChatColor.GOLD);
        TYPE_PREFIXES.put(ItemPropertyTypes.VISUAL, ChatColor.GRAY+"Visual: "+ChatColor.BLACK);
        
        properties = new HashMap<String,ItemProperty>();
        
        this.addProperty(new Poison());
        this.addProperty(new Backstab());
        this.addProperty(new Blinding());
        this.addProperty(new CallLightning());
        this.addProperty(new Confuse());
        this.addProperty(new Disarm());
        this.addProperty(new Slow());
        this.addProperty(new VampiricRegeneration());
        this.addProperty(new Weaken());
        
        
        this.addProperty(new Fertilize());
        this.addProperty(new FireHandling());
        this.addProperty(new HalfBakedIdea());
        this.addProperty(new MeltObsidian());
        this.addProperty(new PaintWool());
        this.addProperty(new Smelt());
        this.addProperty(new Spore());
        
        this.addProperty(new Durability());
        this.addProperty(new Fly(this));
        this.addProperty(new Hardy());
        this.addProperty(new Regeneration());
        this.addProperty(new Strength());
        this.addProperty(new ToughLove());
        this.addProperty(new WaterBreathing());
        
        this.addProperty(new Burst(fireworks));
        this.addProperty(new CatsFeet(this));
        this.addProperty(new CraftItem());
        this.addProperty(new FireResistance());
        this.addProperty(new GreaterBurst(fireworks));
        this.addProperty(new GrowTree());
        this.addProperty(new Haste());
        this.addProperty(new Invisibility());
        this.addProperty(new MagicBag());
        this.addProperty(new RepairItem());
        this.addProperty(new SummonBat());
        this.addProperty(new SummonChicken());
        this.addProperty(new SummonCow());
        this.addProperty(new SummonMooshroom());
        this.addProperty(new SummonOcelot());
        this.addProperty(new SummonPig());
        this.addProperty(new SummonSheep());
        this.addProperty(new SummonSlime());
    }

    private void addProperty(ItemProperty icp)
    {               
        this.properties.put(icp.getName().toLowerCase(), icp);
    } 

    public ItemProperty getPropertyFromComponent(ItemStack is)
    {
        if(is != null
        && is.getItemMeta() != null
        && is.getItemMeta().getLore() != null)
        {
            String sPropertyString = is.getItemMeta().getLore().get(0);
            
            for(String sPrefix : this.TYPE_PREFIXES.values())
            {
                if(sPropertyString.startsWith(sPrefix))
                {
                    String sPropertyName = sPropertyString.substring(sPrefix.length());

                    return this.properties.get(sPropertyName.toLowerCase());
                }
            }
        }
        
        return null;
    }

    public String getPropertyString(ItemProperty icp, Integer level)
    {
        return this.TYPE_PREFIXES.get(icp.getType())+icp.getName() + " " + RomanNumeral.convertToRoman(level);
    }
  
//Wrappers  
    public void onArrowHitEntity(Player shooter, ItemStack bow, EntityDamageByEntityEvent e)
    {
        this.ActivatePlayerRareItem(shooter, bow, e, ItemPropertyActions.ARROW_HIT_ENTITY);
    }

    public void onDamagedOtherEntity(Player attacker, EntityDamageByEntityEvent e)
    {
        this.ActivatePlayerRareItem(attacker, attacker.getItemInHand(), e, ItemPropertyActions.DAMAGE_OTHER_ENTITY);
    }

    public void onInteract(Player player, ItemStack itemInHand, PlayerInteractEvent e)
    {
        this.ActivatePlayerRareItem(player, itemInHand, e, ItemPropertyActions.INTERACT);
    }

    public void onInteractEntity(PlayerInteractEntityEvent e)
    {
        this.ActivatePlayerRareItem(e.getPlayer(), e.getPlayer().getItemInHand(), e, ItemPropertyActions.INTERACT_ENTITY);
    }

    public void onEquip(InventoryClickEvent e)
    {
        this.ActivatePlayerRareItem((Player) e.getWhoClicked(), e.getCursor(), e, ItemPropertyActions.EQUIP);
    }

    public void onUnequip(InventoryClickEvent e)
    {
        this.ActivatePlayerRareItem((Player) e.getWhoClicked(), e.getCurrentItem(), e, ItemPropertyActions.UNEQUIP);
    }
    
    private void ActivatePlayerRareItem(Player player,ItemStack is,Event event,ItemPropertyActions action)
    {
        if(is == null
        || !is.hasItemMeta()
        || !is.getItemMeta().hasLore())
        {
            return;
        }
        
        List<String> lore = is.getItemMeta().getLore();
        
        if(!lore.get(0).equals(plugin.RAREITEM_HEADER_STRING))
        {
            return;
        }
        
        lore.remove(0);
        
        for(String sLore : lore)
        {
            for(String sPrefix : TYPE_PREFIXES.values())
            {
                if(sLore.startsWith(sPrefix))
                {
                    String sPropertyString = sLore.substring(sPrefix.length());
                    
                    int level = 1;
                    
                    String sLevel = sPropertyString.substring(sPropertyString.lastIndexOf(" ")+1);

                    try
                    {
                        level = RomanNumeral.valueOf(sLevel);
                        sPropertyString = sPropertyString.substring(0,sPropertyString.lastIndexOf(" "));
                    }
                    catch(Exception ex){}

                    ItemProperty property = this.properties.get(sPropertyString.toLowerCase());
                    
                    if(property != null)
                    {
                        int levelIncrement = plugin.COST_LEVEL_INCREMENT;
                        
                        if(levelIncrement > 1)
                        {
                            levelIncrement = level / levelIncrement;
                        }

                        int cost = (property.getCost(level) - levelIncrement) * plugin.COST_MULTIPLIER;
                        
                        boolean hasCost = this.hasCost(player, cost);

                        if(action == ItemPropertyActions.INTERACT)
                        {
                            if(property.getType() == ItemPropertyTypes.ENCHANTMENT || property.getType() == ItemPropertyTypes.SPELL)
                            {
                                if(hasCost)
                                {
                                    if(property.onInteract((PlayerInteractEvent) event, level))
                                    {
                                        this.takeCost(player, cost);
                                    }
                                }
                                else
                                {
                                    this.sendCostMessage(player,property,cost);
                                }
                            }
                        }
                        else if(action == ItemPropertyActions.ARROW_HIT_ENTITY)
                        {
                            if(property.getType() == ItemPropertyTypes.BOW)
                            {
                                if(hasCost)
                                {
                                    if(property.onArrowHitEntity((EntityDamageByEntityEvent) event, player, level))
                                    {
                                        this.takeCost(player, cost);
                                    }
                                }
                                else
                                {
                                    this.sendCostMessage(player,property,cost);
                                }
                            }
                        }
                        else if(action == ItemPropertyActions.DAMAGE_OTHER_ENTITY)
                        {
                            if(property.getType() == ItemPropertyTypes.SKILL)
                            {
                                if(hasCost)
                                {
                                    if(property.onDamageOther((EntityDamageByEntityEvent) event, player, level))
                                    {
                                        this.takeCost(player, cost);
                                    }
                                }
                                else
                                {
                                    this.sendCostMessage(player,property,cost);
                                }
                            }
                        }
                        else if(action == ItemPropertyActions.INTERACT_ENTITY)
                        {
                            if(property.getType() == ItemPropertyTypes.SPELL)
                            {
                                if(hasCost)
                                {
                                    if(property.onInteractEntity((PlayerInteractEntityEvent) event, level))
                                    {
                                        this.takeCost(player, cost);
                                    }
                                }
                                else
                                {
                                    this.sendCostMessage(player,property,cost);
                                }
                            }
                        }
                        else if(action == ItemPropertyActions.EQUIP)
                        {
                            if(property.getType() == ItemPropertyTypes.ABILITY)
                            {
                                this.grantPlayerEffect(player,property,level);
        
                                property.onEquip(player,level);
                            }
                        }
                        else if(action == ItemPropertyActions.UNEQUIP)
                        {
                            if(property.getType() == ItemPropertyTypes.ABILITY)
                            {
                                this.revokePlayerEffect(player,property,level);
        
                                property.onUnequip(player,level);
                            }
                        }
                    }
                }
            }            
        }
    }

    public boolean hasCost(Player pShooter, int cost)
    {
        if(plugin.COST_TYPE == ItemPropertyCostTypes.FOOD)
        {
            if(pShooter.getFoodLevel() >= cost)
            {
                return true;
            }
        }        
        else if(plugin.COST_TYPE == ItemPropertyCostTypes.XP)
        {
            if(pShooter.getExp() >= cost)
            {
                return true;
            }
        }        
        else if(plugin.COST_TYPE == ItemPropertyCostTypes.MONEY)
        {
            if(plugin.economy.has(pShooter.getName(), cost))
            {
                return true;
            }
        }
        return false;
    }
    
    public void takeCost(Player player, int cost)
    {   
        if(plugin.COST_TYPE == ItemPropertyCostTypes.FOOD)
        {
            if(player.getFoodLevel() >= cost)
            {
                player.setFoodLevel(player.getFoodLevel() - cost);
            }
        }        
        else if(plugin.COST_TYPE == ItemPropertyCostTypes.XP)
        {
            if(player.getExp() >= cost)
            {
                player.setExp(player.getExp() - cost);
            }
        }        
        else if(plugin.COST_TYPE == ItemPropertyCostTypes.MONEY)
        {
            if(plugin.economy.has(player.getName(), cost))
            {
                plugin.economy.withdrawPlayer(player.getName(), cost);
            }
        }
    }

    public ItemProperty getProperty(String property)
    {
        return this.properties.get(property.toLowerCase());
    }

    public String getPropertyComponentString(ItemProperty ip)
    {
        return this.TYPE_PREFIXES.get(ip.getType())+ip.getName();
    }

    public void sendCostMessage(Player player, ItemProperty property, int cost)
    {
        player.sendMessage(ChatColor.RED+"You need at least "+cost+" "+plugin.COST_TYPE.name().toLowerCase()+" to use "+property.getName()+"!");
    }

    public void grantPlayerEffect(Player player, ItemProperty property, int level)
    {
        Map<ItemProperty, Integer> playerEffects;
        
        if(this.activePlayerEffects.containsKey(player.getName()))
        {
            playerEffects = this.activePlayerEffects.get(player.getName());
        }
        else
        {
            playerEffects = this.activePlayerEffects.put(player.getName(),new HashMap<ItemProperty,Integer>());
        }
        
        playerEffects.put(property, level);
    }

    public void revokePlayerEffect(Player player, ItemProperty property, int level)
    {
        this.revokePlayerEffect(player.getName(), property, level);
    }

    private void revokePlayerEffect(String sPlayer, ItemProperty property, int level)
    {
        Map<ItemProperty, Integer> playerEffects = this.activePlayerEffects.get(sPlayer);
        
        if(playerEffects != null)
        {
            playerEffects.remove(property);
        }
        
        if(playerEffects.isEmpty())
        {
            this.activePlayerEffects.remove(sPlayer);
        }
    }

    public void revokeAllItemProperties(Player player)
    {
        if(activePlayerEffects.containsKey(player.getName()))
        {
            Map<ItemProperty, Integer> playerEffects = activePlayerEffects.get(player.getName());
            
            for(ItemProperty ip : playerEffects.keySet())
            {
                this.revokePlayerEffect(player, ip, playerEffects.get(ip));
            }
        }
    }

    public void addTemporaryEffect(Player player, final ItemProperty property, final int level, int duration)
    {
        final String sPlayer = player.getName();
        
        Map<ItemProperty,BukkitTask> taskIds;
        
        if(!this.playerTemporaryEffectTaskIds.containsKey(sPlayer))
        {
            taskIds = playerTemporaryEffectTaskIds.put(sPlayer, new HashMap<ItemProperty,BukkitTask>());
        }
        else
        {
            taskIds = playerTemporaryEffectTaskIds.get(sPlayer);
            
            if(taskIds.containsKey(property))
            {
                taskIds.get(property).cancel();
            }
        }
        
        final PropertyManager pm = this;
        
        taskIds.put(property, plugin.getServer().getScheduler().runTaskLater(plugin,new Runnable()
        {
            @Override
            public void run()
            {
                pm.revokePlayerEffect(sPlayer, property, level);
            }
        },duration));
    }
}