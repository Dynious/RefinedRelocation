package com.dynious.refinedrelocation.command;

import com.dynious.refinedrelocation.helper.MiscHelper;
import com.dynious.refinedrelocation.lib.Commands;
import com.dynious.refinedrelocation.lib.Strings;
import com.dynious.refinedrelocation.network.NetworkHandler;
import com.dynious.refinedrelocation.network.packet.MessageKonga;
import com.dynious.refinedrelocation.util.VersionChecker;
import com.dynious.refinedrelocation.util.VersionContainer;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class CommandRefinedRelocation extends CommandBase
{
    @Override
    public String getCommandName()
    {
        return Commands.REFINED_RELOCATION;
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender)
    {
        return "/" + getCommandName() + " " + Commands.HELP;
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

            if (commandName.equalsIgnoreCase(Commands.HELP))
            {
                icommandsender.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.COMMAND_FORMAT, getCommandName())));
                icommandsender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(Strings.COMMAND_AVAILABLE)));
                icommandsender.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.COMMAND_HELP_LATEST, Commands.LATEST)));
                icommandsender.addChatMessage(new ChatComponentText(StatCollector.translateToLocalFormatted(Strings.COMMAND_HELP_CHANGELOG, Commands.CHANGE_LOG)));
            }
            else if (commandName.equalsIgnoreCase(Commands.LATEST))
            {
                VersionContainer.Version version = VersionChecker.getRemoteVersion();
                if (version != null)
                {
                    try
                    {
                        MiscHelper.openWebpage(new URL(version.getUpdateURL()));
                    } catch (MalformedURLException e)
                    {
                        e.printStackTrace();
                        icommandsender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(Strings.COMMAND_CORRUPT_URL)));
                    }
                }
                else
                {
                    icommandsender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(Strings.COMMAND_VERSION_UNINTIALIZED)));
                }
            }
            else if (commandName.equalsIgnoreCase(Commands.CHANGE_LOG))
            {
                VersionContainer.Version version = VersionChecker.getRemoteVersion();
                if (version != null)
                {
                    icommandsender.addChatMessage(new ChatComponentText(version.getChangeLog()));
                }
                else
                {
                    icommandsender.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(Strings.COMMAND_VERSION_UNINTIALIZED)));
                }
            }
            else if (commandName.equalsIgnoreCase(Commands.KONGA))
            {
                if (args.length > 1 && args[1].equalsIgnoreCase(Commands.ALL))
                {
                    if (icommandsender.getCommandSenderName().equalsIgnoreCase("Dynious"))
                    {
                        NetworkHandler.INSTANCE.sendToAll(new MessageKonga());
                    }
                }
                else
                {
                    NetworkHandler.INSTANCE.sendTo(new MessageKonga(), (EntityPlayerMP) icommandsender);
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
