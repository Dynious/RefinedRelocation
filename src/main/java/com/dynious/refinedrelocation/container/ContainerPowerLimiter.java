package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ICrafting;

public class ContainerPowerLimiter extends ContainerRefinedRelocation
{
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
            case MessageGUI.REDSTONE_TOGGLE: setRedstoneToggle((Boolean) value); break;
            case MessageGUI.POWER_LIMIT: setMaxAcceptedEnergy((Double) value); break;
        }
    }
}
