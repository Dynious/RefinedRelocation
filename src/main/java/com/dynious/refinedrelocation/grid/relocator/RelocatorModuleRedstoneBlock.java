package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Icon;

import java.util.Arrays;
import java.util.List;

public class RelocatorModuleRedstoneBlock extends RelocatorModuleBase
{
    private static Icon iconOn;
    private static Icon iconOff;
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
            player.sendChatToPlayer(new ChatMessageComponent()
                .addText(StatCollector.translateToLocal(blockOnSignal ? Strings.REDSTONE_BLOCK_ENABLED : Strings.REDSTONE_BLOCK_DISABLED)));
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
    public Icon getIcon(IItemRelocator relocator, int side)
    {
        return relocator.getRedstoneState() ? (blockOnSignal ? iconOn : iconOff) : (blockOnSignal ? iconOff : iconOn);
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        iconOn = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleRedstoneBlockOn");
        iconOff = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleRedstoneBlockOff");
    }
}
