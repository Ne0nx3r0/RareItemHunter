package com.ne0nx3r0.rareitemhunter.property.enchantment;

import com.ne0nx3r0.rareitemhunter.property.ItemProperty;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.player.PlayerInteractEvent;

public class PaintWool extends ItemProperty
{
    public PaintWool()
    {
        super(ItemPropertyTypes.ENCHANTMENT,"Paint Wool","Rotates the color of a clicked wool block",1,1);
    }

    @Override
    public boolean onInteract(PlayerInteractEvent e,int level)
    {
        if(e.getClickedBlock() != null)
        {
            if(e.getClickedBlock().getType() == Material.WOOL)
            {
                Block woolBlock = e.getClickedBlock();
                byte woolData = woolBlock.getData();
                
                if(woolData == 0x15)
                {
                    woolData = 0x0;
                }
                else
                {
                    woolData++;
                }
                
                woolBlock.setData(woolData);
                
                return true;
            }
        }
        return false;
    }
}