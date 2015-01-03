package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import net.minecraft.entity.player.EntityPlayer;

public class ContainerPowerLimiter extends ContainerRefinedRelocation
{
    public TilePowerLimiter tile;

    public ContainerPowerLimiter(TilePowerLimiter tile)
    {
        this.tile = tile;
        registerFieldSync("redstoneToggle", tile);
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

    public void setRedstoneToggle(boolean toggle)
    {
        tile.setRedstoneToggle(toggle);
    }

    @Override
    public void onMessage(int messageID, Object message)
    {
        if (messageID == 0)
            setRedstoneToggle((Boolean) message);
    }
}
