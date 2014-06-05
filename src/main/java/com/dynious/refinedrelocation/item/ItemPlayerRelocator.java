package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.helper.MiscHelper;
import com.dynious.refinedrelocation.helper.ParticleHelper;
import com.dynious.refinedrelocation.lib.*;
import com.dynious.refinedrelocation.tileentity.TileRelocationController;
import com.dynious.refinedrelocation.tileentity.TileRelocationPortal;
import com.dynious.refinedrelocation.util.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.DimensionManager;
import org.apache.commons.lang3.ArrayUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;
import java.util.UUID;

public class ItemPlayerRelocator extends Item
{
    public static final String UUID_TAG = "UUID";
    public static final String DIMENSION_TAG = "dimId";
    public static final String INTER_LINK_TAG = "interLink";
    public static final String TIME = "time";

    public ItemPlayerRelocator()
    {
        super();
        this.setUnlocalizedName(Names.playerRelocator);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
        this.setMaxStackSize(1);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
            return false;

        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocationController)
        {
            if (((TileRelocationController) tile).isFormed(true))
            {
                if (!stack.hasTagCompound() || !stack.getTagCompound().hasKey(UUID_TAG))
                {
                    stack.setTagCompound(new NBTTagCompound());
                    stack.getTagCompound().setString(UUID_TAG, UUID.randomUUID().toString());
                }
                if (((TileRelocationController) tile).isIntraLinker())
                {
                    stack.getTagCompound().setBoolean(INTER_LINK_TAG, true);
                }
                else
                {
                    stack.getTagCompound().setBoolean(INTER_LINK_TAG, false);
                }
                stack.getTagCompound().setInteger(DIMENSION_TAG, tile.getWorldObj().provider.dimensionId);
                stack.getTagCompound().setInteger("x", x);
                stack.getTagCompound().setInteger("y", y);
                stack.getTagCompound().setInteger("z", z);
                ((TileRelocationController) tile).setLinkedUUID(stack.getTagCompound().getString(UUID_TAG));
            }
            return true;
        }
        return false;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack par1ItemStack)
    {
        return EnumAction.block;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack par1ItemStack)
    {
        return 45;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("x") && getTimeDifference(stack)/1000 > (player.capabilities.isCreativeMode ? 1 : Settings.PLAYER_RELOCATOR_COOLDOWN))
        {
            player.setItemInUse(stack, getMaxItemUseDuration(stack));
            world.playSoundAtEntity(player, Sounds.ambiance, 1F, 1F);
            ParticleHelper.spawnParticlesInCircle("portal", 2.0F, 100, world, player.posX, player.posY - 0.5F, player.posZ, true);
        }
        return stack;
    }

    @Override
    public ItemStack onEaten(ItemStack stack, World world, EntityPlayer player)
    {
        if (world.isRemote)
        {
            return stack;
        }
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("x"))
        {
            World world2 = MinecraftServer.getServer().worldServerForDimension(stack.getTagCompound().getInteger(DIMENSION_TAG));
            if (world2 != null && (world == world2 || stack.getTagCompound().getBoolean(INTER_LINK_TAG)))
            {
                TileEntity connectedTile = world2.getTileEntity(stack.getTagCompound().getInteger("x"), stack.getTagCompound().getInteger("y"), stack.getTagCompound().getInteger("z"));
                if (connectedTile != null && connectedTile instanceof TileRelocationController && ((TileRelocationController) connectedTile).isFormed(true))
                {
                    if ((world != world2 && !((TileRelocationController) connectedTile).isIntraLinker()) || ArrayUtils.contains(Settings.PLAYER_RELOCATOR_DISABLED_AGES, world.provider.dimensionId))
                    {
                        return stack;
                    }
                    if (!stack.getTagCompound().getString(UUID_TAG).equals(((TileRelocationController) connectedTile).getLinkedUUID()))
                    {
                        stack.getTagCompound().removeTag(UUID_TAG);
                        return stack;
                    }
                    int xPos = MathHelper.floor_double(player.posX);
                    int yPos = MathHelper.floor_double(player.posY);
                    int zPos = MathHelper.floor_double(player.posZ);

                    if (checkBlocks(world, xPos, yPos, zPos))
                    {
                        player.setPositionAndUpdate(xPos + 0.5D, yPos, zPos + 0.5D);

                        setBlockToPortal(world, xPos, yPos - 1, zPos);
                        setBlockToPortal(world, xPos, yPos - 2, zPos);
                        if (stack.getTagCompound().hasKey(DIMENSION_TAG))
                        {
                            setLowerBlockToDimensionalPortal(world, xPos, yPos - 3, zPos, new Vector3(connectedTile.xCoord, connectedTile.yCoord, connectedTile.zCoord), stack.getTagCompound().getInteger(DIMENSION_TAG));
                        }
                        setLowerBlockToPortal(world, xPos, yPos - 3, zPos, new Vector3(connectedTile.xCoord, connectedTile.yCoord, connectedTile.zCoord));

                        world.playSoundAtEntity(player, Sounds.explosion, 1F, 1F);
                        stack.getTagCompound().setLong(TIME, System.currentTimeMillis());
                    }
                }
            }
        }
        return stack;
    }

    private boolean checkBlocks(World world, int posX, int posY, int posZ)
    {
        Block block = world.getBlock(posX, posY - 1, posZ);
        Block block1 = world.getBlock(posX, posY - 2, posZ);
        Block block2 = world.getBlock(posX, posY - 3, posZ);

        return (world.getTileEntity(posX, posY - 1, posZ) == null) && (block != Blocks.bedrock)
                && (block != ModBlocks.relocationPortal) && (block != null) && (!block.canPlaceBlockAt(world, posX, posY - 1, posZ))
                && (block.getBlockHardness(world, posX, posY - 1, posZ) != -1.0F)
                && (world.getTileEntity(posX, posY - 2, posZ) == null) && (block1 != Blocks.bedrock)
                && (block1 != ModBlocks.relocationPortal)
                && (world.getTileEntity(posX, posY - 3, posZ) == null) && (block2 != Blocks.bedrock)
                && (block2 != ModBlocks.relocationPortal);
    }

    private void setBlockToPortal(World world, int x, int y, int z)
    {
        Block block = world.getBlock(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, ModBlocks.relocationPortal);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocationPortal)
        {
            ((TileRelocationPortal)tile).init(block, blockMeta);
        }
    }

    private void setLowerBlockToPortal(World world, int x, int y, int z, Vector3 linkedPos)
    {
        Block block = world.getBlock(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, ModBlocks.relocationPortal);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocationPortal)
        {
            ((TileRelocationPortal)tile).init(block, blockMeta, linkedPos);
        }
    }

    private void setLowerBlockToDimensionalPortal(World world, int x, int y, int z, Vector3 linkedPos, int dimensionId)
    {
        Block block = world.getBlock(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, ModBlocks.relocationPortal);
        TileEntity tile = world.getTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocationPortal)
        {
            ((TileRelocationPortal)tile).init(block, blockMeta, linkedPos, dimensionId);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b)
    {
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("x"))
        {
            list.add(StatCollector.translateToLocalFormatted(Strings.LINKED_TO_AT, StatCollector.translateToLocal("tile.relocationController.name"), itemStack.getTagCompound().getInteger("x"), itemStack.getTagCompound().getInteger("y"), itemStack.getTagCompound().getInteger("z")));
            if (itemStack.getTagCompound().getBoolean(INTER_LINK_TAG))
            {
                WorldProvider p = WorldProvider.getProviderForDimension(itemStack.getTagCompound().getInteger(DIMENSION_TAG));
                String worldString = p != null ? p.getDimensionName() : "<Error>";
                list.add(StatCollector.translateToLocalFormatted(Strings.INTER_DIMENSIONAL, worldString));
            }
            if (!itemStack.getTagCompound().hasKey(UUID_TAG))
            {
                list.add("\u00A74" + StatCollector.translateToLocal(Strings.BROKEN_LINK));
            }
            if (getTimeLeft(itemStack, player) > 0)
            {
                list.add("\u00A7e" + StatCollector.translateToLocalFormatted(Strings.COOLDOWN, MiscHelper.getDurationString(getTimeLeft(itemStack, player))));
            }
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister par1IconRegister)
    {
        itemIcon = par1IconRegister.registerIcon(Resources.MOD_ID + ":"
                + Names.playerRelocator);
    }

    @SideOnly(Side.CLIENT)
    public void shiftFOV(ItemStack stack, FOVUpdateEvent event)
    {
        float inUse = stack.getMaxItemUseDuration() - Minecraft.getMinecraft().thePlayer.getItemInUseCount();
        event.newfov = event.fov + inUse/110;
    }

    @SuppressWarnings("deprecation")
    @SideOnly(Side.CLIENT)
    @Override
    public int getDisplayDamage(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            if (getTimeLeft(stack, player) > 0)
            {
                return getTimeLeft(stack, player);
            }
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public int getMaxDamage(ItemStack stack)
    {
        if (stack.hasTagCompound())
        {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            return player.capabilities.isCreativeMode ? 1 : Settings.PLAYER_RELOCATOR_COOLDOWN;
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isDamageable()
    {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    public boolean isDamaged(ItemStack itemStack)
    {
        if (itemStack.hasTagCompound())
        {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            return getTimeLeft(itemStack, player) > 0;
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public void renderBlur(ItemStack stack, ScaledResolution resolution)
    {
        float inUse = ((float) stack.getMaxItemUseDuration() - Minecraft.getMinecraft().thePlayer.getItemInUseCount()) / stack.getMaxItemUseDuration();

        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(false);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        float scale = 2/inUse;

        Vector2f sourceCenter = new Vector2f(resolution.getScaledWidth()/2, resolution.getScaledHeight()/2);
        Vector2f destCenter = new Vector2f(resolution.getScaledWidth()/2, resolution.getScaledHeight()/2);
        GL11.glTranslatef(destCenter.getX(), destCenter.getY(), 0.0F);
        GL11.glScalef(scale, scale, 0.0F);
        GL11.glTranslatef(sourceCenter.getX() * -1.0F, sourceCenter.getY() * -1.0F, 0.0F);

        Minecraft.getMinecraft().getTextureManager().bindTexture(Resources.TEXTURE_BLUR);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.addVertexWithUV(0.0D, resolution.getScaledHeight(), 0, 0.0D, 1.0D);
        tessellator.addVertexWithUV(resolution.getScaledWidth(), resolution.getScaledHeight(), 0, 1.0D, 1.0D);
        tessellator.addVertexWithUV(resolution.getScaledWidth(), 0.0D, 0, 1.0D, 0.0D);
        tessellator.addVertexWithUV(0.0D, 0.0D, 0, 0.0D, 0.0D);
        tessellator.draw();
        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static long getTimeDifference(ItemStack stack)
    {
        return System.currentTimeMillis() - stack.getTagCompound().getLong(TIME);
    }

    public static int getTimeLeft(ItemStack stack, EntityPlayer player)
    {
        int cooldown = player.capabilities.isCreativeMode ? 1 : Settings.PLAYER_RELOCATOR_COOLDOWN;
        return (int) (cooldown - (getTimeDifference(stack)/1000));
    }
}
