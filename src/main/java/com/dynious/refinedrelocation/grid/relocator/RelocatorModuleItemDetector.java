package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.Arrays;
import java.util.List;

public class RelocatorModuleItemDetector extends RelocatorModuleFilter
{
    private static IIcon[] icons = new IIcon[4];
    private boolean emitRedstoneSignal = false;
    private int tick = 0;

    @Override
    public void onUpdate(IItemRelocator relocator, int side)
    {
        if (tick > 0)
        {
            tick--;
            if (tick == 0)
            {
                emitRedstoneSignal = false;
                markRedstoneUpdate(relocator.getTileEntity());
            }
        }

        super.onUpdate(relocator, side);
    }

    @Override
    public int strongRedstonePower(int side)
    {
        return emitRedstoneSignal ? 15 : 0;
    }

    @Override
    public boolean connectsToRedstone()
    {
        return true;
    }

    public boolean passesFilter(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate)
    {
        if (!simulate && stack != null && filter.passesFilter(stack))
        {
            tick = 6;
            emitRedstoneSignal = true;
            markRedstoneUpdate(relocator.getTileEntity());
        }

        return true;
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        return Arrays.asList(new ItemStack(ModItems.relocatorModule, 1, 9));
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return emitRedstoneSignal ? icons[1] : icons[0];
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icons[0] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleItemDetector");
        icons[1] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleItemDetectorDetected");
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 9 + ".name");
    }

    private void markRedstoneUpdate(TileEntity relocator)
    {
        TileRelocator.markUpdateAndNotify(relocator.getWorldObj(), relocator.xCoord, relocator.yCoord, relocator.zCoord);
    }

    @Override
    public void readClientData(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        super.readClientData(relocator, side, compound);
        emitRedstoneSignal = compound.getBoolean("emit");
    }

    @Override
    public void writeClientData(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        super.writeClientData(relocator, side, compound);
        compound.setBoolean("emit", emitRedstoneSignal);
    }
}
