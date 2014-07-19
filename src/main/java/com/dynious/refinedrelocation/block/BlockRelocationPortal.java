package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.tileentity.TileRelocationPortal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockRelocationPortal extends BlockContainer
{
    protected BlockRelocationPortal()
    {
        super(Material.rock);
        this.setBlockName(Names.relocationPortal);
        this.setBlockUnbreakable();
        this.setResistance(60000F);
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta)
    {
        return new TileRelocationPortal();
    }

    @Override
    public boolean isBlockSolid(IBlockAccess world, int i, int j, int k, int o)
    {
        return false;
    }

    @Override
    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity)
    {
    }

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public Item getItemDropped(int par1, Random par2Random, int par3)
    {
        return null;
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileRelocationPortal)
        {
            TileRelocationPortal tile = (TileRelocationPortal) tileEntity;
            Block blockDisguisedAs = tile.oldBlock;
            if (blockDisguisedAs != null)
                return blockDisguisedAs.colorMultiplier(world, x, y, z);
        }
        return super.colorMultiplier(world, x, y, z);
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileRelocationPortal)
        {
            TileRelocationPortal tile = (TileRelocationPortal) tileEntity;
            Block blockDisguisedAs = tile.oldBlock;
            int disguisedMeta = tile.oldMeta;
            if (blockDisguisedAs != null)
                return blockDisguisedAs.getIcon(side, disguisedMeta);
        }
        return super.getIcon(world, x, y, z, side);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileRelocationPortal)
        {
            TileRelocationPortal tile = (TileRelocationPortal) tileEntity;

            return dir != ForgeDirection.UP && (tile.isLower() || dir != ForgeDirection.DOWN);
        }
        return false;
    }
}
