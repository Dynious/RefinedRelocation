package com.dynious.refinedrelocation.client.renderer;

import com.dynious.refinedrelocation.block.ModBlocks;
import com.dynious.refinedrelocation.tileentity.TilePowerLimiter;
import com.dynious.refinedrelocation.tileentity.TileSortingInterface;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;
import org.lwjgl.opengl.GL11;

public class DirectionalRenderer implements ISimpleBlockRenderingHandler
{
    public static int renderId;

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer)
    {
        Tessellator tessellator = Tessellator.instance;

        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        tessellator.startDrawingQuads();
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        renderer.renderFaceYNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(0, metadata));
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        renderer.renderFaceYPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(1, metadata));
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        renderer.renderFaceZNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(2, metadata));
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        renderer.renderFaceZPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(3, metadata));
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        renderer.renderFaceXNeg(block, 0.0D, 0.0D, 0.0D, block.getIcon(4, metadata));
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        renderer.renderFaceXPos(block, 0.0D, 0.0D, 0.0D, block.getIcon(5, metadata));
        tessellator.draw();
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
    {
        int metadata = world.getBlockMetadata(x, y, z);
        if (block == ModBlocks.sortingConnector && metadata == 1)
        {
            TileSortingInterface sortingInterface = (TileSortingInterface) world.getTileEntity(x, y, z);
            setupUVRotation(renderer, sortingInterface.getConnectedSide());
        } else if(block == ModBlocks.powerLimiter) {
            TilePowerLimiter powerLimiter = (TilePowerLimiter) world.getTileEntity(x, y, z);
            setupUVRotation(renderer, powerLimiter.getConnectedDirection());
        }

        boolean rendered = renderer.renderStandardBlock(block, x, y, z);

        resetUVRotation(renderer);

        return rendered;
    }

    private void setupUVRotation(RenderBlocks renderer, ForgeDirection direction) {
        switch (direction)
        {
            case NORTH:
                renderer.uvRotateNorth = 2;
                renderer.uvRotateSouth = 1;
                break;
            case DOWN:
                renderer.uvRotateNorth = 3;
                renderer.uvRotateEast = 3;
                renderer.uvRotateSouth = 3;
                renderer.uvRotateWest = 3;
                break;
            case EAST:
                renderer.uvRotateTop = 1;
                renderer.uvRotateBottom = 2;
                renderer.uvRotateEast = 2;
                renderer.uvRotateWest = 1;
                break;
            case SOUTH:
                renderer.uvRotateTop = 3;
                renderer.uvRotateBottom = 3;
                renderer.uvRotateNorth = 1;
                renderer.uvRotateSouth = 2;
                break;
            case WEST:
                renderer.uvRotateTop = 2;
                renderer.uvRotateBottom = 1;
                renderer.uvRotateEast = 1;
                renderer.uvRotateWest = 2;
                break;
        }
    }

    private void resetUVRotation(RenderBlocks renderer) {
        renderer.uvRotateTop = 0;
        renderer.uvRotateBottom = 0;
        renderer.uvRotateNorth = 0;
        renderer.uvRotateEast = 0;
        renderer.uvRotateSouth = 0;
        renderer.uvRotateWest = 0;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId)
    {
        return true;
    }

    @Override
    public int getRenderId()
    {
        return renderId;
    }

}
