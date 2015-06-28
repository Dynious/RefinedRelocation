package com.dynious.refinedrelocation.block;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.mods.EE3Helper;
import com.dynious.refinedrelocation.mods.IronChestHelper;
import com.dynious.refinedrelocation.tileentity.TileSortingChest;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.Iterator;
import java.util.Random;

public class BlockSortingChest extends BlockContainer
{
    private final Random random = new Random();

    protected BlockSortingChest()
    {
        super(Material.wood);
        setHardness(3.0F);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
        this.setBlockName(Names.sortingChest);
    }

    /**
     * Looks for a sitting ocelot within certain bounds. Such an ocelot is considered to be blocking access to the
     * chest.
     */
    public static boolean isOcelotBlockingChest(World par0World, int par1, int par2, int par3)
    {
        Iterator iterator = par0World.getEntitiesWithinAABB(EntityOcelot.class, AxisAlignedBB.getBoundingBox((double) par1, (double) (par2 + 1), (double) par3, (double) (par1 + 1), (double) (par2 + 2), (double) (par3 + 1))).iterator();
        EntityOcelot entityocelot;

        do
        {
            if (!iterator.hasNext())
            {
                return false;
            }

            entityocelot = (EntityOcelot) iterator.next();
        }
        while (!entityocelot.isSitting());

        return true;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube?  This determines whether or not to render the shared face of two
     * adjacent blocks and also whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube()
    {
        return false;
    }

    /**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock()
    {
        return false;
    }

    /**
     * The type of render function that is called for this block
     */
    @Override
    public int getRenderType()
    {
        return 22;
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

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        this.setBlockBounds(0.0625F, 0.0F, 0.0625F, 0.9375F, 0.875F, 0.9375F);
    }

    /**
     * Called when the block is placed in the world.
     */
    @Override
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        byte chestFacing = 0;
        int facing = MathHelper.floor_double((double) ((par5EntityLivingBase.rotationYaw * 4F) / 360F) + 0.5D) & 3;
        if (facing == 0)
        {
            chestFacing = 2;
        }
        if (facing == 1)
        {
            chestFacing = 5;
        }
        if (facing == 2)
        {
            chestFacing = 3;
        }
        if (facing == 3)
        {
            chestFacing = 4;
        }

        TileEntity te = par1World.getTileEntity(par2, par3, par4);
        if (te != null && te instanceof TileSortingChest)
        {
            TileSortingChest tile = (TileSortingChest) te;
            tile.setFacing(chestFacing);
            par1World.markBlockForUpdate(par2, par3, par4);
        }
    }

    /**
     * Called on server worlds only when the block has been replaced by a different block ID, or the same block with a
     * different metadata value, but before the new metadata value is set. Args: World, x, y, z, old block ID, old
     * metadata
     */
    @Override
    public void breakBlock(World world, int x, int y, int z, Block oldBlock, int oldMetadata)
    {
        TileSortingChest tileEntityChest = (TileSortingChest) world.getTileEntity(x, y, z);

        if (tileEntityChest != null)
        {
            IOHelper.dropInventory(world, x, y, z);

            world.func_147453_f(x, y, z, oldBlock);
        }
        super.breakBlock(world, x, y, z, oldBlock, oldMetadata);
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9)
    {
        if (Mods.IS_IRON_CHEST_LOADED)
        {
            if (IronChestHelper.upgradeToIronChest(world, player, x, y, z))
            {
                return true;
            }
        }

        if (!world.isRemote)
        {
            IInventory iinventory = this.getInventory(world, x, y, z);

            if (iinventory != null)
            {
                if (!player.isSneaking())
                {
                    GuiHelper.openGui(player, world.getTileEntity(x, y, z));
                }
                else
                {
                    APIUtils.openFilteringGUI(player, world, x, y, z);
                }
            }
        }
        return true;
    }

    /**
     * Gets the inventory of the chest at the specified coords, accounting for blocks or ocelots on top of the chest,
     * and double chests.
     */
    public IInventory getInventory(World world, int x, int y, int z)
    {
        Object object = world.getTileEntity(x, y, z);

        if (object == null)
        {
            return null;
        }
        else if (world.isSideSolid(x, y + 1, z, ForgeDirection.DOWN))
        {
            return null;
        }
        else if (isOcelotBlockingChest(world, x, y, z))
        {
            return null;
        }
        else
        {
            return (IInventory) object;
        }
    }

    /**
     * Returns a new instance of a block's tile entity class. Called on placing the block.
     */
    @Override
    public TileEntity createNewTileEntity(World par1World, int meta)
    {
        return new TileSortingChest();
    }

    @SideOnly(Side.CLIENT)
    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("planks_oak");
    }

    @Override
    public boolean rotateBlock(World worldObj, int x, int y, int z, ForgeDirection axis)
    {
        if (worldObj.isRemote)
        {
            return false;
        }
        if (axis == ForgeDirection.UP || axis == ForgeDirection.DOWN)
        {
            TileEntity tileEntity = worldObj.getTileEntity(x, y, z);
            if (tileEntity instanceof TileSortingChest)
            {
                TileSortingChest sortingChest = (TileSortingChest) tileEntity;
                sortingChest.rotateAround(axis);
            }
            return true;
        }
        return false;
    }
}
