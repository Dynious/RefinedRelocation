package com.dynious.refinedrelocation.client.renderer;

import codechicken.lib.render.BlockRenderer;
import codechicken.lib.vec.Cuboid6;
import com.dynious.refinedrelocation.multiblock.IMultiBlock;
import com.dynious.refinedrelocation.multiblock.MultiBlockRegistry;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;
import com.dynious.refinedrelocation.util.BlockAndMeta;
import com.dynious.refinedrelocation.util.MultiBlockAndMeta;
import com.dynious.refinedrelocation.util.Vector3;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RendererMultiBlock extends TileEntitySpecialRenderer
{
    private static RenderBlocks renderBlocks = new RenderBlocks();

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double xPos, double yPos, double zPos, float timer)
    {
        if (tileEntity != null && tileEntity instanceof TileMultiBlockBase)
        {
            TileMultiBlockBase tileMultiBlock = (TileMultiBlockBase) tileEntity;
            if (!tileMultiBlock.isFormed(false))
            {
                IMultiBlock multiBlock = MultiBlockRegistry.getMultiBlock(tileMultiBlock.getMultiBlockIdentifier());

                if (multiBlock != null)
                {
                    Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

                    GL11.glPushMatrix();
                    GL11.glTranslated(xPos + 0.5F, yPos + 0.5F, zPos + 0.5F);

                    for (int x = 0; x < multiBlock.getMultiBlockMap().getSizeX(); x++)
                    {
                        for (int y = 0; y < multiBlock.getMultiBlockMap().getSizeY(); y++)
                        {
                            for (int z = 0; z < multiBlock.getMultiBlockMap().getSizeZ(); z++)
                            {
                                GL11.glPushMatrix();
                                
                                renderBlocks(multiBlock, tileMultiBlock, x, y, z);

                                GL11.glPopMatrix();
                            }
                        }
                    }
                    GL11.glPopMatrix();
                }
            }
        }
    }

    private void renderBlocks(IMultiBlock multiBlock, TileMultiBlockBase tileMultiBlock, int x, int y, int z)
    {
        Vector3 leaderPos = multiBlock.getRelativeLeaderPos();
        Object blockInfo = multiBlock.getMultiBlockMap().getBlockAndMetaAtPos(x, y, z);
        int xOffset = tileMultiBlock.xCoord + x - leaderPos.getX();
        int yOffset = tileMultiBlock.yCoord + y - leaderPos.getY();
        int zOffset = tileMultiBlock.zCoord + z - leaderPos.getZ();

        if (!tileMultiBlock.getWorldObj().isAirBlock(xOffset, yOffset, zOffset))
        {
            Block block = tileMultiBlock.getWorldObj().getBlock(xOffset, yOffset, zOffset);
            BlockAndMeta blockAndMeta;
            if (blockInfo instanceof MultiBlockAndMeta)
            {
                blockAndMeta = getBlockAndMeta((MultiBlockAndMeta) blockInfo, tileMultiBlock);
            }
            else if (blockInfo instanceof BlockAndMeta)
            {
                blockAndMeta = (BlockAndMeta) blockInfo;
            }
            else
            {
                return;
            }

            if (blockAndMeta.getBlock() != block || (blockAndMeta.getMeta() != -1 && blockAndMeta.getMeta() != tileMultiBlock.getWorldObj().getBlockMetadata(xOffset, yOffset, zOffset)))
            {
                renderIncorrectBlock(x, y, z);
            }
        }
        else
        {
            renderBlock(blockInfo, multiBlock, tileMultiBlock, x, y, z);
        }
    }

    private void renderBlock(Object blockInfo, IMultiBlock multiBlock, TileMultiBlockBase tileMultiBlock, int x, int y, int z)
    {
        Vector3 leaderPos = multiBlock.getRelativeLeaderPos();
        BlockAndMeta blockAndMeta = null;
        int relativeX = 0;
        int relativeY = 0;
        int relativeZ = 0;

        if (blockInfo instanceof MultiBlockAndMeta)
        {
            MultiBlockAndMeta multiBlockAndMeta = (MultiBlockAndMeta) blockInfo;

            blockAndMeta = getBlockAndMeta(multiBlockAndMeta, tileMultiBlock);
            relativeX = x - leaderPos.getX();
            relativeY = y - leaderPos.getY();
            relativeZ = z - leaderPos.getZ();
        }
        else if (blockInfo instanceof BlockAndMeta)
        {
            blockAndMeta = (BlockAndMeta) blockInfo;
            relativeX = x - leaderPos.getX();
            relativeY = y - leaderPos.getY();
            relativeZ = z - leaderPos.getZ();
        }

        if (blockAndMeta != null && blockAndMeta.getBlock() != null)
        {
            float scale = 0.5F;
            GL11.glTranslatef(relativeX, relativeY, relativeZ);
            GL11.glScalef(scale, scale, scale);
            renderBlocks.renderBlockAsItem(blockAndMeta.getBlock(), blockAndMeta.getMeta(), 255F);
        }
    }

    private void renderIncorrectBlock(int x, int y, int z)
    {
        GL11.glPushMatrix();
        GL11.glTranslatef(x - 1, y, z - 1);

        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_CULL_FACE);

        Tessellator t = Tessellator.instance;

        t.startDrawingQuads();
        t.setColorRGBA(255, 0, 0, 126);
        
        Cuboid6 vector = new Cuboid6(-0.501, -0.501, -0.501, 0.501, 0.501, 0.501);

        // Top side
        t.setNormal(0, 1, 0);
        t.addVertex(vector.min.x, vector.max.y, vector.max.z);
        t.addVertex(vector.max.x, vector.max.y, vector.max.z);
        t.addVertex(vector.max.x, vector.max.y, vector.min.z);
        t.addVertex(vector.min.x, vector.max.y, vector.min.z);

        // Bottom side
        t.setNormal(0, -1, 0);
        t.addVertex(vector.max.x, vector.min.y, vector.max.z);
        t.addVertex(vector.min.x, vector.min.y, vector.max.z);
        t.addVertex(vector.min.x, vector.min.y, vector.min.z);
        t.addVertex(vector.max.x, vector.min.y, vector.min.z);

        // West side:
        t.setNormal(-1, 0, 0);
        t.addVertex(vector.min.x, vector.min.y, vector.max.z);
        t.addVertex(vector.min.x, vector.max.y, vector.max.z);
        t.addVertex(vector.min.x, vector.max.y, vector.min.z);
        t.addVertex(vector.min.x, vector.min.y, vector.min.z);

        // East side:
        t.setNormal(1, 0, 0);
        t.addVertex(vector.max.x, vector.min.y, vector.min.z);
        t.addVertex(vector.max.x, vector.max.y, vector.min.z);
        t.addVertex(vector.max.x, vector.max.y, vector.max.z);
        t.addVertex(vector.max.x, vector.min.y, vector.max.z);

        // North side
        t.setNormal(0, 0, -1);
        t.addVertex(vector.min.x, vector.min.y, vector.min.z);
        t.addVertex(vector.min.x, vector.max.y, vector.min.z);
        t.addVertex(vector.max.x, vector.max.y, vector.min.z);
        t.addVertex(vector.max.x, vector.min.y, vector.min.z);

        // South side
        t.setNormal(0, 0, 1);
        t.addVertex(vector.min.x, vector.min.y, vector.max.z);
        t.addVertex(vector.max.x, vector.min.y, vector.max.z);
        t.addVertex(vector.max.x, vector.max.y, vector.max.z);
        t.addVertex(vector.min.x, vector.max.y, vector.max.z);

        GL11.glColor4d(1, 1, 1, 1);

        t.draw();

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
    }

    private BlockAndMeta getBlockAndMeta(MultiBlockAndMeta multiBlockAndMeta, TileMultiBlockBase tileMultiBlock)
    {
        int timePerBlock = 20 / multiBlockAndMeta.getBlockAndMetas().size();
        return multiBlockAndMeta.getBlockAndMetas().get((tileMultiBlock.timer % 20) / timePerBlock);
    }
}
