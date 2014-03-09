package com.dynious.refinedrelocation.tileentity;

import com.dynious.refinedrelocation.api.Filter;
import com.dynious.refinedrelocation.api.IFilterTile;
import cpw.mods.fml.common.Optional;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import org.apache.commons.lang3.ArrayUtils;

public class TileFilteredBlockExtender extends TileBlockExtender implements IFilterTile
{
    private Filter filter = new Filter();

    @Override
    public boolean canInsertItem(int i, ItemStack itemStack, int i2)
    {
        return super.canInsertItem(i, itemStack, connectedDirection.getOpposite().ordinal()) && filter.passesFilter(itemStack);
    }

    /*
    ComputerCraft interaction
    */

    /*

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public String getType()
    {
        return "filtered_extender";
    }

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public String[] getMethodNames()
    {
        return ArrayUtils.addAll(super.getMethodNames(), "isFilterEnabled", "setFilterEnabled");
    }

    @Optional.Method(modid = "ComputerCraft")
    @Override
    public Object[] callMethod(IComputerAccess computer, ILuaContext context, int method, Object[] arguments) throws Exception
    {
        Object[] superArr = super.callMethod(computer, context, method, arguments);
        if (superArr != null)
        {
            return superArr;
        }
        switch (method)
        {
            case 2:
                if (arguments.length > 0 && arguments[0] instanceof Double)
                {
                    double arg = (Double) arguments[0];
                    if (arg >= 0 && arg < filter.getSize())
                    {
                        return new Boolean[]{filter.getValue((int) arg)};
                    }
                    return null;
                }
            case 3:
                if (arguments.length > 1 && arguments[0] instanceof Double && arguments[1] instanceof Boolean)
                {
                    double arg = (Double) arguments[0];
                    if (arg >= 0 && arg < filter.getSize())
                    {
                        filter.setValue((int) arg, (Boolean) arguments[1]);
                        return new Boolean[]{true};
                    }
                }
                return new Boolean[]{false};
        }
        return null;
    }

    */

    @Override
    public Filter getFilter()
    {
        return filter;
    }

    @Override
    public void writeToNBT(NBTTagCompound compound)
    {
        super.writeToNBT(compound);
        filter.writeToNBT(compound);
    }

    @Override
    public void readFromNBT(NBTTagCompound compound)
    {
        super.readFromNBT(compound);
        filter.readFromNBT(compound);
    }
}
