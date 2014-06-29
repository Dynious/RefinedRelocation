package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.lib.*;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraft.util.Icon;

import org.lwjgl.opengl.GL11;

import java.util.List;

public class ItemFrozenLiquid extends Item
{
    public ItemFrozenLiquid(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.frozenLiquid);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        this.setMaxStackSize(1);
    }

    public static void setFluid(ItemStack stack, FluidStack fluid)
    {
        if (!stack.hasTagCompound())
        {
            stack.setTagCompound(new NBTTagCompound());
        }

        if (fluid != null)
        {
            stack.getTagCompound().setInteger("fluidID", fluid.fluidID);
            stack.getTagCompound().setInteger("fluidAmount", fluid.amount);
        }
    }

    public static FluidStack getFluidStack(ItemStack stack)
    {
        return new FluidStack(stack.getTagCompound().getInteger("fluidID"), stack.getTagCompound().getInteger("fluidAmount"));
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b)
    {
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("fluidID"))
        {
            FluidStack fluidStack = this.getFluidStack(itemStack);
            Fluid fluid = fluidStack.getFluid();
            list.add("Fluid: " + getFluidName(itemStack) + " (" + fluidStack.amount + " millibuckets)");
        }
    }

    public static String getFluidName(ItemStack stack)
    {
        return getFluidStack(stack).getFluid().getLocalizedName();
    }

    @Override
    public String getItemDisplayName(ItemStack stack)
    {
        return "Frozen Fluid: " + getFluidName(stack);
    }
}

