package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.helper.DirectionHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.TileBlockExtender;
import com.dynious.refinedrelocation.tileentity.TileRelocationPortal;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Icon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;
import java.util.Random;

public class BlockRelocationPortal extends BlockContainer
{
    protected BlockRelocationPortal(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.relocationPortal);
        this.setBlockUnbreakable();
        this.setResistance(60000F);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return new TileRelocationPortal();
    }

    public boolean isBlockSolidOnSide(World world, int i, int j, int k, ForgeDirection o)
    {
        return false;
    }

    public void addCollisionBoxesToList(World world, int i, int j, int k, AxisAlignedBB axisalignedbb, List arraylist, Entity par7Entity)
    {
    }

    public void setBlockBoundsBasedOnState(IBlockAccess iblockaccess, int i, int j, int k)
    {
        setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
    }

    public AxisAlignedBB getSelectedBoundingBoxFromPool(World w, int i, int j, int k)
    {
        return AxisAlignedBB.getBoundingBox(0.0D, 0.0D, 0.0D, 0.0D, 0.0D, 0.0D);
    }

    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isOpaqueCube()
    {
        return false;
    }

    public int idDropped(int par1, Random par2Random, int par3)
    {
        return 0;
    }

    @Override
    public void randomDisplayTick(World world, int x, int y, int z, Random random)
    {
        for (int particles = 0; particles < 10; particles++) {
            world.spawnParticle("portal", x + 0.5D +random.nextGaussian()/2, y + 0.5D + random.nextGaussian()/2, z + 0.5D + random.nextGaussian()/2, random.nextGaussian(), random.nextGaussian(), random.nextGaussian());
        }
    }

    @Override
    public int colorMultiplier(IBlockAccess world, int x, int y, int z)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileRelocationPortal)
        {
            TileRelocationPortal tile = (TileRelocationPortal) tileEntity;
            Block blockDisguisedAs = Block.blocksList[tile.oldId];
            if (blockDisguisedAs != null)
                return blockDisguisedAs.colorMultiplier(world, x, y, z);
        }
        return super.colorMultiplier(world, x, y, z);
    }

    @Override
    public Icon getBlockTexture(IBlockAccess world, int x, int y, int z, int side)
    {
        TileEntity tileEntity = world.getBlockTileEntity(x, y, z);
        if (tileEntity != null && tileEntity instanceof TileRelocationPortal)
        {
            TileRelocationPortal tile = (TileRelocationPortal) tileEntity;
            Block blockDisguisedAs = Block.blocksList[tile.oldId];
            int disguisedMeta = tile.oldMeta;
            if (blockDisguisedAs != null)
                return blockDisguisedAs.getIcon(side, disguisedMeta);
        }
        return super.getBlockTexture(world, x, y, z, side);
    }

    @Override
    public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
    {
        ForgeDirection dir = ForgeDirection.getOrientation(side);

        TileEntity tileEntity = DirectionHelper.getTileAtSide(world, x, y, z, dir.getOpposite());
        if (tileEntity != null && tileEntity instanceof TileRelocationPortal)
        {
            TileRelocationPortal tile = (TileRelocationPortal) tileEntity;

            return dir != ForgeDirection.UP && (tile.isLower() || dir != ForgeDirection.DOWN);
        }
        return false;
    }
}
