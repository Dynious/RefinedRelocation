package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.mods.EE3Helper;
import com.dynious.refinedrelocation.mods.IronChestHelper;
import com.dynious.refinedrelocation.mods.JabbaHelper;
import com.dynious.refinedrelocation.tileentity.TileFilteringHopper;
import com.dynious.refinedrelocation.tileentity.TileSortingChest;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.world.World;

public class ItemSortingUpgrade extends Item
{
    public ItemSortingUpgrade(int par1)
    {
        super(par1);
        setMaxStackSize(1);
        setUnlocalizedName(Names.sortingUpgrade);
        setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int X, int Y, int Z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote) return false;
        TileEntity te = world.getBlockTileEntity(X, Y, Z);
        if (te != null)
        {
            if (te instanceof TileEntityChest)
            {
                TileEntityChest tec = (TileEntityChest) te;
                if (tec.numUsingPlayers > 0)
                {
                    return false;
                }
                // Force old TE out of the world so that adjacent chests can update
                TileSortingChest newChest = new TileSortingChest();
                ItemStack[] chestInventory = ObfuscationReflectionHelper.getPrivateValue(TileEntityChest.class, tec, 0);
                ItemStack[] chestContents = chestInventory.clone();
                newChest.setFacing((byte) tec.getBlockMetadata());
                for (int i = 0; i < chestInventory.length; i++)
                {
                    chestInventory[i] = null;
                }
                // Clear the old block out
                world.setBlock(X, Y, Z, 0, 0, 3);
                // Force the Chest TE to reset it's knowledge of neighbouring blocks
                tec.updateContainingBlockInfo();
                // Force the Chest TE to update any neighbours so they update next
                // tick
                tec.checkForAdjacentChests();
                // And put in our block instead
                world.setBlock(X, Y, Z, ModBlocks.sortingChest.blockID, 0, 3);

                world.setBlockTileEntity(X, Y, Z, newChest);
                world.setBlockMetadataWithNotify(X, Y, Z, 0, 3);
                System.arraycopy(chestContents, 0, newChest.inventory, 0, newChest.getSizeInventory());
                stack.stackSize--;
                return true;
            }
            else if (te instanceof TileEntityHopper)
            {
                TileEntityHopper hopper = (TileEntityHopper) te;

                // Force old TE out of the world so that adjacent chests can update
                TileFilteringHopper newHopper = new TileFilteringHopper();
                ItemStack[] chestInventory = ObfuscationReflectionHelper.getPrivateValue(TileEntityHopper.class, hopper, 0);
                ItemStack[] chestContents = chestInventory.clone();
                int meta =  hopper.getBlockMetadata();
                for (int i = 0; i < chestInventory.length; i++)
                {
                    chestInventory[i] = null;
                }
                // Clear the old block out
                world.setBlock(X, Y, Z, 0, 0, 3);

                // And put in our block instead
                world.setBlock(X, Y, Z, ModBlocks.filteringHopper.blockID, meta, 3);

                world.setBlockTileEntity(X, Y, Z, newHopper);
                world.setBlockMetadataWithNotify(X, Y, Z, meta, 3);
                System.arraycopy(chestContents, 0, (ItemStack[]) ObfuscationReflectionHelper.getPrivateValue(TileEntityHopper.class, newHopper, 0), 0, newHopper.getSizeInventory());
                stack.stackSize--;
                return true;
            }
            if (Loader.isModLoaded("IronChest"))
            {
                if (IronChestHelper.upgradeIronToFilteringChest(te))
                {
                    stack.stackSize--;
                    return true;
                }
            }
            if (Loader.isModLoaded("JABBA"))
            {
                if (JabbaHelper.upgradeToSortingBarrel(te))
                {
                    stack.stackSize--;
                    return true;
                }
            }
            if (Loader.isModLoaded("EE3"))
            {
                if (EE3Helper.upgradeAlchemicalToSortingChest(te))
                {
                    stack.stackSize--;
                    return true;
                }
            }
        }
        return false;
    }


    @Override
    public void registerIcons(IconRegister par1IconRegister)
    {
        itemIcon = par1IconRegister.registerIcon(Resources.MOD_ID + ":"
                + Names.sortingUpgrade);
    }
}
