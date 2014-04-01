package com.dynious.refinedrelocation.item;

import com.dynious.refinedrelocation.RefinedRelocation;
import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.helper.ParticleHelper;
import com.dynious.refinedrelocation.lib.Names;
import com.dynious.refinedrelocation.lib.Resources;
import com.dynious.refinedrelocation.lib.Sounds;
import com.dynious.refinedrelocation.tileentity.TileRelocationController;
import com.dynious.refinedrelocation.tileentity.TileRelocationPortal;
import com.dynious.refinedrelocation.until.Vector3;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.particle.EntityPortalFX;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.common.DimensionManager;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;

import java.util.List;

public class ItemPlayerRelocator extends Item
{
    public static final String COOL_DOWN = "coolDown";

    public ItemPlayerRelocator(int id)
    {
        super(id);
        this.setUnlocalizedName(Names.playerRelocator);
        this.setCreativeTab(RefinedRelocation.tabRefinedRelocation);
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
    {
        if (world.isRemote)
            return false;

        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocationController)
        {
            if (((TileRelocationController)tile).isFormed(true))
            {
                if (!stack.hasTagCompound())
                {
                    stack.setTagCompound(new NBTTagCompound());
                }
                if (((TileRelocationController) tile).isIntraLinker())
                {
                    stack.getTagCompound().setBoolean("interLink", true);
                }
                else
                {
                    stack.getTagCompound().setBoolean("interLink", false);
                }
                stack.getTagCompound().setInteger("dimId", tile.getWorldObj().provider.dimensionId);
                stack.getTagCompound().setInteger("x", x);
                stack.getTagCompound().setInteger("y", y);
                stack.getTagCompound().setInteger("z", z);
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
    public void onUpdate(ItemStack stack, World world, Entity entity, int par4, boolean par5)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey(COOL_DOWN))
        {
            short coolDown = stack.getTagCompound().getShort(COOL_DOWN);
            coolDown--;
            if (coolDown <= 0)
            {
                stack.getTagCompound().removeTag(COOL_DOWN);
            }
            else
            {
                stack.getTagCompound().setShort(COOL_DOWN, coolDown);
            }
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player)
    {
        if (stack.hasTagCompound() && stack.getTagCompound().hasKey("x") && !stack.getTagCompound().hasKey(COOL_DOWN))
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
            World world2 = world;
            if (stack.getTagCompound().hasKey("dimId"))
            {
                world2 = DimensionManager.getProvider(stack.getTagCompound().getInteger("dimId")).worldObj;
            }
            if (world == world2 || stack.getTagCompound().getBoolean("interLink"))
            {
                TileEntity connectedTile = world2.getBlockTileEntity(stack.getTagCompound().getInteger("x"), stack.getTagCompound().getInteger("y"), stack.getTagCompound().getInteger("z"));
                if (connectedTile != null && connectedTile instanceof TileRelocationController && ((TileRelocationController) connectedTile).isFormed(true))
                {
                    if (world != world2 && !((TileRelocationController) connectedTile).isIntraLinker())
                    {
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
                        if (stack.getTagCompound().hasKey("dimId"))
                        {
                            setLowerBlockToDimensionalPortal(world, xPos, yPos - 3, zPos, new Vector3(connectedTile.xCoord, connectedTile.yCoord, connectedTile.zCoord), stack.getTagCompound().getInteger("dimId"));
                        }
                        setLowerBlockToPortal(world, xPos, yPos - 3, zPos, new Vector3(connectedTile.xCoord, connectedTile.yCoord, connectedTile.zCoord));

                        world.playSoundAtEntity(player, Sounds.explosion, 1F, 1F);
                        stack.getTagCompound().setShort(COOL_DOWN, (short) 200);
                    }
                }
            }
        }
        return stack;
    }

    private boolean checkBlocks(World world, int posX, int posY, int posZ)
    {
        int blockID = world.getBlockId(posX, posY - 1, posZ);
        int blockID2 = world.getBlockId(posX, posY - 2, posZ);
        int blockID3 = world.getBlockId(posX, posY - 3, posZ);

        return (world.getBlockTileEntity(posX, posY - 1, posZ) == null) && (blockID != Block.bedrock.blockID)
                && (blockID != ModBlocks.relocationPortal.blockID) && (Block.blocksList[blockID] != null) && (!Block.blocksList[blockID].canPlaceBlockAt(world, posX, posY - 1, posZ))
                && (Block.blocksList[blockID].getBlockHardness(world, posX, posY - 1, posZ) != -1.0F)
                && (world.getBlockTileEntity(posX, posY - 2, posZ) == null) && (blockID2 != Block.bedrock.blockID)
                && (blockID2 != ModBlocks.relocationPortal.blockID)
                && (world.getBlockTileEntity(posX, posY - 3, posZ) == null) && (blockID3 != Block.bedrock.blockID)
                && (blockID3 != ModBlocks.relocationPortal.blockID);
    }

    private void setBlockToPortal(World world, int x, int y, int z)
    {
        int blockID = world.getBlockId(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, ModBlocks.relocationPortal.blockID);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocationPortal)
        {
            ((TileRelocationPortal)tile).init(blockID, blockMeta);
        }
    }

    private void setLowerBlockToPortal(World world, int x, int y, int z, Vector3 linkedPos)
    {
        int blockID = world.getBlockId(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, ModBlocks.relocationPortal.blockID);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocationPortal)
        {
            ((TileRelocationPortal)tile).init(blockID, blockMeta, linkedPos);
        }
    }

    private void setLowerBlockToDimensionalPortal(World world, int x, int y, int z, Vector3 linkedPos, int dimentionId)
    {
        int blockID = world.getBlockId(x, y, z);
        int blockMeta = world.getBlockMetadata(x, y, z);
        world.setBlock(x, y, z, ModBlocks.relocationPortal.blockID);
        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if (tile != null && tile instanceof TileRelocationPortal)
        {
            ((TileRelocationPortal)tile).init(blockID, blockMeta, linkedPos, dimentionId);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean b)
    {
        if (itemStack.hasTagCompound() && itemStack.getTagCompound().hasKey("x"))
        {
            list.add("Linked to Relocation Controller at: " + itemStack.getTagCompound().getInteger("x") + ":" + itemStack.getTagCompound().getInteger("y") + ":" +  itemStack.getTagCompound().getInteger("z"));
            if (itemStack.getTagCompound().getBoolean("interLink"))
            {
                list.add("Inter-Dimensional link to: " + DimensionManager.getProvider(itemStack.getTagCompound().getInteger("dimId")).getDimensionName());
            }
            if (itemStack.getTagCompound().hasKey(COOL_DOWN))
            {
                list.add("Cooldown: " + itemStack.getTagCompound().getShort(COOL_DOWN));
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public void shiftFOV(ItemStack stack, FOVUpdateEvent event)
    {
        float inUse = stack.getMaxItemUseDuration() - Minecraft.getMinecraft().thePlayer.getItemInUseCount();
        event.newfov = event.fov + inUse/110;
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
}
