package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileSortingBarrel;
import mcp.mobius.betterbarrels.common.blocks.BlockBarrel;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSortingBarrel extends BlockBarrel
{
    public BlockSortingBarrel(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.sortingBarrel);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileSortingBarrel();
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase entity, ItemStack par6ItemStack)
    {
        super.onBlockPlacedBy(world, x, y, z, entity, par6ItemStack);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileSortingBarrel)
        {
            ((TileSortingBarrel)tile).getSortingHandler().onTileAdded();
        }
    }

    @Override
    public void registerIcons(IconRegister iconRegister)
    {
    }
}
