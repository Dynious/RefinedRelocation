package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.block.ModBlocks;
import net.minecraft.block.Block;
import com.dynious.refinedrelocation.tileentity.TileSortingInterface;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemSortingConnector extends ItemBlock
{
    public ItemSortingConnector(Block block)
    {
        super(block);
        this.setHasSubtypes(true);
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return getUnlocalizedName() + stack.getItemDamage();
    }

    @Override
    public int getMetadata(int meta)
    {
        return meta;
    }

    @Override
    public boolean placeBlockAt(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ, int metadata)
    {
        if (!super.placeBlockAt(stack, player, world, x, y, z, side, hitX, hitY, hitZ, metadata))
        {
            return false;
        }
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileSortingInterface)
        {
            ((TileSortingInterface) tile).setConnectedSide(ForgeDirection.getOrientation(side).getOpposite());
        }
        return true;
    }
}
