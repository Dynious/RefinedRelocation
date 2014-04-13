package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileSortingInterface;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSortingInterface extends BlockSortingConnector
{
    public BlockSortingInterface(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.sortingInterface);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileSortingInterface();
    }

    @Override
    public void breakBlock(World par1World, int par2, int par3, int par4, int par5, int par6)
    {
        TileSortingInterface tile = (TileSortingInterface) par1World.getBlockTileEntity(par2, par3, par4);

        if (tile != null)
        {
            tile.onTileDestroyed();
        }
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (world.isRemote)
        {
            return true;
        }
        else
        {
            APIUtils.openFilteringGUI(player, world, x, y, z);
            return true;
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon(Resources.MOD_ID + ":" + Names.sortingInterface);
    }
}
