package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.mods.EE3Helper;
import com.dynious.refinedrelocation.mods.IronChestHelper;
import com.dynious.refinedrelocation.mods.JabbaHelper;
import com.dynious.refinedrelocation.tileentity.TileFilteringHopper;
import com.dynious.refinedrelocation.tileentity.TileSortingChest;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityHopper;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.List;

public class ItemSortingUpgrade extends Item
{
    private IIcon[] icons = new IIcon[2];

    public ItemSortingUpgrade()
    {
        super();
        setUnlocalizedName(Names.sortingUpgrade);
        setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int par1)
    {
        return par1;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List list)
    {
        for (int i = 0; i < icons.length; i++)
        {
            list.add(new ItemStack(item, 1, i));
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int X, int Y, int Z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote) return false;
        TileEntity te = world.getTileEntity(X, Y, Z);
        if (te != null)
        {
            if (stack.getItemDamage() == 0)
            {
                if (te instanceof TileEntityChest)
                {
                    TileEntityChest tec = (TileEntityChest) te;
                    if (tec.numPlayersUsing > 0)
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
                    world.setBlockToAir(X, Y, Z);
                    // Force the Chest TE to reset it's knowledge of neighbouring blocks
                    tec.updateContainingBlockInfo();
                    // Force the Chest TE to update any neighbours so they update next
                    // tick
                    tec.checkForAdjacentChests();
                    // And put in our block instead
                    world.setBlock(X, Y, Z, ModBlocks.sortingChest, 0, 3);

                    world.setTileEntity(X, Y, Z, newChest);
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
                    int meta = hopper.getBlockMetadata();
                    for (int i = 0; i < chestInventory.length; i++)
                    {
                        chestInventory[i] = null;
                    }
                    // Clear the old block out
                    world.setBlockToAir(X, Y, Z);

                    // And put in our block instead
                    world.setBlock(X, Y, Z, ModBlocks.filteringHopper, meta, 3);

                    world.setTileEntity(X, Y, Z, newHopper);
                    world.setBlockMetadataWithNotify(X, Y, Z, meta, 3);
                    System.arraycopy(chestContents, 0, ObfuscationReflectionHelper.getPrivateValue(TileEntityHopper.class, newHopper, 0), 0, newHopper.getSizeInventory());
                    stack.stackSize--;
                    return true;
                }
                if (Mods.IS_IRON_CHEST_LOADED)
                {
                    if (IronChestHelper.upgradeIronToFilteringChest(te))
                    {
                        stack.stackSize--;
                        return true;
                    }
                }
                if (Mods.IS_EE3_LOADED)
                {
                    if (EE3Helper.upgradeAlchemicalToSortingChest(te))
                    {
                        stack.stackSize--;
                        return true;
                    }
                }
            }
            else if (stack.getItemDamage() == 1)
            {
                if (Mods.IS_JABBA_LOADED)
                {
                    if (JabbaHelper.upgradeToSortingBarrel(te))
                    {
                        stack.stackSize--;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean b)
    {
        list.add(StatCollector.translateToLocal(Strings.SORTING_UPGRADE + itemstack.getItemDamage()));
    }

    @Override
    public void registerIcons(IIconRegister par1IconRegister)
    {
        for (int i = 0; i < icons.length; i++)
        {
            icons[i] = par1IconRegister.registerIcon(Resources.MOD_ID + ":"
                    + Names.sortingUpgrade + i);
        }
    }

    @Override
    public IIcon getIconFromDamage(int damage)
    {
        if (damage >= 0 && damage < icons.length)
            return icons[damage];
        return null;
    }
}
