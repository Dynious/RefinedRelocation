package com.dynious.refinedrelocation.mods;

import com.dynious.refinedrelocation.api.ModObjects;
import com.dynious.refinedrelocation.block.BlockSortingIronChest;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.item.ItemSortingIronChest;
import com.dynious.refinedrelocation.lib.BlockIds;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.renderer.ItemRendererSortingIronChest;
import com.dynious.refinedrelocation.renderer.RendererSortingIronChest;
import com.dynious.refinedrelocation.tileentity.TileSortingChest;
import com.dynious.refinedrelocation.tileentity.TileSortingIronChest;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import cpw.mods.ironchest.IronChest;
import cpw.mods.ironchest.IronChestType;
import cpw.mods.ironchest.ItemChestChanger;
import cpw.mods.ironchest.TileEntityIronChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;

public class IronChestHelper
{
    public static void addIronChestBlocks()
    {
        ModBlocks.sortingIronChest = new BlockSortingIronChest(BlockIds.SORTING_IRON_CHEST);
        ModObjects.sortingIronChest = new ItemStack(ModBlocks.sortingIronChest);
        GameRegistry.registerBlock(ModBlocks.sortingIronChest, ItemSortingIronChest.class, Names.sortingIronChest);
    }

    public static void addIronChestRecipes()
    {
        for (int i = 0; i < IronChestType.values().length; i++)
        {
            GameRegistry.addShapedRecipe(new ItemStack(ModBlocks.sortingIronChest, 1, i), "g g", " b ", "g g", 'g', Item.ingotGold, 'b', new ItemStack(IronChest.ironChestBlock, 1, i));
        }
        for (int i = 0; i < IronChestType.values().length; i++)
        {
            GameRegistry.addShapelessRecipe(new ItemStack(IronChest.ironChestBlock, 1, i), new ItemStack(ModBlocks.sortingIronChest, 1, i));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void addIronChestRenders()
    {
        ClientRegistry.bindTileEntitySpecialRenderer(TileSortingIronChest.class, new RendererSortingIronChest());
        MinecraftForgeClient.registerItemRenderer(BlockIds.SORTING_IRON_CHEST, new ItemRendererSortingIronChest());
    }

    public static boolean upgradeToIronChest(World world, EntityPlayer player, int x, int y, int z)
    {
        if (player.getHeldItem() != null && player.getHeldItem().getItem() instanceof ItemChestChanger)
        {
            ItemChestChanger chestChanger = (ItemChestChanger) player.getHeldItem().getItem();
            if (chestChanger.getType().canUpgrade(IronChestType.WOOD))
            {
                TileEntity tile = world.getBlockTileEntity(x, y, z);
                if (tile instanceof TileSortingChest)
                {
                    TileSortingChest tec = (TileSortingChest) tile;
                    if (tec.numUsingPlayers > 0)
                    {
                        return false;
                    }
                    // Force old TE out of the world so that adjacent chests can update
                    TileSortingIronChest newchest = new TileSortingIronChest(IronChestType.values()[chestChanger.getType().getTarget()]);
                    ItemStack[] chestInventory = tec.inventory;
                    ItemStack[] chestContents = chestInventory.clone();
                    newchest.setFacing((byte) tec.getFacing());
                    for (int i = 0; i < chestInventory.length; i++)
                    {
                        chestInventory[i] = null;
                    }

                    NBTTagCompound tag = new NBTTagCompound();
                    tec.getFilter().writeToNBT(tag);
                    newchest.getFilter().readFromNBT(tag);

                    // Clear the old block out
                    world.setBlock(x, y, z, 0, 0, 3);
                    // Force the Chest TE to reset it's knowledge of neighbouring blocks
                    // And put in our block instead
                    world.setBlock(x, y, z, ModBlocks.sortingIronChest.blockID, chestChanger.getType().getTarget(), 3);

                    world.setBlockTileEntity(x, y, z, newchest);
                    world.setBlockMetadataWithNotify(x, y, z, chestChanger.getType().getTarget(), 3);
                    System.arraycopy(chestContents, 0, newchest.chestContents, 0, Math.min(chestContents.length, newchest.getSizeInventory()));
                    player.getHeldItem().stackSize--;
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean upgradeIronToFilteringChest(TileEntity tile)
    {
        if (tile instanceof TileEntityIronChest && !(tile instanceof TileSortingIronChest))
        {
            World world = tile.getWorldObj();

            TileEntityIronChest teic = (TileEntityIronChest) tile;
            int numUsers = ObfuscationReflectionHelper.getPrivateValue(TileEntityIronChest.class, teic, "numUsingPlayers");
            if (numUsers > 0)
            {
                return false;
            }
            TileSortingIronChest chest = new TileSortingIronChest(teic.getType());
            ItemStack[] chestContents = teic.chestContents.clone();
            chest.setFacing(teic.getFacing());
            for (int i = 0; i < teic.chestContents.length; i++)
            {
                teic.chestContents[i] = null;
            }
            // Clear the old block out
            world.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, 0, 0, 3);
            // And put in our block instead
            world.setBlock(tile.xCoord, tile.yCoord, tile.zCoord, ModBlocks.sortingIronChest.blockID, teic.getType().ordinal(), 3);

            world.setBlockTileEntity(tile.xCoord, tile.yCoord, tile.zCoord, chest);
            world.setBlockMetadataWithNotify(tile.xCoord, tile.yCoord, tile.zCoord, chest.getType().ordinal(), 3);
            System.arraycopy(chestContents, 0, chest.chestContents, 0, chest.getSizeInventory());
            return true;
        }
        return false;
    }
}
