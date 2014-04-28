package com.dynious.refinedrelocation.gui.container;

import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;

public class ContainerPowerLimiter extends Container
{
    public TilePowerLimiter tile;

    public ContainerPowerLimiter(TilePowerLimiter tile)
    {
        this.tile = tile;
    }

    @Override
    public boolean canInteractWith(EntityPlayer entityplayer)
    {
        return true;
    }

    public void setMaxAcceptedEnergy(double amount)
    {
        tile.setMaxAcceptedEnergy(amount);
    }
}
