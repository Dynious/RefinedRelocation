package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerPowerLimiter extends ContainerRefinedRelocation
{
    public static final int MESSAGE_REDSTONE_TOGGLE = 0;
    public static final int MESSAGE_POWER_LIMIT = 1;

    public TilePowerLimiter tile;
    private boolean initialUpdate = true;
    private boolean redstoneToggle = false;

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

    public void setRedstoneToggle(boolean toggle)
    {
        tile.setRedstoneToggle(toggle);
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (tile.getRedstoneToggle() != redstoneToggle || initialUpdate)
        {
            for (Object crafter : crafters)
            {
                ((ICrafting) crafter).sendProgressBarUpdate(this, 0, tile.getRedstoneToggle() ? 1 : 0);
            }
            redstoneToggle = tile.getRedstoneToggle();
        }

        if (initialUpdate)
            initialUpdate = false;
    }

    @Override
    public void updateProgressBar(int id, int value)
    {
        if (id == 0)
            setRedstoneToggle(value != 0);
    }

    @Override
    public void onMessage(int messageId, Object value, EntityPlayer entityPlayer)
    {
        switch(messageId) {
            case MESSAGE_REDSTONE_TOGGLE: setRedstoneToggle((Boolean) value); break;
            case MESSAGE_POWER_LIMIT: setMaxAcceptedEnergy((Double) value); break;
        }
    }
}
