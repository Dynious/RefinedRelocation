package com.dynious.refinedrelocation.command;

import com.dynious.refinedrelocation.helper.WebpageHelper;
import com.dynious.refinedrelocation.lib.Commands;
import com.dynious.refinedrelocation.version.VersionChecker;
import com.dynious.refinedrelocation.version.VersionContainer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;

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
        return "/" + this.getCommandName() + " " + Commands.HELP;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] args)
    {
        if (args.length > 0)
        {
            String commandName = args[0];
            System.arraycopy(args, 1, args, 0, args.length - 1);

            if (commandName.equalsIgnoreCase(Commands.HELP))
            {
                icommandsender.addChatMessage(new ChatComponentText("Format: '" + this.getCommandName() + " <command> <arguments>'"));
                icommandsender.addChatMessage(new ChatComponentText("Available commands:"));
                icommandsender.addChatMessage(new ChatComponentText("- latest : Download latest"));
                icommandsender.addChatMessage(new ChatComponentText("- changelog : Show latest changelog"));
            }
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
                        icommandsender.addChatMessage(new ChatComponentText("ERROR: Version URL was corrupt"));
                    }
                }
                else
                {
                    icommandsender.addChatMessage(new ChatComponentText("Version Checker has not initialized"));
                }
            }
            if (commandName.equalsIgnoreCase(Commands.CHANGE_LOG))
            {
                VersionContainer.Version version = VersionChecker.getRemoteVersion();
                if (version != null)
                {
                    icommandsender.addChatMessage(new ChatComponentText(version.getChangeLog()));
                }
                else
                {
                    icommandsender.addChatMessage(new ChatComponentText("Version Checker has not initialized"));
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
                return getListOfStringsMatchingLastWord(args, Commands.HELP, Commands.LATEST, Commands.CHANGE_LOG);
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
