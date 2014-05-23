package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileSortingPreciousChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import rebelkeithy.mods.metallurgy.machines.chests.BlockPreciousChest;

public class BlockSortingPreciousChest extends BlockPreciousChest
{
    public BlockSortingPreciousChest(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.sortingPreciousChest);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public boolean onBlockActivated(World world, int i, int j, int k, EntityPlayer player, int i1, float f1, float f2, float f3)
    {
        if (world.isRemote)
        {
            return true;
        }
        if (player.isSneaking())
        {
            APIUtils.openFilteringGUI(player, world, i, j, k);
            return true;
        }
        return super.onBlockActivated(world, i, j, k, player, i1, f1, f2, f3);
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileSortingPreciousChest();
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
}
