package com.dynious.blex.block;

import cofh.api.block.IDismantleable;
import com.dynious.blex.BlockExtenders;
import com.dynious.blex.helper.GuiHelper;
import com.dynious.blex.lib.Names;
import com.dynious.blex.tileentity.TileAdvancedBuffer;
import com.dynious.blex.tileentity.TileBuffer;
import com.dynious.blex.tileentity.TileFilteredBuffer;
import cpw.mods.fml.common.Optional.Method;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;
import java.util.ArrayList;
import java.util.List;
import static cpw.mods.fml.common.Optional.Interface;
import static cpw.mods.fml.common.Optional.InterfaceList;

@InterfaceList(value = {@Interface(iface = "cofh.api.block.IDismantleable", modid = "CoFHCore")})
public class BlockBuffer extends BlockContainer implements IDismantleable
{
    protected BlockBuffer(int id)
    {
        super(id, Material.rock);
        this.setUnlocalizedName(Names.buffer);
        this.setHardness(3.0F);
        this.setCreativeTab(BlockExtenders.tabBlEx);
    }

    @Override
    public TileEntity createNewTileEntity(World world)
    {
        return null;
    }

    @Override
    public TileEntity createTileEntity(World world, int metadata)
    {
        switch (metadata)
        {
            case 0:
                return new TileBuffer();
            case 1:
                return new TileAdvancedBuffer();
            case 2:
                return new TileFilteredBuffer();
        }
        return null;
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
                                    EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (player.isSneaking())
        {
            return false;
        }
        else
        {
            TileEntity tile = world.getBlockTileEntity(x, y, z);
            if (tile != null)
            {
                GuiHelper.openGui(player, tile);
            }
        }
        return true;
    }

    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    @Override
    public int getRenderType()
    {
        return -1;
    }

    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    public boolean isBlockSolidOnSide(World world, int x, int y, int z, ForgeDirection side)
    {
        return true;
    }

    @Override
    public int getRenderBlockPass()
    {
        return 1;
    }

    @Override
    public void onNeighborBlockChange(World world, int x, int y, int z, int side)
    {
        super.onNeighborBlockChange(world, x, y, z, side);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileBuffer)
        {
            ((TileBuffer) tile).onBlocksChanged();
        }
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public void getSubBlocks(int par1, CreativeTabs par2CreativeTabs,
                             List par3List)
    {
        for (int j = 0; j < 3; ++j)
        {
            par3List.add(new ItemStack(par1, 1, j));
        }
    }

    @Override
    protected String getTextureName()
    {
        return "obsidian";
    }

    @Override
    public int damageDropped(int metadata)
    {
        return metadata;
    }

    @Method(modid = "CoFHCore")
    @Override
    public ItemStack dismantleBlock(EntityPlayer player, World world, int x,
                                    int y, int z, boolean returnBlock)
    {
        int meta = world.getBlockMetadata(x, y, z);

        ArrayList<ItemStack> items = this.getBlockDropped(world, x, y, z, meta, 0);

        for (ItemStack item : items)
        {

            EntityItem entityitem = new EntityItem(world, x + 0.5F, y + 0.5F, z + 0.5F, item);

            entityitem.delayBeforeCanPickup = 10;

            world.spawnEntityInWorld(entityitem);

            entityitem.motionX *= 0.25F;
            entityitem.motionZ *= 0.25F;
        }

        world.setBlockToAir(x, y, z);
        return null;
    }

    @Method(modid = "CoFHCore")
    @Override
    public boolean canDismantle(EntityPlayer player, World world, int x, int y, int z)
    {
        return true;
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        TileBuffer tile = (TileBuffer) worldObj.getBlockTileEntity(x, y, z);
        return tile.rotateBlock();
    }
}
