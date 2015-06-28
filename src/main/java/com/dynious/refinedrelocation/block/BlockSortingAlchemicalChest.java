package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileSortingAlchemicalChest;
import com.pahimar.ee3.block.BlockAlchemicalChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

@Deprecated
public class BlockSortingAlchemicalChest extends BlockAlchemicalChest
{
    public BlockSortingAlchemicalChest()
    {
        super();
        this.setBlockName(Names.sortingAlchemicalChest);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int metaData)
    {
        if (metaData == 0)
        {
            return new TileSortingAlchemicalChest(0);
        }
        else if (metaData == 1)
        {
            return new TileSortingAlchemicalChest(1);
        }
        else if (metaData == 2)
        {
            return new TileSortingAlchemicalChest(2);
        }

        return null;
    }

    @Override
    public String getUnlocalizedName()
    {
        return "tile." + Names.sortingAlchemicalChest;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean canRenderInPass(int pass)
    {
        return pass == 0 || pass == 1;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("EE3:alchemicalChest");
    }
}
