package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.repack.codechicken.lib.raytracer.RayTracer;
import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.lib.Mods;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Strings;
import ic2.api.item.ElectricItem;
import mcp.mobius.waila.utils.ModIdentification;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import org.lwjgl.input.Keyboard;

import java.util.ArrayList;
import java.util.List;

import static mcp.mobius.waila.api.SpecialChars.*;

public class ItemToolBox extends Item {

    private static final String[] WRENCH_CLASS_NAMES = new String[]{
            "ic2.core.item.tool.ItemToolWrench",
            "ic2.core.item.tool.ItemToolMeter",
            "buildcraft.api.tools.IToolWrench",
            "appeng.api.implementations.items.IAEWrench",
            "thermalexpansion.item.tool.ItemWrench",
            "com.carpentersblocks.api.ICarpentersChisel",
            "com.carpentersblocks.api.ICarpentersHammer",
            "com.iconmaster.aec.item.ItemAetometer",
            "factorization.charge.ItemChargeMeter",
            "mekanism.common.item.ItemConfigurator",
            "mekanism.common.item.ItemNetworkReader",
            "mrtjp.projectred.api.IScrewdriver",
            "com.yogpc.qp.ItemTool",
            "redstonearsenal.item.tool.ItemWrenchRF",
            "redstonearsenal.item.tool.ItemWrenchBattleRF",
            "aroma1997.core.items.wrench.ItemWrench"
    };

    public static void addToolboxClass(Class clazz) {
        if (clazz != null && !wrenchClasses.contains(clazz)) {
            wrenchClasses.add(clazz);
        }
    }

    private static final List<Class<?>> wrenchClasses = new ArrayList<>();
    private IIcon unknownIcon;
    private IIcon transparentIcon;

    public ItemToolBox() {
        super();
        setUnlocalizedName(Names.toolbox);
        setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        setMaxStackSize(1);
        setContainerItem(this);
        for (String className : WRENCH_CLASS_NAMES) {
            try {
                wrenchClasses.add(Class.forName(className));
            } catch (ClassNotFoundException ignored) {
            }
        }
    }

    public boolean isItemWrench(Item item) {
        for (Class<?> clazz : wrenchClasses) {
            if (clazz.isAssignableFrom(item.getClass())) {
                return true;
            }
        }
        return false;
    }

    public boolean doesToolBoxContainWrench(NBTTagCompound toolBoxCompound, ItemStack wrenchStack) {
        NBTTagList tagList = toolBoxCompound.getTagList("wrenches", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < tagList.tagCount(); i++) {
            NBTTagCompound tagCompound = tagList.getCompoundTagAt(i);
            ItemStack itemStack = ItemStack.loadItemStackFromNBT(tagCompound);
            if (itemStack.getHasSubtypes() ? itemStack.isItemEqual(wrenchStack) : itemStack.getItem() == wrenchStack.getItem()) {
                return true;
            }
        }
        return false;
    }

    public ItemStack getCurrentWrench(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            NBTTagList tagList = itemStack.getTagCompound().getTagList("wrenches", Constants.NBT.TAG_COMPOUND);
            int index = itemStack.getTagCompound().getByte("index");
            if (tagList.tagCount() > index) {
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(index);
                return ItemStack.loadItemStackFromNBT(tagCompound);
            }
        }
        return null;
    }

    public ArrayList<ItemStack> getWrenches(ItemStack itemStack) {
        if (itemStack.hasTagCompound()) {
            ArrayList<ItemStack> wrenches = new ArrayList<>();
            NBTTagList list = itemStack.getTagCompound().getTagList("wrenches", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < list.tagCount(); i++) {
                NBTTagCompound tagCompound = list.getCompoundTagAt(i);
                wrenches.add(ItemStack.loadItemStackFromNBT(tagCompound));
            }
            return wrenches;
        }
        return null;
    }

    public void addWrenchAtIndex(ItemStack itemStack, ItemStack wrenchStack, int index) {
        if (wrenchStack == null || wrenchStack.stackSize == 0) {
            removeWrenchAtIndex(itemStack, index);
            return;
        }
        if (itemStack.hasTagCompound()) {
            NBTTagList tagList = itemStack.getTagCompound().getTagList("wrenches", Constants.NBT.TAG_COMPOUND);
            if (tagList.tagCount() > index) {
                NBTTagCompound tagCompound = tagList.getCompoundTagAt(index);
                wrenchStack.writeToNBT(tagCompound);
            }
        }
    }

    public void removeWrenchAtIndex(ItemStack itemStack, int index) {
        if (itemStack.hasTagCompound()) {
            NBTTagList tagList = itemStack.getTagCompound().getTagList("wrenches", Constants.NBT.TAG_COMPOUND);
            if (tagList.tagCount() > index) {
                tagList.removeTag(index);
                index--;
                if (index < 0) {
                    index = 0;
                }
                itemStack.getTagCompound().setByte("index", (byte) index);
            }
        }
    }

    @Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
        ItemStack wrenchStack = getCurrentWrench(player.getHeldItem());
        return wrenchStack != null && wrenchStack.getItem().doesSneakBypassUse(world, x, y, z, player);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer entityPlayer) {
        MovingObjectPosition mop = RayTracer.reTrace(world, entityPlayer);
        if (!itemStack.hasTagCompound()) {
            NBTTagCompound tagCompound = new NBTTagCompound();
            tagCompound.setTag("wrenches", new NBTTagList());
            tagCompound.setByte("index", (byte) 0);
            itemStack.setTagCompound(tagCompound);
        }
        if (entityPlayer.isSneaking()) {
            ItemStack[] mainInventory = entityPlayer.inventory.mainInventory;
            for (int i = 0; i < mainInventory.length; i++) {
                ItemStack invStack = mainInventory[i];
                if (invStack != null && isItemWrench(invStack.getItem()) && !doesToolBoxContainWrench(itemStack.getTagCompound(), invStack)) {
                    NBTTagList tagList = itemStack.getTagCompound().getTagList("wrenches", Constants.NBT.TAG_COMPOUND);
                    NBTTagCompound tagCompound = new NBTTagCompound();
                    ItemStack wrenchStack = invStack.splitStack(1);
                    wrenchStack.writeToNBT(tagCompound);
                    tagList.appendTag(tagCompound);
                    if (invStack.stackSize == 0) {
                        entityPlayer.inventory.setInventorySlotContents(i, null);
                    }
                }
            }
            if (mop == null || mop.typeOfHit != MovingObjectPosition.MovingObjectType.BLOCK) {
                int index = itemStack.getTagCompound().getByte("index");
                index++;
                if (index >= itemStack.getTagCompound().getTagList("wrenches", Constants.NBT.TAG_COMPOUND).tagCount()) {
                    index = 0;
                }
                itemStack.getTagCompound().setByte("index", (byte) index);

                if (!world.isRemote) {
                    ArrayList<ItemStack> wrenches = getWrenches(itemStack);
                    if (!wrenches.isEmpty()) {
                        ItemStack currentWrench = getCurrentWrench(itemStack);
                        StringBuilder sb = new StringBuilder(StatCollector.translateToLocal(Strings.TOOLBOX_WRENCH_LIST_START)).append(" ");
                        for (int i = 0; i < wrenches.size(); i++) {
                            if (i > 0) {
                                sb.append(".");
                            }
                            ItemStack wrenchStack = wrenches.get(i);
                            if (wrenchStack.getHasSubtypes() ? wrenchStack.isItemEqual(currentWrench) : wrenchStack.getItem() == currentWrench.getItem()) {
                                sb.append("\u00a7a");
                            }
                            sb.append(wrenchStack.getDisplayName()).append("\u00a7r");
                        }
                        sb.append(".");
                        entityPlayer.addChatMessage(new ChatComponentText(sb.toString()));
                    }
                }
            }
        }
        return itemStack;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!itemStack.hasTagCompound()) {
            return false;
        }
        NBTTagCompound tagCompound = itemStack.getTagCompound();
        int index = tagCompound.getByte("index");
        ItemStack wrenchStack = getCurrentWrench(itemStack);
        if (wrenchStack != null) {
            if (wrenchStack.getItem().onItemUseFirst(itemStack, entityPlayer, world, x, y, z, side, hitX, hitY, hitZ)) {
                return true;
            }
            Block block = world.getBlock(x, y, z);
            if (block != null) {
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = wrenchStack;
                if (!wrenchStack.getItem().onItemUseFirst(wrenchStack, entityPlayer, world, x, y, z, side, hitX, hitY, hitZ)) {
                    block.onBlockActivated(world, x, y, z, entityPlayer, side, hitX, hitY, hitZ);
                }
                addWrenchAtIndex(itemStack, entityPlayer.getCurrentEquippedItem(), index);
                entityPlayer.inventory.mainInventory[entityPlayer.inventory.currentItem] = itemStack;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer entityPlayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!itemStack.hasTagCompound()) {
            return false;
        }
        ItemStack wrenchStack = getCurrentWrench(itemStack);
        return wrenchStack != null && wrenchStack.getItem().onItemUse(itemStack, entityPlayer, world, x, y, z, side, hitX, hitY, hitZ);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer entityPlayer, List list, boolean flag) {
        ItemStack wrenchStack = getCurrentWrench(itemStack);
        if (wrenchStack != null) {
            String modName = Mods.IS_WAILA_LOADED ? " (" + BLUE + ITALIC + ModIdentification.nameFromStack(wrenchStack) + RESET + GRAY + ")" : "";
            list.add(wrenchStack.getDisplayName() + modName);

            wrenchStack.getItem().addInformation(wrenchStack, entityPlayer, list, flag);

            if (Mods.IS_IC2_LOADED) {
                String charge = ElectricItem.manager.getToolTip(wrenchStack);
                if (charge != null) {
                    list.add(charge);
                }
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) || Keyboard.isKeyDown(Keyboard.KEY_RSHIFT)) {
            String[] tooltipLines = StatCollector.translateToLocal(Strings.TOOLBOX_INFO).split("\\\\n");
            for (String s : tooltipLines) {
                list.add("\u00a73" + s);
            }
        } else {
            list.add("\u00a76" + StatCollector.translateToLocal(Strings.TOOLTIP_SHIFT));
        }
    }

    @Override
    public ItemStack getContainerItem(ItemStack itemStack) {
        ItemStack copiedStack = itemStack.copy();
        if (copiedStack.hasTagCompound()) {
            NBTTagList list = copiedStack.getTagCompound().getTagList("wrenches", Constants.NBT.TAG_COMPOUND);
            int index = copiedStack.getTagCompound().getByte("index");
            if (index < list.tagCount()) {
                list.removeTag(index);
                index--;
                if (index < 0) {
                    index = 0;
                }
                copiedStack.getTagCompound().setByte("index", (byte) index);
            }
        }
        return copiedStack;
    }

    @Override
    public int getDamage(ItemStack stack) {
        ItemStack wrenchStack = getCurrentWrench(stack);
        return wrenchStack == null ? super.getDamage(stack) : wrenchStack.getItem().getDamage(wrenchStack);
    }

    @Override
    public int getDisplayDamage(ItemStack stack) {
        ItemStack wrenchStack = getCurrentWrench(stack);
        return wrenchStack == null ? super.getDisplayDamage(stack) : wrenchStack.getItem().getDisplayDamage(wrenchStack);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        ItemStack wrenchStack = getCurrentWrench(stack);
        return wrenchStack == null ? super.getMaxDamage(stack) : wrenchStack.getItem().getMaxDamage(wrenchStack);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        ItemStack wrenchStack = getCurrentWrench(stack);
        return wrenchStack == null ? super.isDamaged(stack) : wrenchStack.getItem().isDamaged(wrenchStack);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        ItemStack wrenchStack = getCurrentWrench(stack);
        if (wrenchStack != null) {
            wrenchStack.getItem().setDamage(wrenchStack, damage);
        }
    }

    @Override
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @Override
    public IIcon getIcon(ItemStack stack, int pass) {
        if (pass == 0) {
            return itemIcon;
        } else if (pass == 1) {
            ItemStack wrench = getCurrentWrench(stack);
            IIcon wrenchIcon = wrench != null ? wrench.getItem().getIcon(wrench, 0) : null;
            if (wrenchIcon != null) {
                return wrenchIcon;
            } else {
                return unknownIcon;
            }
        }
        return transparentIcon;
    }

    @Override
    public void registerIcons(IIconRegister register) {
        itemIcon = register.registerIcon(Resources.MOD_ID + ":" + Names.toolbox);
        unknownIcon = register.registerIcon(Resources.MOD_ID + ":" + "unknown");
        transparentIcon = register.registerIcon(Resources.MOD_ID + ":" + "transparent");
    }
}
