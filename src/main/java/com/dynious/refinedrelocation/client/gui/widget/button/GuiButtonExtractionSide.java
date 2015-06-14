package com.dynious.refinedrelocation.client.gui.widget.button;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import com.dynious.refinedrelocation.grid.relocator.RelocatorModuleSneakyExtraction;
import com.dynious.refinedrelocation.helper.BlockHelper;
import com.dynious.refinedrelocation.helper.GuiHelper;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.tileentity.TileRelocator;
import net.minecraft.client.Minecraft;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

public class GuiButtonExtractionSide extends GuiButton
{
    private RelocatorModuleSneakyExtraction module;

    public GuiButtonExtractionSide(IGuiParent parent, int x, int y, RelocatorModuleSneakyExtraction module)
    {
        super(parent, x, y, 34, 20, 120, 0, ForgeDirection.getOrientation(module.getExtractionSide()).toString());
        this.module = module;
        this.setTooltipString(StatCollector.translateToLocal(Strings.SNEAKY));
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        if (isInsideBounds(mouseX, mouseY))
        {
            if (type == 0)
            {
                onValueChangedByUser((module.getExtractionSide() + 1) % ForgeDirection.VALID_DIRECTIONS.length);
            }
            else if (type == 1)
            {
                int i = module.getExtractionSide() - 1;
                if (i < 0) i = ForgeDirection.VALID_DIRECTIONS.length - 1;
                onValueChangedByUser(i);
            }
        }
    }

    protected void onValueChangedByUser(int side)
    {
        module.setExtractionSide(side);
        GuiHelper.sendIntMessage(3, side);
    }

    public void update()
    {
        if (module != null)
        {
            ForgeDirection outputDirection = ForgeDirection.getOrientation(module.getExtractionSide());
            label.setText(outputDirection.toString());

            int[] blockInfo = getSneakySideBlockInfo();
            String blockName = BlockHelper.getBlockDisplayName(Minecraft.getMinecraft().theWorld, blockInfo[0], blockInfo[1], blockInfo[2], ForgeDirection.getOrientation(blockInfo[3]));

            this.setTooltipString(blockName + "\n" + StatCollector.translateToLocal(Strings.SNEAKY_EXTRACTION));
        }

        super.update();
    }

    private int[] getSneakySideBlockInfo()
    {
        final TileRelocator relocator = module.getRelocator();
        final ForgeDirection direction = module.getSide();
        final ForgeDirection outputDirection = ForgeDirection.getOrientation(module.getExtractionSide());
        return new int[]{
                relocator.xCoord + direction.offsetX + outputDirection.offsetX,
                relocator.yCoord + direction.offsetY + outputDirection.offsetY,
                relocator.zCoord + direction.offsetZ + outputDirection.offsetZ,
                direction.ordinal()
        };
    }
}
