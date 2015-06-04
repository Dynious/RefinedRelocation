package com.dynious.refinedrelocation.mods.waila;

import com.dynious.refinedrelocation.api.tileentity.IMultiFilterTile;
import com.dynious.refinedrelocation.api.tileentity.ISortingInventory;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.IAdvancedTile;
import com.dynious.refinedrelocation.tileentity.IDisguisable;
import com.dynious.refinedrelocation.tileentity.TileBuffer;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ITileHUDHandler implements IWailaDataProvider
{
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
    public List<String> getWailaBody(ItemStack itemStatck, List<String> strings, IWailaDataAccessor accessor, IWailaConfigHandler config)
    {
        TileEntity tileEntity = accessor.getTileEntity();
        if (tileEntity instanceof IMultiFilterTile)
        {
            NBTTagCompound compound = accessor.getNBTData();
            List<String> wailaInfo = ((IMultiFilterTile) tileEntity).getFilter().getWAILAInformation(compound);
            if (wailaInfo != null && !wailaInfo.isEmpty())
            {
                addToList(strings, wailaInfo);
            }
        }

        if (tileEntity instanceof ISortingInventory)
        {
            NBTTagCompound compound = accessor.getNBTData();
            if (compound.hasKey("priority"))
            {
                ISortingInventory.Priority priority = ISortingInventory.Priority.values()[compound.getByte("priority")];
                addToList(strings, StatCollector.translateToLocal(Strings.PRIORITY) + ": " + StatCollector.translateToLocal(priority.name().replace('_', '-')));
            }
        }

        if (tileEntity instanceof IAdvancedTile)
        {
            IAdvancedTile tile = (IAdvancedTile) accessor.getTileEntity();

            if (tile.getMaxStackSize() != -1)
                addToList(strings, StatCollector.translateToLocalFormatted(Strings.WAILA_MAX_STACK_SIZE, tile.getMaxStackSize()));

            if (tile instanceof TileBuffer)
            {
                if (tile.getSpreadItems())
                {
                    addToList(strings, StatCollector.translateToLocal(Strings.MODE) + ": " + StatCollector.translateToLocal(Strings.ROUND_ROBIN));
                }
                else
                {
                    addToList(strings, StatCollector.translateToLocal(Strings.MODE) + ": " + StatCollector.translateToLocal(Strings.GREEDY));
                }
            }
            else
            {
                addToList(strings, StatCollector.translateToLocal(Strings.MODE) + ": " + StatCollector.translateToLocal((tile.getSpreadItems()) ? Strings.SPREAD : Strings.STACK));
            }
        }

        if (tileEntity instanceof IDisguisable)
        {
            Block block = ((IDisguisable) tileEntity).getDisguise();
            int blockMeta = ((IDisguisable) tileEntity).getDisguiseMeta();
            if (block != null)
            {
                addToList(strings, StatCollector.translateToLocalFormatted(Strings.DISGUISED, BlockHelper.getBlockDisplayName(block, blockMeta)));
            }
        }

        return strings;
    }

    private void addToList(List<String> list, String string)
    {
        if (!list.contains(string))
        {
            list.add(string);
        }
    }

    private void addToList(List<String> list, List<String> stringsToAdd)
    {
        for (String stringToAdd : stringsToAdd)
        {
            addToList(list, stringToAdd);
        }
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
