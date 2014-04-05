package com.dynious.refinedrelocation.renderer;

import com.dynious.refinedrelocation.multiblock.IMultiBlock;
import com.dynious.refinedrelocation.multiblock.MultiBlockRegistry;
import com.dynious.refinedrelocation.multiblock.TileMultiBlockBase;
import com.dynious.refinedrelocation.util.BlockAndMeta;
import com.dynious.refinedrelocation.util.MultiBlockAndMeta;
import com.dynious.refinedrelocation.util.Vector3;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.RenderGlobal;
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
                Vector3 leaderPos = multiBlock.getRelativeLeaderPos();
                Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);

                if (multiBlock != null)
                {
                    GL11.glPushMatrix();
                    GL11.glTranslated(xPos + 0.5F, yPos + 0.5F, zPos + 0.5F);

                    for (int x = 0; x < multiBlock.getMultiBlockMap().getSizeX(); x++)
                    {
                        for (int y = 0; y < multiBlock.getMultiBlockMap().getSizeY(); y++)
                        {
                            for (int z = 0; z < multiBlock.getMultiBlockMap().getSizeZ(); z++)
                            {
                                if (!tileMultiBlock.getWorldObj().isAirBlock(tileMultiBlock.xCoord + x - leaderPos.getX(), tileMultiBlock.yCoord + y - leaderPos.getY(), tileMultiBlock.zCoord + z - leaderPos.getZ()))
                                {
                                    continue;
                                }
                                Object blockInfo = multiBlock.getMultiBlockMap().getBlockAndMetaAtPos(x, y, z);

                                GL11.glPushMatrix();

                                if (blockInfo instanceof MultiBlockAndMeta)
                                {
                                    if (tileMultiBlock.timer % 40 >= 20)
                                    {
                                        MultiBlockAndMeta multiBlockAndMeta = (MultiBlockAndMeta) blockInfo;
                                        int timePerBlock = 20 / multiBlockAndMeta.getBlockAndMetas().size();
                                        int blockPlace = (tileMultiBlock.timer % 20) / timePerBlock;
                                        renderBlock(multiBlockAndMeta.getBlockAndMetas().get(blockPlace), x - leaderPos.getX(), y - leaderPos.getY(), z - leaderPos.getZ());
                                    }
                                }
                                else if (blockInfo instanceof BlockAndMeta)
                                {
                                    if (tileMultiBlock.timer % 40 >= 20)
                                    {
                                        renderBlock((BlockAndMeta) blockInfo, x - leaderPos.getX(), y - leaderPos.getY(), z - leaderPos.getZ());
                                    }
                                }

                                GL11.glPopMatrix();
                            }
                        }
                    }

                    GL11.glPopMatrix();
                }
            }
        }
    }

    private void renderBlock(BlockAndMeta blockAndMeta, int relativeX, int relativeY, int relativeZ)
    {
        if (blockAndMeta.getBlock() != null)
        {
            GL11.glTranslatef(relativeX, relativeY, relativeZ);
            renderBlocks.renderBlockAsItem(blockAndMeta.getBlock(), blockAndMeta.getMeta(), 255F);
            //Minecraft.getMinecraft().renderGlobal.globalRenderBlocks.renderBlockByRenderType(Block.blocksList[blockAndMeta.getBlockId()], x - leaderPos.getX(), y - leaderPos.getY(), z - leaderPos.getZ());
        }
    }
}
