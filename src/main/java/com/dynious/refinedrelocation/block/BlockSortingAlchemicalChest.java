package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileSortingAlchemicalChest;
import com.pahimar.ee3.block.BlockAlchemicalChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class BlockSortingAlchemicalChest extends BlockAlchemicalChest
{
    public BlockSortingAlchemicalChest(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.sortingAlchemicalChest);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3)
    {
        if (world.isRemote) return true;

        if (player.isSneaking())
        {
            APIUtils.openFilteringGUI(player, world, i, j, k);
            return true;
        }
        return super.onBlockActivated(world, i, j, k, player, i1, f1, f2, f3);
    }

    @Override
    public TileEntity createTileEntity(World world, int metaData)
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
    public void registerIcons(IconRegister iconRegister)
    {
        blockIcon = iconRegister.registerIcon("EE3:alchemicalChest");
    }
}
