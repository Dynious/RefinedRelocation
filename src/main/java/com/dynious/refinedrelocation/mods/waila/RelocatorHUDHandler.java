package com.dynious.refinedrelocation.mods.waila;

import codechicken.multipart.TileMultipart;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.part.PartRelocator;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import mcp.mobius.waila.api.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.ForgeDirection;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;

import java.util.ArrayList;
import java.util.List;

public class RelocatorHUDHandler implements IWailaDataProvider
{
    public static int tick = 0;
    private static final int ticksBetweenItemChange = 20; // 1 Second
    private int currentStackShown = 0;

    /*
    Cached Variables
    */
    public static ArrayList<ItemStack> stuffedItems = null;

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        if (tick >= ticksBetweenItemChange)
        {
            ++currentStackShown;
            tick = 0;
        }

        if (tick <= ticksBetweenItemChange)
        {
            IRelocator relocator = (IRelocator) getTileEntity(iWailaDataAccessor);
            NBTTagCompound compound = getCompound(iWailaDataAccessor, relocator);
            int side = iWailaDataAccessor.getPosition().subHit;

            if (side >= 6)
                return null;

            if (compound == null || relocator == null)
                return null;

            int stackAmount = getStackAmount(compound, side);
            if (currentStackShown > stackAmount)
                currentStackShown = 0;

            if (currentStackShown == 0)
                return null;

            return getItemStack(compound, side, currentStackShown);
        }

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
        IRelocator relocator = getTileEntity(iWailaDataAccessor);
        NBTTagCompound compound = getCompound(iWailaDataAccessor, relocator);
        int side = iWailaDataAccessor.getPosition().subHit;

        if (compound == null || relocator == null)
        {
            return strings;
        }
        else
        {
            for (IRelocatorModule module : getModules(compound, relocator, side))
            {
                strings.add(module.getDrops(relocator, side).get(0).getDisplayName());
            }

            if (compound.hasKey("StuffedItems"))
            {

                for (ItemStack stack : getItemStacks(compound, side))
                {
                    String modifier = "";
                    if (stack.itemID == itemStack.itemID) // If we are showing this stack
                    {
                        modifier += EnumChatFormatting.UNDERLINE;
                    }

                    strings.add("Stuffed: " + modifier + stack.getDisplayName() + " x " + stack.stackSize + EnumChatFormatting.RESET);
                }
            }
        }

        return strings;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> strings, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        return strings;
    }

    private void addStack(ArrayList<ItemStack> stacks, ItemStack stack)
    {
        boolean stacksContains = false;
        for (ItemStack needleStack : stacks)
        {
            if (needleStack.itemID == stack.itemID)
            {
                needleStack.stackSize += stack.stackSize;
                stacksContains = true;
            }
        }

        if (!stacksContains)
        {
            stacks.add(stack);
        }
    }

    private ItemStack getItemStack(NBTTagCompound compound, int side, int currentStackShown)
    {
        ArrayList<ItemStack> itemStacks = getItemStacks(compound, side);
        for (int i = 1; i <= itemStacks.size(); i++)
        {
            if (i < currentStackShown)
                continue;
            else
                return itemStacks.get(i - 1);
        }

        return null;
    }

    private int getStackAmount(NBTTagCompound compound, int side)
    {
        return getItemStacks(compound, side).size();
    }

    private ArrayList<ItemStack> getItemStacks(NBTTagCompound compound, int side)
    {
        if (stuffedItems != null)
        {
            return stuffedItems;
        }

        ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();
        if (compound.hasKey("StuffedItems"))
        {
            NBTTagList nbttaglist = compound.getTagList("StuffedItems");
            for (int x = 0; x < nbttaglist.tagCount(); x++)
            {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) nbttaglist.tagAt(x);
                byte stackSide = nbttagcompound1.getByte("Side");
                if (stackSide == side)
                {
                    addStack(itemStacks, ItemStack.loadItemStackFromNBT(nbttagcompound1));
                }
            }
        }

        stuffedItems = itemStacks;
        return itemStacks;
    }

    private ArrayList<IRelocatorModule> getModules(NBTTagCompound compound, IRelocator relocator, int side)
    {
        ArrayList<IRelocatorModule> modules = new ArrayList<IRelocatorModule>();
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
                    module.init(relocator, side);
                    module.readFromNBT(nbttagcompound1);
                    modules.add(module);
                }
            }
        }
        return modules;
    }

    private NBTTagCompound getCompound(IWailaDataAccessor iWailaDataAccessor, IRelocator tileentity)
    {
        int side = iWailaDataAccessor.getPosition().subHit;

        if (tileentity instanceof TileRelocator)
        {
            return iWailaDataAccessor.getNBTData();
        }
        else if (Mods.IS_FMP_LOADED && tileentity instanceof TileMultipart)
        {
            if (!iWailaDataAccessor.getNBTData().hasKey("parts")) return null;

            if (side < 6)
            {
                NBTTagList parts = iWailaDataAccessor.getNBTData().getTagList("parts");
                for (int i = 0; i < parts.tagCount(); i++)
                {
                    NBTTagCompound subtag = (NBTTagCompound) parts.tagAt(i);
                    String id = subtag.getString("id");

                    if (id.contains(PartRelocator.RELOCATOR_TYPE))
                    {
                        return subtag.getCompoundTag("relocator");
                    }
                }
            }
        }
        return null;
    }

    private IRelocator getTileEntity(IWailaDataAccessor iWailaDataAccessor)
    {
        TileEntity tileentity = iWailaDataAccessor.getTileEntity();
        if (tileentity instanceof TileRelocator || tileentity instanceof TileMultipart)
        {
            return (IRelocator) tileentity;
        }
        else
        {
            return null;
        }
    }
}
