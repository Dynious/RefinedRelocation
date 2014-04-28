package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileSortingBarrel;
import mcp.mobius.betterbarrels.common.blocks.BlockBarrel;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.tileentity.TileEntity;
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
}
