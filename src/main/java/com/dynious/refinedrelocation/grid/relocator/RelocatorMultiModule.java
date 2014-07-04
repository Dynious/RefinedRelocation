package com.dynious.refinedrelocation.grid.relocator;

import com.dynious.refinedrelocation.APIHandler;
import com.dynious.refinedrelocation.api.APIUtils;
import com.dynious.refinedrelocation.api.item.IItemRelocatorModule;
import com.dynious.refinedrelocation.api.relocator.IItemRelocator;
import com.dynious.refinedrelocation.api.relocator.IRelocatorModule;
import com.dynious.refinedrelocation.api.relocator.RelocatorModuleBase;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleRegistry;
import com.dynious.refinedrelocation.gui.GuiHome;
import com.dynious.refinedrelocation.gui.GuiModularTest;
import com.dynious.refinedrelocation.gui.container.ContainerMultiModule;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.tileentity.IRelocator;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;

import java.util.ArrayList;
import java.util.List;

public class RelocatorMultiModule extends RelocatorModuleBase
{
    private static IIcon icon;
    private List<IRelocatorModule> modules = new ArrayList<IRelocatorModule>();
    private int currentModule = -1; // -1 is the multi module

    public void setCurrentModule(int newModule)
    {
        currentModule = newModule;
    }

    public IRelocatorModule getCurrentModule()
    {
        return currentModule == -1 ? this : modules.get(currentModule);
    }

    public boolean addModule(IRelocatorModule module)
    {
        for (IRelocatorModule module1 : modules)
        {
            if (module1.getClass() == module.getClass() || module instanceof RelocatorMultiModule)
                return false;
        }
        modules.add(module);
        return true;
    }

    @Override
    public void init(IItemRelocator relocator, int side)
    {
        for (IRelocatorModule module : modules)
        {
            if (module != null)
                module.init(relocator, side);
        }
    }

    @Override
    public boolean onActivated(IItemRelocator relocator, EntityPlayer player, int side, ItemStack stack)
    {
        if (stack != null && stack.getItem() instanceof IItemRelocatorModule)
        {
            IRelocatorModule module = ((IItemRelocatorModule) stack.getItem()).getRelocatorModule(stack);
            if (module != null)
                return addModule(module);
        }
        APIUtils.openRelocatorModuleGUI(relocator, player, side);
        return true;
    }

    @Override
    public void onUpdate(IItemRelocator relocator, int side)
    {
        for (IRelocatorModule module : modules)
        {
            if (module != null)
                module.onUpdate(relocator, side);
        }
    }

    @Override
    public ItemStack outputToSide(IItemRelocator relocator, int side, TileEntity inventory, ItemStack stack, boolean simulate)
    {
        ItemStack returned = stack;
        for (IRelocatorModule module : modules)
        {
            if (module != null)
                returned = module.outputToSide(relocator, side, inventory, stack, simulate);
            if (returned == null)
                return null;
        }
        return returned;
    }

    @Override
    public void onRedstonePowerChange(boolean isPowered)
    {
        for (IRelocatorModule module : modules)
        {
            if (module != null)
                module.onRedstonePowerChange(isPowered);
        }
    }

    @Override
    public int strongRedstonePower(int side)
    {
        int power = 0;
        for (IRelocatorModule module : modules)
        {
            if (module != null)
            {
                int p = module.strongRedstonePower(side);
                if (p > power)
                    power = p;
            }
        }
        return power;
    }

    @Override
    public boolean connectsToRedstone()
    {
        for (IRelocatorModule module : modules)
        {
            if (module != null && module.connectsToRedstone())
                return true;
        }
        return false;
    }

    @Override
    public GuiScreen getGUI(IItemRelocator relocator, int side, EntityPlayer player)
    {
        if (currentModule == -1)
            return new GuiHome(this, modules, relocator, player, side);
        else
            return getCurrentModule().getGUI(relocator, side, player);
    }

    @Override
    public Container getContainer(IItemRelocator relocator, int side, EntityPlayer player)
    {
        if (currentModule == -1)
            return new ContainerMultiModule(this, relocator, player, side);
        else
            return getCurrentModule().getContainer(relocator, side, player);
    }

    @Override
    public boolean passesFilter(IItemRelocator relocator, int side, ItemStack stack, boolean input, boolean simulate)
    {
        for (IRelocatorModule module : modules)
        {
            if (module != null && !module.passesFilter(relocator, side, stack, input, simulate))
                return false;
        }
        return true;
    }

    @Override
    public void readFromNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        NBTTagList list = compound.getTagList("multiModules", 10);
        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound compound1 = list.getCompoundTagAt(i);
            IRelocatorModule module = RelocatorModuleRegistry.getModule(compound1.getString("clazzIdentifier"));
            if (module != null)
            {
                modules.add(module);
                module.init(relocator, side);
                module.readFromNBT(relocator, side, compound1);
            }
        }
    }

    @Override
    public void writeToNBT(IItemRelocator relocator, int side, NBTTagCompound compound)
    {
        NBTTagList list = new NBTTagList();
        for (IRelocatorModule module : modules)
        {
            if (module != null)
            {
                NBTTagCompound compound1 = new NBTTagCompound();
                compound1.setString("clazzIdentifier", RelocatorModuleRegistry.getIdentifier(module.getClass()));
                module.writeToNBT(relocator, side, compound1);
                list.appendTag(compound1);
            }
        }
        compound.setTag("multiModules", list);
    }

    @Override
    public List<ItemStack> getDrops(IItemRelocator relocator, int side)
    {
        List<ItemStack> drops = new ArrayList<ItemStack>();
        for (IRelocatorModule module : modules)
        {
            if (module != null)
                drops.addAll(module.getDrops(relocator, side));
        }
        return drops;
    }

    @Override
    public IIcon getIcon(IItemRelocator relocator, int side)
    {
        return icon;
    }

    @Override
    public void registerIcons(IIconRegister register)
    {
        icon = register.registerIcon(Resources.MOD_ID + ":" + "relocatorModuleMulti");
    }
}
