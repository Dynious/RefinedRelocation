package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.IFilteringInventory;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.GuiIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileFilteringIronChest;
import cpw.mods.fml.common.network.FMLNetworkHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.BlockIronChest;
import cpw.mods.ironchest.IronChestType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

public class BlockFilteringIronChest extends BlockIronChest
{
    public BlockFilteringIronChest(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.filteringIronChest);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public void breakBlock(World world, int i, int j, int k, int i1, int i2)
    {
        TileEntity tile = world.getBlockTileEntity(i, j, k);
        if (tile instanceof IFilteringInventory)
        {
            ((IFilteringInventory)tile).getFilteringInventoryHandler().onTileDestroyed();
        }
        super.breakBlock(world, i, j, k, i1, i2);
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
            FMLNetworkHandler.openGui(player, RefinedRelocation.instance, GuiIds.FILTERED, world, i, j, k);
            return true;
        }
        return world.isBlockSolidOnSide(i, j + 1, k, ForgeDirection.DOWN) || GuiHelper.openGui(player, world.getBlockTileEntity(i, j, k));
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        return new TileFilteringIronChest(IronChestType.values()[metadata]);
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
