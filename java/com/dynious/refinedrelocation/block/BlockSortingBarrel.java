package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileSortingBarrel;
import mcp.mobius.betterbarrels.common.blocks.BlockBarrel;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSortingBarrel extends BlockBarrel
{
    public BlockSortingBarrel()
    {
        super();
        this.setBlockName(Names.sortingBarrel);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileSortingBarrel();
    }

    @Override
    public void breakBlock(World world, int x, int y, int z, Block par5, int par6)
    {
        TileSortingBarrel tile = (TileSortingBarrel )world.getTileEntity(x, y, z);
        tile.getSortingHandler().onTileDestroyed();
        super.breakBlock(world, x, y, z, par5, par6);
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack par6ItemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entity, par6ItemStack);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileSortingBarrel)
        {
            ((TileSortingBarrel)tile).getSortingHandler().onTileAdded();
        }
    }
}
