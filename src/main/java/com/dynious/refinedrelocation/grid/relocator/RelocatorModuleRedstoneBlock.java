package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;

import java.util.Arrays;
import java.util.List;

public class RelocatorModuleRedstoneBlock extends RelocatorModuleBase
{
    private static IIcon[] icons = new IIcon[4];
    private boolean blockOnSignal = true;

    @Override
    public boolean passesFilter(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate)
    {
        return blockOnSignal ? !relocator.getRedstoneState() : relocator.getRedstoneState();
    }

    @Override
    public boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        if (player.worldObj.isRemote)
        {
            return true;
        }
        else
        {
            blockOnSignal = !blockOnSignal;
            player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(blockOnSignal ? Strings.REDSTONE_BLOCK_ENABLED : Strings.REDSTONE_BLOCK_DISABLED)));
            TileRelocator.markUpdate(relocator.getTileEntity().getWorldObj(), relocator.getTileEntity().xCoord, relocator.getTileEntity().yCoord, relocator.getTileEntity().zCoord);
            return true;
        }
    }

    @Override
    public boolean connectsToRedstone()
    {
        return true;
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        return Arrays.asList(new ItemStack(ModItems.relocatorModule, 1, 7));
    }

    @Override
    public void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        blockOnSignal = compound.getBoolean("blockOnSignal");
    }

    @Override
    public void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        compound.setBoolean("blockOnSignal", blockOnSignal);
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return relocator.getRedstoneState() ? (blockOnSignal ? icons[0] : icons[1]) : (blockOnSignal ? icons[2] : icons[3]);
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icons[0] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleRedstoneBlockRSBlock");
        icons[1] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleRedstoneBlockRSPass");
        icons[2] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleRedstoneBlockPass");
        icons[3] = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleRedstoneBlockBlock");
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 7 + ".name");
    }
}
