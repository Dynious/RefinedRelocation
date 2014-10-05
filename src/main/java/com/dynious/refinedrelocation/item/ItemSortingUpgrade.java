package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.helper.IOHelper;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.mods.EE3Helper;
import com.dynious.refinedrelocation.mods.IronChestHelper;
import com.dynious.refinedrelocation.mods.JabbaHelper;
import com.dynious.refinedrelocation.tileentity.TileSortingChest;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

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
                ItemStack neededMaterial = null;

                if (te instanceof TileEntityChest)
                {
                    neededMaterial = new ItemStack(Blocks.planks);
                    if (!hasNeededItem(player, neededMaterial))
                        return true;
                    if (!upgradeNormalChest(te))
                        return false;
                }
                else if (Mods.IS_IRON_CHEST_LOADED && IronChestHelper.isIronChest(te))
                {
                    neededMaterial = IronChestHelper.getUpgradeItemStack(te);
                    if (!hasNeededItem(player, neededMaterial))
                        return true;
                    if (!IronChestHelper.upgradeIronToFilteringChest(te))
                        return false;
                }
                else if (Mods.IS_EE3_LOADED && EE3Helper.isAlchemicalChest(te))
                {
                    neededMaterial = EE3Helper.getUpgradeItemStack(te);
                    if (!hasNeededItem(player, neededMaterial))
                        return true;
                    if (!EE3Helper.upgradeAlchemicalToSortingChest(te))
                        return false;
                }

                removeNeededItem(player, neededMaterial);
                stack.stackSize--;
                return true;
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

    public static boolean hasNeededItem(EntityPlayer player, ItemStack stack)
    {
        stack.stackSize = 2;

        ItemStack returnedStack = IOHelper.extract(player.inventory, stack.copy(), ForgeDirection.UNKNOWN, true, true);
        if (returnedStack != null && returnedStack.stackSize >= stack.stackSize)
        {
            return true;
        }
        else
        {
            String name;
            //Fix name -.-
            if (stack.getItem() == ItemBlock.getItemFromBlock(Blocks.planks))
                name = StatCollector.translateToLocal(Strings.PLANKS);
            else
                name = stack.getDisplayName();
            player.addChatComponentMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.SORTING_UPGRADE_NO_MAT, name)));
            return false;
        }
    }

    public static void removeNeededItem(EntityPlayer player, ItemStack itemStack)
    {
        IOHelper.extract(player.inventory, itemStack, ForgeDirection.UNKNOWN, true, false);
        player.inventoryContainer.detectAndSendChanges();
    }

    public boolean upgradeNormalChest(TileEntity te)
    {
        World world = te.getWorldObj();
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
        world.setBlockToAir(te.xCoord, te.yCoord, te.zCoord);
        // Force the Chest TE to reset it's knowledge of neighbouring blocks
        tec.updateContainingBlockInfo();
        // Force the Chest TE to update any neighbours so they update next
        // tick
        tec.checkForAdjacentChests();
        // And put in our block instead
        world.setBlock(te.xCoord, te.yCoord, te.zCoord, ModBlocks.sortingChest, 0, 3);

        world.setTileEntity(te.xCoord, te.yCoord, te.zCoord, newChest);
        world.setBlockMetadataWithNotify(te.xCoord, te.yCoord, te.zCoord, 0, 3);
        System.arraycopy(chestContents, 0, newChest.inventory, 0, newChest.getSizeInventory());
        return true;
    }

    @Override
    public void addInformation(ItemStack itemstack, EntityPlayer player, List list, boolean b)
    {
        list.add(StatCollector.translateToLocal(Strings.SORTING_UPGRADE + itemstack.getItemDamage()));
        list.add(StatCollector.translateToLocal(Strings.SORTING_UPGRADE_MATS));
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
