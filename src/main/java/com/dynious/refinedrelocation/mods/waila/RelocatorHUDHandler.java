package com.dynious.refinedrelocation.mods.waila;

import codechicken.multipart.TileMultipart;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.part.PartRelocator;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import mcp.mobius.waila.api.*;
import static mcp.mobius.waila.api.SpecialChars.*; // Staticly import to allow us to use these without SpecialChars.TAB;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;

import java.util.ArrayList;
import java.util.List;

public class RelocatorHUDHandler implements IWailaDataProvider
{
    public static int tick = 0;
    private static final int TICKS_BETWEEN_ITEM_CHANGE = 20; // 1 Second
    public static final int TICKS_BETWEEN_STUFFED_ITEM_UPDATE = 5; // 1 Second
    private int currentStackShown = 0;

    /*
    Cached Variables
    */
    public static ArrayList<ItemStack> stuffedItems = null;

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler)
    {
        if (tick >= TICKS_BETWEEN_ITEM_CHANGE)
        {
            ++currentStackShown;
            tick = 0;
        }

        if (tick <= TICKS_BETWEEN_ITEM_CHANGE)
        {
            IRelocator relocator = getTileEntity(iWailaDataAccessor);
            int side = iWailaDataAccessor.getPosition().subHit;

            if (side >= 6 || relocator == null)
                return null;

            int stackAmount = getItemStacks(iWailaDataAccessor, side).size();
            if (currentStackShown > stackAmount)
                currentStackShown = 0;

            if (currentStackShown == stackAmount)
                return null;

            return getItemStacks(iWailaDataAccessor, side).get(currentStackShown);
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
        int side = iWailaDataAccessor.getPosition().subHit;

        if (side >= 6)
        {
            return strings;
        }
        else
        {
            IRelocator relocator = getTileEntity(iWailaDataAccessor);

            IRelocatorModule module = relocator.getRelocatorModule(side);
            if (module != null)
            {
                strings.add(module.getDrops(relocator, side).get(0).getDisplayName());
            }

            List<String> stuffedStrings = new ArrayList<String>();
            for (ItemStack stack : getItemStacks(iWailaDataAccessor, side))
            {
                String modifier = "";
                if (ItemStackHelper.areItemStacksEqual(itemStack, stack)) // If we are showing this stack
                {
                    modifier += EnumChatFormatting.UNDERLINE;
                }

                stuffedStrings.add(modifier + stack.getDisplayName() + " x " + stack.stackSize + EnumChatFormatting.RESET);
            }

            for (int i = 0; i < stuffedStrings.size(); i++)
            {
                String finalString = "";
                if (i == 0) finalString += "Stuffed: ";

                strings.add(finalString + TAB + ALIGNRIGHT + stuffedStrings.get(i));
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
            if (ItemStackHelper.areItemStacksEqual(needleStack, stack))
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

    private ArrayList<ItemStack> getItemStacks(IWailaDataAccessor iWailaDataAccessor, int side)
    {
        IRelocator relocator = getTileEntity(iWailaDataAccessor);
        if (!relocator.isStuffedOnSide(side))
        {
            stuffedItems = new ArrayList<ItemStack>();
        }
        if (stuffedItems != null)
        {
            return stuffedItems;
        }

        NBTTagCompound compound = getCompound(iWailaDataAccessor);

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

    private NBTTagCompound getCompound(IWailaDataAccessor iWailaDataAccessor)
    {
        int side = iWailaDataAccessor.getPosition().subHit;
        TileEntity tileentity = iWailaDataAccessor.getTileEntity();

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
        if (tileentity instanceof IRelocator)
        {
            return (IRelocator) tileentity;
        }
        else
        {
            return null;
        }
    }
}
