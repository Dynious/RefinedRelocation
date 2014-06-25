package com.dynious.refinedrelocation.mods.waila;

import codechicken.multipart.TileMultipart;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.part.PartRelocator;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import mcp.mobius.waila.api.*;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.ForgeDirection;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;

import java.util.ArrayList;
import java.util.List;

public class RelocatorHUDHandler implements IWailaDataProvider
{
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> strings, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return strings;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> strings, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        if (iWailaDataAccessor.getTileEntity() instanceof TileRelocator)
        {
            if (iWailaDataAccessor.getPosition().subHit < 6)
            {
                int side = iWailaDataAccessor.getPosition().subHit;

                updateStringsFromRelocatorData(iWailaDataAccessor.getNBTData(), (IRelocator) iWailaDataAccessor.getTileEntity(), side, strings);
            }
        }
        else if (Mods.IS_FMP_LOADED && iWailaDataAccessor.getTileEntity() instanceof TileMultipart)
        {
            if (!iWailaDataAccessor.getNBTData().hasKey("parts")) return strings;

            if (iWailaDataAccessor.getPosition().subHit < 6)
            {
                int side = iWailaDataAccessor.getPosition().subHit;

                NBTTagList parts = iWailaDataAccessor.getNBTData().getTagList("parts");
                for (int i = 0; i < parts.tagCount(); i++)
                {
                    NBTTagCompound subtag = (NBTTagCompound) parts.tagAt(i);
                    String id = subtag.getString("id");

                    if (id.contains(PartRelocator.RELOCATOR_TYPE))
                    {
                        updateStringsFromRelocatorData(subtag.getCompoundTag("relocator"), (IRelocator) iWailaDataAccessor.getTileEntity(), side, strings);
                    }
                }
            }
        }
        return strings;
    }

    public void updateStringsFromRelocatorData(NBTTagCompound compound, IRelocator relocator, int side, List<String> strings)
    {
        if (compound.hasKey("StuffedItems"))
        {
            ArrayList<ItemStack> stacks = new ArrayList<ItemStack>();

            NBTTagList nbttaglist = compound.getTagList("StuffedItems");
            for (int x = 0; x < nbttaglist.tagCount(); x++)
            {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(x);
                byte stackSide = nbttagcompound1.getByte("Side");
                if (stackSide == side)
                {
                    stacks.add(ItemStack.loadItemStackFromNBT(nbttagcompound1));
                }
            }
            for (ItemStack stack : stacks)
            {
                strings.add("Stuffed: " + stack.getDisplayName());
            }
        }
        NBTTagList nbttaglist = compound.getTagList("modules");
        for (int i = 0; i < nbttaglist.tagCount(); ++i)
        {
            NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(i);
            byte place = nbttagcompound1.getByte("place");
            if (place == side)
            {
                IRelocatorModule module = RelocatorModuleRegistry.getModule(nbttagcompound1.getString("clazzIdentifier"));
                if (module != null)
                {
                    module.init(relocator, place);
                    module.readFromNBT(nbttagcompound1);

                    strings.add(module.getDrops(relocator, side).get(0).getDisplayName());
                }
            }
        }
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> strings, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return strings;
    }
}
