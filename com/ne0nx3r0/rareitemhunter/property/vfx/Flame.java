package com.ne0nx3r0.rareitemhunter.property.vfx;

import com.ne0nx3r0.rareitemhunter.property.ItemPropertyRepeatingEffect;
import com.ne0nx3r0.rareitemhunter.property.ItemPropertyTypes;
import org.bukkit.Effect;
import org.bukkit.entity.Player;

public class Flame extends ItemPropertyRepeatingEffect
{
    public Flame()
    {
        super(ItemPropertyTypes.VISUAL,"Flame","Flame surrounding you bro",1,0);
        
        this.createRepeatingAppliedEffect(this,20*2);
    }

    @Override
    public void applyEffectToPlayer(Player p,int level)
    {
        p.getWorld().playEffect(p.getLocation(), Effect.MOBSPAWNER_FLAMES, 0);
        p.getWorld().playEffect(p.getLocation(), Effect.SMOKE, 0);
    }
}
