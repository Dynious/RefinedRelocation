package com.dynious.refinedrelocation.container;

import com.dynious.refinedrelocation.helper.EnergyType;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUI;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIBoolean;
import com.dynious.refinedrelocation.network.packet.gui.MessageGUIBooleanArray;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import net.minecraft.entity.player.EntityPlayer;

import java.util.Arrays;

public class ContainerPowerLimiter extends ContainerRefinedRelocation
{
    private final TilePowerLimiter tile;
    private boolean initialUpdate = true;
    private boolean redstoneToggle;
    private boolean[] energyTypes = new boolean[EnergyType.values().length];

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

    public boolean[] getEnergyTypes()
    {
        return energyTypes;
    }

    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        if (tile.getRedstoneToggle() != redstoneToggle || initialUpdate)
        {
            sendSyncMessage(new MessageGUIBoolean(MessageGUI.REDSTONE_TOGGLE, tile.getRedstoneToggle()));
            redstoneToggle = tile.getRedstoneToggle();
        }

        boolean[] newEnergyTypes = new boolean[energyTypes.length];
        for(EnergyType energyType : tile.getConnectionTypes()) {
            newEnergyTypes[energyType.ordinal()] = true;
        }
        if(initialUpdate || !Arrays.equals(energyTypes, newEnergyTypes)) {
            sendSyncMessage(new MessageGUIBooleanArray(MessageGUI.ENERGY_TYPES, newEnergyTypes));
            for(int i = 0; i < energyTypes.length; i++) {
                energyTypes[i] = newEnergyTypes[i];
            }
        }

        initialUpdate = false;
    }

    @Override
    public void onMessageDouble(int messageId, double value, EntityPlayer player)
    {
        if(isRestrictedAccessWithError(player)) {
            return;
        }
        if(messageId == MessageGUI.POWER_LIMIT) {
            setMaxAcceptedEnergy(value);
        }
    }

    @Override
    public void onMessageBoolean(int messageId, boolean value, EntityPlayer player)
    {
        if(isRestrictedAccessWithError(player)) {
            return;
        }
        if(messageId == MessageGUI.REDSTONE_TOGGLE) {
            setRedstoneToggle(value);
        }
    }

    @Override
    public void onMessageBooleanArray(int messageId, boolean[] values, EntityPlayer player)
    {
        if(isRestrictedAccessWithError(player)) {
            return;
        }
        if(messageId == MessageGUI.ENERGY_TYPES) {
            energyTypes = values;
        }
    }
}
