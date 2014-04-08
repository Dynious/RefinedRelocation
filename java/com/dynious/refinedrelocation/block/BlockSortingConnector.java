package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileSortingConnector;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockSortingConnector extends BlockContainer
{
    protected BlockSortingConnector(int par1)
    {
        super(par1, Material.rock);
        this.setUnlocalizedName(Names.sortingConnector);
        this.setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileSortingConnector();
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileEntity tile = par1World.getBlockTileEntity(par2, par3, par4);
        if (tile != null && tile instanceof TileSortingConnector)
        {
            ((TileSortingConnector)tile).getSortingHandler().onTileDestroyed();
        }
        super.breakBlock(par1World, par2, par3, par4, par5, par6);
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileSortingConnector)
        {
            TileSortingConnector tile = (TileSortingConnector) tileEntity;
            Block blockDisguisedAs = tile.getDisguise();
            int disguisedMeta = tile.blockDisguisedMetadata;
            if (blockDisguisedAs != null)
                return blockDisguisedAs.getIcon(side, disguisedMeta);
        }
        return super.getBlockTexture(world, x, y, z, side);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.sortingConnector);
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileSortingConnector)
        {
            TileSortingConnector tile = (TileSortingConnector) tileEntity;
            Block blockDisguisedAs = tile.getDisguise();
            if (blockDisguisedAs != null)
                return blockDisguisedAs.colorMultiplier(world, x, y, z);
        }
        return super.colorMultiplier(world, x, y, z);
    }
}
