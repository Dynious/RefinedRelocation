package com.dynious.refinedrelocation.mods.waila;

import com.dynious.refinedrelocation.helper.StringHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class RelocationControllerHUDHandler implements IWailaDataProvider
{
    private static final int TICKS_BETWEEN_UPDATE = 5;
    public static int tick = TICKS_BETWEEN_UPDATE; // Start at the amount of ticks so as to immediatly update.
    private static String linkedString = "";
    private static String lockedString = "";
    private static String formedString = "";

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        if (tick >= TICKS_BETWEEN_UPDATE)
        {
            NBTTagCompound compound = accessor.getNBTData();
            boolean isLinked = compound.hasKey("UUID") && !compound.getString("UUID").isEmpty();
            if (isLinked)
            {
                linkedString = StatCollector.translateToLocalFormatted(Strings.LINKED, StringHelper.YES);
            }
            else
            {
                linkedString = StatCollector.translateToLocalFormatted(Strings.LINKED, StringHelper.NO);
            }

            boolean isLocked = compound.hasKey("isLocked") && compound.getBoolean("isLocked");
            if (isLocked)
            {
                lockedString = StatCollector.translateToLocalFormatted(Strings.LOCKED, StringHelper.YES);
            }
            else
            {
                lockedString = StatCollector.translateToLocalFormatted(Strings.LOCKED, StringHelper.NO);
            }

            if (((TileMultiBlockBase) accessor.getTileEntity()).isFormed(false)) // Does not use NBT to make this check because this isn't saved to NBT
            {
                formedString = StatCollector.translateToLocalFormatted(Strings.FORMED, StringHelper.YES);
            }
            else
            {
                formedString = StatCollector.translateToLocalFormatted(Strings.FORMED, StringHelper.NO);
            }
            tick = 0;
        }

        currenttip.add(linkedString);
        currenttip.add(lockedString);
        currenttip.add(formedString);

        return currenttip;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        return null;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, int i, int i1, int i2)
    {
        if (tileEntity != null)
            tileEntity.writeToNBT(nbtTagCompound);
        return nbtTagCompound;
    }
}
