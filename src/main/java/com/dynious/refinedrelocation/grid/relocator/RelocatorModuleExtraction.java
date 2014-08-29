package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.client.gui.GuiModuleExtraction;
import com.dynious.refinedrelocation.container.ContainerModuleExtraction;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.helper.StringHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Settings;
import com.dynious.refinedrelocation.lib.Strings;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleExtraction extends RelocatorModuleBase
{
    private static IIcon icon;
    private byte tick = 0;
    protected int lastCheckedSlot = -1;
    private int ticksBetweenExtraction = Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION;
    private boolean isPowered = false;
    private boolean hasHadPulse = false;
    public int redstoneControlState = 0;
    public int maxExtractionStackSize = 64; // By default extract a whole stack

    @Override
    public void init(IItemRelocator relocator, int side)
    {
        super.init(relocator, side);
    }

    @Override
    public boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        APIUtils.openRelocatorModuleGUI(relocator, player, side);
        return true;
    }

    @Override
    public void onUpdate(IItemRelocator relocator, int side)
    {
        tick++;
        if (tick >= ticksBetweenExtraction)
        {
            TileEntity tile = relocator.getConnectedInventories()[side];
            if (tile instanceof IInventory)
            {
                switch (redstoneControlState)
                {
                    case 0:
                        tryExtraction(relocator, (IInventory) tile, getExtractionSide(side), side, lastCheckedSlot);
                        tick = 0;
                        break;
                    case 1:
                        if (!isPowered)
                        {
                            tryExtraction(relocator, (IInventory) tile, getExtractionSide(side), side, lastCheckedSlot);
                            tick = 0;
                        }
                        break;
                    case 2:
                        if (isPowered)
                        {
                            tryExtraction(relocator, (IInventory) tile, getExtractionSide(side), side, lastCheckedSlot);
                            tick = 0;
                        }
                        break;
                    case 3:
                        if (hasHadPulse)
                        {
                            tryExtraction(relocator, (IInventory) tile, getExtractionSide(side), side, lastCheckedSlot);
                            tick = 0;
                            hasHadPulse = false;
                        }
                        break;
                }
            }
        }
    }

    @Override
    public void onRedstonePowerChange(boolean isPowered)
    {
        this.isPowered = isPowered;
        if (isPowered) hasHadPulse = true;
        super.onRedstonePowerChange(isPowered);
    }

    @Override
    public boolean connectsToRedstone()
    {
        return true;
    }

    protected int getExtractionSide(int side)
    {
        return ForgeDirection.OPPOSITES[side];
    }

    public void tryExtraction(IItemRelocator relocator, IInventory inventory, int extractionSide, int connectedSide, int firstChecked)
    {
        int slot = getNextSlot(inventory, ForgeDirection.getOrientation(extractionSide));
        if (slot != -1)
        {
            ItemStack stack = inventory.getStackInSlot(slot);
            if (stack != null && stack.stackSize != 0)
            {
                if (IOHelper.canExtractItemFromInventory(inventory, stack, slot, extractionSide))
                {
                    ItemStack stackCopy = stack.copy();
                    stackCopy.stackSize = Math.min(maxExtractionStackSize, stackCopy.stackSize);
                    int returnedStackExtra = stack.stackSize - stackCopy.stackSize;

                    ItemStack returnedStack = relocator.insert(stackCopy, connectedSide, false);

                    if (returnedStack == null || stack.stackSize != returnedStack.stackSize)
                    {
                        if (returnedStack != null)
                        {
                            returnedStack.stackSize = Math.min(returnedStack.stackSize + returnedStackExtra, returnedStack.getMaxStackSize()); // Should never be greater than 64
                        }
                        else if (returnedStackExtra > 0)
                        {
                            returnedStack = stackCopy;
                            returnedStack.stackSize = returnedStackExtra;
                        }
                        inventory.setInventorySlotContents(slot, returnedStack);
                    }
                }
            }
            else if (firstChecked != lastCheckedSlot)
            {
                if (firstChecked == -1)
                {
                    firstChecked = lastCheckedSlot;
                }
                tryExtraction(relocator, inventory, extractionSide, connectedSide, firstChecked);
            }
        }
    }

    public int getNextSlot(IInventory inventory, ForgeDirection direction)
    {
        if (inventory instanceof ISidedInventory)
        {
            ISidedInventory isidedinventory = (ISidedInventory) inventory;
            int[] accessibleSlotsFromSide = isidedinventory.getAccessibleSlotsFromSide(direction.ordinal());

            if (lastCheckedSlot < accessibleSlotsFromSide.length - 1)
            {
                lastCheckedSlot++;
            }
            else
            {
                lastCheckedSlot = 0;
            }
            return lastCheckedSlot < accessibleSlotsFromSide.length ? accessibleSlotsFromSide[lastCheckedSlot] : -1;
        }
        else
        {
            if (lastCheckedSlot < inventory.getSizeInventory() - 1)
            {
                lastCheckedSlot++;
            }
            else
            {
                lastCheckedSlot = 0;
            }
            return lastCheckedSlot;
        }
    }

    public void setTicksBetweenExtraction(int ticks)
    {
        ticksBetweenExtraction = Math.max(Settings.RELOCATOR_MIN_TICKS_BETWEEN_EXTRACTION, ticks);
    }

    public int getTicksBetweenExtraction()
    {
        return ticksBetweenExtraction;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public GuiScreen getGUI(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new GuiModuleExtraction(this);
    }

    @Override
    public Container getContainer(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new ContainerModuleExtraction(this);
    }

    @Override
    public void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        ticksBetweenExtraction = compound.getInteger("ticksBetweenExt");
        redstoneControlState = compound.getInteger("redstoneControlState");
        if (compound.hasKey("maxExtractionStackSize"))
            maxExtractionStackSize = compound.getInteger("maxExtractionStackSize");
    }

    @Override
    public void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        compound.setInteger("ticksBetweenExt", ticksBetweenExtraction);
        compound.setInteger("redstoneControlState", redstoneControlState);
        compound.setInteger("maxExtractionStackSize", maxExtractionStackSize);
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 3));
        return list;
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 3 + ".name");
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleExtraction");
    }

    @Override
    public List<String> getWailaInformation(NBTTagCompound nbtData)
    {
        List<String> information = new ArrayList<String>();
        information.add(StatCollector.translateToLocalFormatted(Strings.WAILA_MAX_STACK_SIZE, nbtData.getInteger("maxExtractionStackSize")));
        information.add(StatCollector.translateToLocalFormatted(Strings.WAILA_REDSTONE_CONTROL, StringHelper.getLocalizedString(Strings.MODULE_REDSTONE_CONTROL + nbtData.getInteger("redstoneControlState"))));
        return information;
    }
}
