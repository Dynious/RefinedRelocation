package com.dynious.refinedrelocation.item;

import codechicken.lib.raytracer.RayTracer;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import cpw.mods.fml.common.Optional;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

@Optional.Interface(iface = "ic2.api.item.IElectricItem", modid = Mods.IC2_ID)
public class ItemToolBox extends Item //implements IElectricItem
{
    private static final String[] WRENCH_CLASS_NAMES;
    private static final List<Class<?>> WRENCH_CLASSES;
    private static Icon transparent;

    static
    {
        WRENCH_CLASS_NAMES = new String[] { "ic2.core.item.tool.ItemToolWrench", "buildcraft.api.tools.IToolWrench" };
        WRENCH_CLASSES = new ArrayList<Class<?>>();
        for (String className : WRENCH_CLASS_NAMES)
        {
            try
            {
                WRENCH_CLASSES.add(Class.forName(className));
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
    }

    public ItemToolBox(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.toolbox);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        this.setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        MovingObjectPosition mop = RayTracer.reTrace(world, player);
        if (mop == null || mop.typeOfHit != EnumMovingObjectType.TILE)
        {
            if (!stack.hasTagCompound())
            {
                NBTTagCompound compound = new NBTTagCompound();
                compound.setTag("wrenches", new NBTTagList());
                compound.setByte("index", (byte) 0);
                stack.setTagCompound(compound);
            }
            if (player.isSneaking())
            {
                ItemStack[] mainInventory = player.inventory.mainInventory;
                for (int i = 0; i < mainInventory.length; i++)
                {
                    ItemStack invStack = mainInventory[i];
                    if (invStack != null && isItemWrench(invStack.getItem()) && !doesToolBoxContainWrench(stack.getTagCompound(), invStack.getItem()))
                    {
                        NBTTagList list = stack.getTagCompound().getTagList("wrenches");
                        NBTTagCompound newTag = new NBTTagCompound();
                        ItemStack wrenchStack = invStack.splitStack(1);
                        wrenchStack.writeToNBT(newTag);
                        list.appendTag(newTag);
                        if (invStack.stackSize == 0)
                            player.inventory.setInventorySlotContents(i, null);
                    }
                }

                byte index = stack.getTagCompound().getByte("index");
                index++;
                if (index >= stack.getTagCompound().getTagList("wrenches").tagCount())
                    index = 0;
                stack.getTagCompound().setByte("index", index);
            }
        }
        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        NBTTagList list = stack.getTagCompound().getTagList("wrenches");
        byte index = stack.getTagCompound().getByte("index");
        if (list.tagCount() > index)
        {
            NBTTagCompound compound = (NBTTagCompound) list.tagAt(index);
            ItemStack wrenchStack = ItemStack.loadItemStackFromNBT(compound);
            Block block = Block.blocksList[world.getBlockId(x, y, z)];
            if (block != null)
            {
                player.inventory.mainInventory[player.inventory.currentItem] = wrenchStack;
                if (!wrenchStack.getItem().onItemUseFirst(wrenchStack, player, world, x, y, z, side, hitX, hitY, hitZ))
                {
                    block.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
                    if (player.getCurrentEquippedItem() == null)
                    {
                        list.removeTag(index);
                        index--;
                        stack.getTagCompound().setByte("index", index);
                    }
                }
                player.inventory.mainInventory[player.inventory.currentItem] = stack;
                if (!world.isRemote) return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool)
    {
        ItemStack wrench = getCurrentWrench(stack);
        if (wrench != null)
        {
            list.add(wrench.getDisplayName());
        }
        String[] info = StatCollector.translateToLocal(Strings.TOOLBOX_INFO).split("\\\\n");
        for (String line : info)
        {
            list.add("\u00A7f" + line);
        }
    }

    /*
    Damage Stuffs
     */

    @Override
    public int getDamage(ItemStack stack)
    {
        ItemStack wrench = getCurrentWrench(stack);
        return wrench == null ? super.getDamage(stack) : wrench.getItem().getDamage(wrench);
    }

    @Override
    public int getDisplayDamage(ItemStack stack)
    {
        ItemStack wrench = getCurrentWrench(stack);
        return getDamage(stack);
        //return wrench == null ? super.getDisplayDamage(stack) : wrench.getItem().getDisplayDamage(wrench);
    }

    @Override
    public int getMaxDamage(ItemStack stack)
    {
        ItemStack wrench = getCurrentWrench(stack);
        return wrench == null ? super.getMaxDamage(stack) : wrench.getItem().getMaxDamage(wrench);
    }

    @Override
    public boolean isDamaged(ItemStack stack)
    {
        ItemStack wrench = getCurrentWrench(stack);
        return wrench == null ? super.isDamaged(stack) : wrench.getItem().isDamaged(wrench);
    }

    @Override
    public void setDamage(ItemStack stack, int damage)
    {
        ItemStack wrench = getCurrentWrench(stack);
        if (wrench != null) wrench.getItem().setDamage(wrench, damage);
    }

    /*
    Icon stuffs
     */

    @Override
    public boolean requiresMultipleRenderPasses()
    {
        return true;
    }

    @Override
    public Icon getIcon(ItemStack stack, int pass)
    {
        if (pass == 0)
        {
            return itemIcon;
        }
        else if (pass == 1)
        {
            ItemStack wrench = getCurrentWrench(stack);
            if (wrench != null)
            {
                return wrench.getItem().getIcon(wrench, 0);
            }
        }
        return transparent;
    }

    @Override
    public void registerIcons(IconRegister register)
    {
        itemIcon = register.registerIcon(Resources.MOD_ID + ":" + Names.toolbox);
        transparent = register.registerIcon(Resources.MOD_ID + ":" + "transparent");
    }

    public static boolean isItemWrench(Item item)
    {
        for (Class<?> clazz : WRENCH_CLASSES)
        {
            if (clazz.isAssignableFrom(item.getClass()) || ArrayUtils.contains(item.getClass().getInterfaces(), clazz))
                return true;
        }
        return false;
    }

    public static boolean doesToolBoxContainWrench(NBTTagCompound toolBoxCompound, Item wrench)
    {
        NBTTagList list = toolBoxCompound.getTagList("wrenches");
        for (int i = 0; i < list.tagCount(); i++)
        {
            NBTTagCompound compound = (NBTTagCompound) list.tagAt(i);
            ItemStack stack = ItemStack.loadItemStackFromNBT(compound);
            if (stack.getItem() == wrench)
                return true;
        }
        return false;
    }

    public static ItemStack getCurrentWrench(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            NBTTagList list = stack.getTagCompound().getTagList("wrenches");
            byte index = stack.getTagCompound().getByte("index");
            if (list.tagCount() > index)
            {
                NBTTagCompound compound = (NBTTagCompound) list.tagAt(index);
                return ItemStack.loadItemStackFromNBT(compound);
            }
        }
        return null;
    }

    /*

    @Override
    @Optional.Method(modid = Mods.IC2_ID)
    public boolean canProvideEnergy(ItemStack stack)
    {
        ItemStack wrench = getCurrentWrench(stack);
        if (wrench != null)
        {
            if (wrench.getItem() instanceof IElectricItem)
            {
                return ((IElectricItem)wrench.getItem()).canProvideEnergy(wrench);
            }
        }
        return false;
    }

    @Override
    @Optional.Method(modid = Mods.IC2_ID)
    public int getChargedItemId(ItemStack stack)
    {
        ItemStack wrench = getCurrentWrench(stack);
        if (wrench != null)
        {
            if (wrench.getItem() instanceof IElectricItem)
            {
                return ((IElectricItem)wrench.getItem()).getChargedItemId(wrench);
            }
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = Mods.IC2_ID)
    public int getEmptyItemId(ItemStack stack)
    {
        ItemStack wrench = getCurrentWrench(stack);
        if (wrench != null)
        {
            if (wrench.getItem() instanceof IElectricItem)
            {
                return ((IElectricItem)wrench.getItem()).getEmptyItemId(wrench);
            }
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = Mods.IC2_ID)
    public int getMaxCharge(ItemStack stack)
    {
        ItemStack wrench = getCurrentWrench(stack);
        if (wrench != null)
        {
            if (wrench.getItem() instanceof IElectricItem)
            {
                return ((IElectricItem)wrench.getItem()).getMaxCharge(wrench);
            }
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = Mods.IC2_ID)
    public int getTier(ItemStack stack)
    {
        ItemStack wrench = getCurrentWrench(stack);
        if (wrench != null)
        {
            if (wrench.getItem() instanceof IElectricItem)
            {
                return ((IElectricItem)wrench.getItem()).getTier(wrench);
            }
        }
        return 0;
    }

    @Override
    @Optional.Method(modid = Mods.IC2_ID)
    public int getTransferLimit(ItemStack stack)
    {
        ItemStack wrench = getCurrentWrench(stack);
        if (wrench != null)
        {
            if (wrench.getItem() instanceof IElectricItem)
            {
                return ((IElectricItem)wrench.getItem()).getTransferLimit(wrench);
            }
        }
        return 0;
    }

    */
}
