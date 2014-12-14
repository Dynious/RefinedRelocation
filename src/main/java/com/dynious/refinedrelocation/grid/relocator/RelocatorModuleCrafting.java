package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.client.gui.GuiModuleCrafting;
import com.dynious.refinedrelocation.container.ContainerModuleCrafting;
import com.dynious.refinedrelocation.helper.ItemStackHelper;
import com.dynious.refinedrelocation.item.ModItems;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class RelocatorModuleCrafting extends RelocatorModuleBase
{
    private static IIcon icon;
    public World world;
    public final LocalInventoryCrafting CRAFT_MATRIX = new LocalInventoryCrafting();
    public final IInventory CRAFT_RESULT = new InventoryCraftResult();
    public int maxItemStack = 1;

    @Override
    public void init(IItemRelocator relocator, int side)
    {
        world = relocator.getTileEntity().getWorldObj();
    }

    @Override
    public boolean isItemDestination()
    {
        return true;
    }

    @Override
    public ItemStack receiveItemStack(IItemRelocator relocator, int side, ItemStack stack, boolean simulate)
    {
        for (int i = 0; i < CRAFT_MATRIX.getSizeInventory(); i++)
        {
            ItemStack craftStack = CRAFT_MATRIX.getStackInSlot(i);
            if (ItemStackHelper.areItemStacksEqual(stack, craftStack))
            {
                int toMove = Math.min(maxItemStack - craftStack.stackSize, stack.stackSize);

                stack.stackSize -= toMove;
                if (!simulate)
                    craftStack.stackSize += toMove;

                if (stack.stackSize == 0)
                {
                    return null;
                }
            }
        }
        return stack;
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        List<ItemStack> list = new ArrayList<ItemStack>();
        list.add(new ItemStack(ModItems.relocatorModule, 1, 12));
        return list;
    }

    @Override
    public boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        APIUtils.openRelocatorModuleGUI(relocator, player, side);
        return true;
    }

    @Override
    public GuiScreen getGUI(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new GuiModuleCrafting(player, this);
    }

    @Override
    public Container getContainer(IItemRelocator relocator, int side, EntityPlayer player)
    {
        return new ContainerModuleCrafting(player, this);
    }

    @Override
    public String getDisplayName()
    {
        return StatCollector.translateToLocal("item." + Names.relocatorModule + 12 + ".name");
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleExtraction");
    }

    private class LocalInventoryCrafting extends InventoryCrafting
    {
        public LocalInventoryCrafting()
        {
            super(new Container()
            {
                @Override
                public boolean canInteractWith(EntityPlayer p_75145_1_)
                {
                    return false;
                }
            }, 3, 3);

        }

        @Override
        public void setInventorySlotContents(int p_70299_1_, ItemStack p_70299_2_)
        {
            super.setInventorySlotContents(p_70299_1_, p_70299_2_);
            CRAFT_RESULT.setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(CRAFT_MATRIX, world));
        }
    }
}
