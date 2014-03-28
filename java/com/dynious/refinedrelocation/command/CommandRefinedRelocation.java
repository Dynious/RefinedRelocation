package com.dynious.refinedrelocation.command;

import com.dynious.refinedrelocation.helper.WebpageHelper;
import com.dynious.refinedrelocation.lib.Commands;
import com.dynious.refinedrelocation.version.VersionChecker;
import com.dynious.refinedrelocation.version.VersionContainer;
import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatMessageComponent;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CommandRefinedRelocation extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return Commands.COMMAND_REFINED_RELOCATION;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return null;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] args)
    {
        if (args.length > 0)
        {
            String commandName = args[0];
            System.arraycopy(args, 1, args, 0, args.length - 1);

            if (commandName.equalsIgnoreCase(Commands.LATEST))
            {
                VersionContainer.Version version = VersionChecker.getRemoteVersion();
                if (version != null)
                {
                    try
                    {
                        WebpageHelper.openWebpage(new URL(version.getUpdateURL()));
                    }
                    catch (MalformedURLException e)
                    {
                        e.printStackTrace();
                        Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText("ERROR: Version URL was corrupt"));
                    }
                }
                else
                {
                    Minecraft.getMinecraft().thePlayer.sendChatToPlayer(ChatMessageComponent.createFromText("Version Checker has not initialized"));
                }
            }
            if (commandName.equalsIgnoreCase(Commands.CHANGE_LOG))
            {
                VersionContainer.Version version = VersionChecker.getRemoteVersion();
                if (version != null)
                {
                    Minecraft.getMinecraft().thePlayer.sendChatToPlayer(new ChatMessageComponent().addText(version.getChangeLog()));
                }
                else
                {
                    Minecraft.getMinecraft().thePlayer.sendChatToPlayer(new ChatMessageComponent().addText("Version Checker has not initialized"));
                }
            }
        }
    }

    @Override
    public List addTabCompletionOptions(ICommandSender commandSender, String[] args)
    {
        switch (args.length)
        {
            case 1:
            {
                return getListOfStringsMatchingLastWord(args, Commands.LATEST, Commands.CHANGE_LOG);
            }
        }
        return null;
    }

    @SuppressWarnings("NullableProblems")
    @Override
    public int compareTo(Object obj)
    {
        if (obj instanceof ICommand)
        {
            return this.compareTo((ICommand) obj);
        }
        else
        {
            return 0;
        }
    }
}
