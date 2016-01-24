package com.dynious.refinedrelocation.client.gui.widget;

import com.dynious.refinedrelocation.client.gui.IGuiParent;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ChatAllowedCharacters;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class GuiTextInputMultiline extends GuiWidgetBase
{

    private static final int MARGIN = 2;

    private final FontRenderer fontRenderer;

    protected boolean isMultiLine;
    private boolean enabled = true;
    private boolean hasFocus;

    private int cursorPosition;
    private int cursorCounter;
    private int maxLength;
    private int maxLengthPerLine;

    private String text;
    private String[] renderLines;
    private int scrollOffset;
    private int lineScrollOffset;

    public GuiTextInputMultiline(IGuiParent parent, int x, int y, int w, int h)
    {
        super(parent, x, y, w, h);
        fontRenderer = mc.fontRenderer;
    }

    @Override
    public void update()
    {
        super.update();

        cursorCounter++;
    }

    @Override
    public void drawBackground(int mouseX, int mouseY)
    {
        super.drawBackground(mouseX, mouseY);

        drawRect(x - 1, y - 1, x + w + 1, y + h + 1, -6250336);
        drawRect(x, y, x + w, y + h, -16777216);
    }

    @Override
    public void drawForeground(int mouseX, int mouseY)
    {
        super.drawForeground(mouseX, mouseY);

        if (visible)
        {
            if (renderLines == null)
            {
                renderLines = text.split("\n");
            }

            // Draw text lines
            for (int i = scrollOffset; i < renderLines.length; i++)
            {
                int offsetY = (i - scrollOffset) * fontRenderer.FONT_HEIGHT;
                if (offsetY + fontRenderer.FONT_HEIGHT >= h)
                {
                    break;
                }
                if (lineScrollOffset >= renderLines[i].length())
                {
                    continue;
                }
                String lineText = fontRenderer.trimStringToWidth(renderLines[i].substring(lineScrollOffset), w - MARGIN);
                fontRenderer.drawString(lineText, x + MARGIN, y + MARGIN + offsetY, Integer.MAX_VALUE);
            }

            // Draw cursor
            int cursorLine = 0;
            int lastLineIdx = 0;
            for (int i = 0; i < cursorPosition; i++)
            {
                if (text.charAt(i) == '\n')
                {
                    cursorLine++;
                    lastLineIdx = i + 1;
                }
            }
            if ((cursorLine - scrollOffset) * fontRenderer.FONT_HEIGHT >= 0 && (cursorLine - scrollOffset + 1) * fontRenderer.FONT_HEIGHT < h - MARGIN)
            {
                drawCursorVertical(x + fontRenderer.getStringWidth(text.substring(lastLineIdx + lineScrollOffset, cursorPosition)) + MARGIN, y + (cursorLine - scrollOffset) * fontRenderer.FONT_HEIGHT + MARGIN);
            }
        }
    }

    private void drawCursorVertical(int x, int y)
    {
        Tessellator tessellator = Tessellator.instance;
        GL11.glColor4f(0.0F, 0.0F, 255.0F, 255.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glLogicOp(GL11.GL_OR_REVERSE);
        tessellator.startDrawingQuads();
        tessellator.addVertex((double) x, (double) y + fontRenderer.FONT_HEIGHT, 0.0D);
        tessellator.addVertex((double) x + 1, (double) y + fontRenderer.FONT_HEIGHT, 0.0D);
        tessellator.addVertex((double) x + 1, (double) y, 0.0D);
        tessellator.addVertex((double) x, (double) y, 0.0D);
        tessellator.draw();
        GL11.glDisable(GL11.GL_COLOR_LOGIC_OP);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    @Override
    public boolean keyTyped(char unicode, int keyCode)
    {
        if (!hasFocus)
        {
            return false;
        }
        switch (keyCode)
        {
            case Keyboard.KEY_END:
                if (GuiScreen.isCtrlKeyDown())
                {
                    setCursorPosition(text.length());
                } else
                {
                    setCursorPosition(getEndOfLine(cursorPosition, 1));
                }
                return true;
            case Keyboard.KEY_HOME:
                if (GuiScreen.isCtrlKeyDown())
                {
                    setCursorPosition(0);
                } else
                {
                    setCursorPosition(getStartOfLine(cursorPosition, 1));
                }
                return true;
            case Keyboard.KEY_LEFT:
                if (GuiScreen.isCtrlKeyDown())
                {
                    setCursorPosition(getStartOfWord(cursorPosition - 1));
                } else
                {
                    setCursorPosition(cursorPosition - 1);
                }
                return true;
            case Keyboard.KEY_RIGHT:
                if (GuiScreen.isCtrlKeyDown())
                {
                    setCursorPosition(getStartOfNextWord(cursorPosition + 1));
                } else
                {
                    setCursorPosition(cursorPosition + 1);
                }
                return true;
            case Keyboard.KEY_UP:
                if (GuiScreen.isCtrlKeyDown())
                {
                    scroll(0, -1);
                } else
                {
                    int upLine = getStartOfLine(cursorPosition, 2);
                    setCursorPosition(upLine + Math.min(getLineLength(upLine), (cursorPosition - getStartOfLine(cursorPosition, 1))));
                }
                return true;
            case Keyboard.KEY_DOWN:
                if (GuiScreen.isCtrlKeyDown())
                {
                    scroll(0, 1);
                } else
                {
                    int downLine = getEndOfLine(cursorPosition, 2);
                    setCursorPosition(getStartOfLine(downLine, 1) + Math.min(getLineLength(downLine), (cursorPosition - getStartOfLine(cursorPosition, 1))));
                }
                return true;
            case Keyboard.KEY_RETURN:
                if(enabled)
                {
                    insertText("\n");
                    markDirty();
                }
                return true;
            case Keyboard.KEY_DELETE:
                if(enabled)
                {
                    deleteFront(GuiScreen.isCtrlKeyDown());
                }
                return true;
            case Keyboard.KEY_BACK:
                if(enabled)
                {
                    deleteBack(GuiScreen.isCtrlKeyDown());
                }
                return true;
            default:
                if(enabled)
                {
                    if (ChatAllowedCharacters.isAllowedCharacter(unicode))
                    {
                        insertText(Character.toString(unicode));
                        return true;
                    }
                }
        }
        return super.keyTyped(unicode, keyCode);
    }

    private int getLineLength(int position)
    {
        return getEndOfLine(position, 1) - getStartOfLine(position, 1);
    }

    private int getStartOfWord(int position)
    {
        position = Math.max(Math.min(position, text.length() - 1), 0);
        if (text.charAt(position) == '\n')
        {
            return position;
        }
        boolean foundAlphabetic = false;
        for (int i = position; i >= 0; i--)
        {
            char c = text.charAt(i);
            if (c == '\n')
            {
                return i + 1;
            }
            if (Character.isAlphabetic(c))
            {
                foundAlphabetic = true;
            } else if (foundAlphabetic)
            {
                return i + 1;
            }
        }
        return 0;
    }

    private int getStartOfNextWord(int position)
    {
        position = Math.max(Math.min(position, text.length() - 1), 0);
        if (text.charAt(position) == '\n')
        {
            return position;
        }
        boolean foundNonAlphabetic = false;
        for (int i = position; i < text.length(); i++)
        {
            char c = text.charAt(i);
            if (c == '\n')
            {
                return i;
            }
            if (!Character.isAlphabetic(c))
            {
                foundNonAlphabetic = true;
            } else if (foundNonAlphabetic)
            {
                return i;
            }
        }
        return text.length();
    }

    private int getStartOfLine(int position, int iterations)
    {
        int startOfLine = position;
        for (int i = 0; i < iterations; i++)
        {
            startOfLine = text.lastIndexOf('\n', startOfLine - 1);
        }
        return startOfLine != -1 ? startOfLine + 1 : 0;
    }

    private int getEndOfLine(int position, int iteration)
    {
        int endOfLine = position - 1;
        for (int i = 0; i < iteration; i++)
        {
            endOfLine = text.indexOf('\n', endOfLine + 1);
            if (endOfLine == -1)
            {
                return text.length();
            }
        }
        return endOfLine != -1 ? endOfLine : text.length();
    }

    private void markDirty()
    {
        renderLines = null;
    }

    private void deleteBack(boolean wholeWord)
    {
        int deleteCount = 1;
        if (wholeWord)
        {
            deleteCount = cursorPosition - getStartOfWord(cursorPosition);
        }
        if (cursorPosition > 0)
        {
            text = text.substring(0, cursorPosition - deleteCount) + text.substring(cursorPosition);
            setCursorPosition(cursorPosition - deleteCount);
            markDirty();
            onTextChangedByUser(text);
        }
    }

    private void deleteFront(boolean wholeWord)
    {
        int deleteCount = 1;
        if (wholeWord)
        {
            deleteCount = getStartOfNextWord(cursorPosition) - cursorPosition;
        }
        if (cursorPosition < text.length())
        {
            text = text.substring(0, cursorPosition) + text.substring(cursorPosition + deleteCount);
            markDirty();
            onTextChangedByUser(text);
        }
    }

    private void insertText(String s)
    {
        text = (cursorPosition > 0 ? text.substring(0, cursorPosition) : "") + s + (text.length() > cursorPosition ? text.substring(cursorPosition) : "");
        setCursorPosition(cursorPosition + s.length());
        markDirty();
        onTextChangedByUser(text);
    }

    public void setFocused(boolean hasFocus)
    {
        if (hasFocus && !this.hasFocus)
        {
            cursorCounter = 0;
        }
        this.hasFocus = hasFocus;
    }

    public void setCursorPosition(int cursorPosition)
    {
        this.cursorPosition = Math.min(Math.max(cursorPosition, 0), text.length());

        int cursorLine = 0;
        for (int i = 0; i < this.cursorPosition; i++)
        {
            if (text.charAt(i) == '\n')
            {
                cursorLine++;
            }
        }

        int visibleHeight = h - MARGIN;
        int visibleCursorY = (cursorLine - scrollOffset) * fontRenderer.FONT_HEIGHT + MARGIN;
        if (visibleCursorY < 0)
        {
            scroll(0, visibleCursorY / fontRenderer.FONT_HEIGHT - 1);
        } else if (visibleCursorY > visibleHeight - fontRenderer.FONT_HEIGHT)
        {
            scroll(0, (visibleCursorY - visibleHeight) / fontRenderer.FONT_HEIGHT + 1);
        }

        int visibleWidth = w - MARGIN;
        int cursorLineStart = getStartOfLine(this.cursorPosition, 1);
        int cursorLineEnd = getEndOfLine(this.cursorPosition, 1);
        int cursorLineX = Math.min(getLineLength(this.cursorPosition), (this.cursorPosition - cursorLineStart));
        String lineText = text.substring(cursorLineStart, cursorLineEnd);
        lineScrollOffset = Math.max(Math.min(lineScrollOffset, lineText.length()), 0);
        if (cursorLineX == lineScrollOffset)
        {
            lineScrollOffset -= fontRenderer.trimStringToWidth(lineText, visibleWidth, true).length();
        }
        lineScrollOffset = Math.max(Math.min(lineScrollOffset, lineText.length()), 0);
        String visibleLineText = fontRenderer.trimStringToWidth(lineText.substring(lineScrollOffset), visibleWidth);
        int l = visibleLineText.length() + lineScrollOffset;
        if (cursorLineX > l)
        {
            lineScrollOffset += cursorLineX - l;
        } else if (cursorLineX <= lineScrollOffset)
        {
            lineScrollOffset -= lineScrollOffset - cursorLineX;
        }
        lineScrollOffset = Math.max(Math.min(lineScrollOffset, lineText.length()), 0);
    }

    public void scroll(int x, int y)
    {
        lineScrollOffset = Math.max(lineScrollOffset + x, 0);
        scrollOffset = Math.max(scrollOffset + y, 0);
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int type, boolean isShiftKeyDown)
    {
        super.mouseClicked(mouseX, mouseY, type, isShiftKeyDown);

        boolean isInside = isInsideBounds(mouseX, mouseY);
        setFocused(isInside);

        if (isInside && type == 0)
        {
            int relX = mouseX - x;
            int relY = mouseY - y - MARGIN;
            int lineNumber = Math.round((float) relY / (float) fontRenderer.FONT_HEIGHT) + scrollOffset + 1;
            int startOfLine = getStartOfLine(getEndOfLine(0, lineNumber), 1);
            int endOfLine = getEndOfLine(startOfLine, 1);
            String visibleLine = fontRenderer.trimStringToWidth(text.substring(Math.max(startOfLine + lineScrollOffset, 0), endOfLine), w - MARGIN);
            setCursorPosition(startOfLine + fontRenderer.trimStringToWidth(visibleLine, relX).length() + lineScrollOffset);
        }
    }

    public String getText()
    {
        return text;
    }

    public void setText(String text)
    {
        this.text = text;
        markDirty();
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    protected void onTextChangedByUser(String newText)
    {
    }
}
