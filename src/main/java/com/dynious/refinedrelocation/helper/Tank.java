package com.dynious.refinedrelocation.helper;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class Tank extends FluidTank
{
	public Tank(int capacity, TileEntity tile)
	{
		super(capacity);
		this.tile = tile;
	}

	public boolean isEmpty() {
		return getFluid() == null || getFluid().amount <= 0;
	}

	public boolean isFull() {
		return getFluid() != null && getFluid().amount >= getCapacity();
	}

	public Fluid getFluidType() {
		return getFluid() != null ? getFluid().getFluid() : null;
	}

	// @Override
	// public void setFluid(FluidStack fluid)
	// {
		// if (fluid == null)
		// {
		// 	super.setFluid(null);
		// }
		// else if (getFluid() == null || getFluid().fluidID == fluid.fluidID)
		// {
		// 	if (getFluid().amount + fluid.amount <= getCapacity())
		// 	{
		// 		FluidStack fluidCopy = fluid.copy();
		// 		fluidCopy.amount = getFluid().amount + fluid.amount;
		// 		super.setFluid(fluidCopy);
		// 	}
		// }
	// }

	public void clearFluid()
	{
		setFluid(null);
	}

	@Override
	public final NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		if (getFluid() != null)
		{
			nbt.setInteger("fluidID", getFluid().fluidID);
			nbt.setInteger("fluidAmount", getFluidAmount());
			nbt.setInteger("capacity", getCapacity());
		}

		return nbt;
	}

	@Override
	public final FluidTank readFromNBT(NBTTagCompound nbt) {
		this.setFluid(new FluidStack(nbt.getInteger("fluidID"), nbt.getInteger("fluidAmount")));
		this.setCapacity(nbt.getInteger("capacity"));
		return this;
	}
}
