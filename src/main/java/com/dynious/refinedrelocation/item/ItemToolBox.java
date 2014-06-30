package com.dynious.refinedrelocation.item;

import codechicken.lib.raytracer.RayTracer;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Optional;
import static mcp.mobius.waila.api.SpecialChars.*;
import mcp.mobius.waila.utils.ModIdentification;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumMovingObjectType;
import net.minecraft.util.Icon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.List;

public class ItemToolBox extends Item
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
        this.setContainerItem(this);
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

                if (!world.isRemote)
                {
                    String wrenchText = StatCollector.translateToLocal(Strings.TOOLBOX_WRENCH_LIST_START) + " ";
                    ArrayList<ItemStack> wrenches = getWrenches(stack);

                    for (int i = 0; i < wrenches.size(); i++)
                    {
                        ItemStack wrench = wrenches.get(i);
                        ItemStack currentWrench = getCurrentWrench(stack);
                        String modifier = wrench.itemID == currentWrench.itemID ? "\u00A77" : ""; // If current wrench, print in grey

                        wrenchText += modifier + wrench.getDisplayName() + "\u00A7r"; // reset after item name

                        if (i < wrenches.size() - 1)
                            wrenchText += ", ";
                        else
                            wrenchText += ".";
                    }

                    player.sendChatToPlayer(new ChatMessageComponent()
                        .addText(wrenchText));
                }
            }
        }
        return stack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (!stack.hasTagCompound() || world.isRemote) return false;
        NBTTagCompound stackCompound = stack.getTagCompound();

        NBTTagList list = stackCompound.getTagList("wrenches");
        byte index = stackCompound.getByte("index");
        if (list.tagCount() > index)
        {
            NBTTagCompound compound = (NBTTagCompound) list.tagAt(index);
            ItemStack wrenchStack = ItemStack.loadItemStackFromNBT(compound);
            Block block = Block.blocksList[world.getBlockId(x, y, z)];
            if (block != null)
            {
                player.inventory.mainInventory[player.inventory.currentItem] = wrenchStack;
                player.inventoryContainer.detectAndSendChanges();
                if (!wrenchStack.getItem().onItemUseFirst(wrenchStack, player, world, x, y, z, side, hitX, hitY, hitZ))
                {
                    block.onBlockActivated(world, x, y, z, player, side, hitX, hitY, hitZ);
                }
                addWrenchAtIndex(stack, player.getCurrentEquippedItem(), index);
                player.inventory.mainInventory[player.inventory.currentItem] = stack;
                player.inventoryContainer.detectAndSendChanges();
                return true;
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
            String modName = Mods.IS_WAILA_LOADED ? " (" + BLUE + ITALIC + ModIdentification.nameFromStack(wrench) + RESET + GRAY + ")" : "";
            list.add(wrench.getDisplayName() + modName);
        }
        String[] info = StatCollector.translateToLocal(Strings.TOOLBOX_INFO).split("\\\\n");
        for (String line : info)
        {
            list.add("\u00A7f" + line);
        }
    }

    @Override
    public ItemStack getContainerItemStack(ItemStack itemStack)
    {
        ItemStack copiedStack = itemStack.copy();
        if (copiedStack.hasTagCompound())
        {
            NBTTagList list = copiedStack.getTagCompound().getTagList("wrenches");
            byte index = copiedStack.getTagCompound().getByte("index");
            if (list.tagCount() > index)
            {
                list.removeTag(index);
                index--;
                if (index < 0)
                    index = 0;
                copiedStack.getTagCompound().setByte("index", index);
            }
        }
        return copiedStack;
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
        return wrench == null ? super.getDisplayDamage(stack) : wrench.getItem().getDisplayDamage(wrench);
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

    public static ArrayList<ItemStack> getWrenches(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            ArrayList<ItemStack> wrenches = new ArrayList<ItemStack>();
            NBTTagList list = stack.getTagCompound().getTagList("wrenches");
            for (int x = 0; x < list.tagCount(); x++)
            {
                NBTTagCompound nbttagcompound1 = (NBTTagCompound) list.tagAt(x);
                wrenches.add(ItemStack.loadItemStackFromNBT(nbttagcompound1));
            }
            return wrenches;
        }
        return null;
    }

    public static void addWrenchAtIndex(ItemStack stack, ItemStack wrench, byte index)
    {
        if (wrench == null || wrench.stackSize == 0)
        {
            removeWrenchAtIndex(stack, index);
            return;
        }
        if (stack.hasTagCompound())
        {
            NBTTagList list = stack.getTagCompound().getTagList("wrenches");
            if (list.tagCount() > index)
            {
                NBTTagCompound compound = (NBTTagCompound) list.tagAt(index);
                wrench.writeToNBT(compound);
            }
        }
    }

    public static void removeWrenchAtIndex(ItemStack stack, byte index)
    {
        if (stack.hasTagCompound())
        {
            NBTTagList list = stack.getTagCompound().getTagList("wrenches");
            if (list.tagCount() > index)
            {
                list.removeTag(index);
                index--;
                if (index < 0)
                    index = 0;
                stack.getTagCompound().setByte("index", index);
            }
        }
    }

    public static void addToolboxClass(Class clazz)
    {
        if (clazz != null && !WRENCH_CLASSES.contains(clazz))
            WRENCH_CLASSES.add(clazz);
    }
}
