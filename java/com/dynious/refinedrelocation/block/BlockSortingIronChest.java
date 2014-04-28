package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileSortingIronChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.IronChestType;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BlockSortingIronChest extends BlockIronChest
{
    public BlockSortingIronChest()
    {
        super();
        this.setBlockName(Names.sortingIronChest);
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
        return world.isSideSolid(i, j + 1, k, ForgeDirection.DOWN) || GuiHelper.openGui(player, world.getTileEntity(i, j, k));
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileSortingIronChest(IronChestType.values()[metadata]);
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
