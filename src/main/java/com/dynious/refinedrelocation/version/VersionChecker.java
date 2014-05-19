package com.dynious.refinedrelocation.version;

import com.dynious.refinedrelocation.helper.LogHelper;
import com.dynious.refinedrelocation.lib.Reference;
import com.google.gson.Gson;
import cpw.mods.fml.common.Loader;
import org.apache.commons.io.IOUtils;

import java.net.URL;

public class VersionChecker implements Runnable
{
    private static VersionChecker instance = new VersionChecker();

    private static Gson gson = new Gson();

    private static final int VERSION_CHECK_ATTEMPTS = 3;
    private static final String REMOTE_VERSION_FILE = "https://raw.github.com/Dynious/RefinedRelocation/master/version.json";

    private static CheckState result = CheckState.UNINITIALIZED;
    private static VersionContainer.Version remoteVersion = null;

    public static enum CheckState
    {
        UNINITIALIZED,
        CURRENT,
        OUTDATED,
        ERROR,
        FINAL_ERROR,
        MC_VERSION_NOT_FOUND
    }

    public static void checkVersion()
    {
        result = CheckState.UNINITIALIZED;
        try
        {
            String json = IOUtils.toString(new URL(REMOTE_VERSION_FILE));

            VersionContainer versionContainer = gson.fromJson(json, VersionContainer.class);
            VersionContainer.Version latest = versionContainer.getLatestFromMcVersion(Loader.instance().getMCVersionString());

            if (latest != null)
            {
                remoteVersion = latest;

                if (remoteVersion.getModVersion().equalsIgnoreCase(Reference.VERSION))
                {
                    result = CheckState.CURRENT;
                }
                else
                {
                    result = CheckState.OUTDATED;
                }
            }
            else
            {
                result = CheckState.MC_VERSION_NOT_FOUND;
            }
        }
        catch (Exception ignored)
        {
            ignored.printStackTrace();
        }
        finally
        {
            if (result == CheckState.UNINITIALIZED)
            {
                result = CheckState.ERROR;
            }
        }
    }

    public static void logResult()
    {
        if (result == CheckState.CURRENT || result == CheckState.OUTDATED)
        {
            LogHelper.info(getResultMessage());
        }
        else
        {
            LogHelper.warning(getResultMessage());
        }
    }

    public static String getResultMessage()
    {
        if (result == CheckState.UNINITIALIZED)
        {
            return "Version Checker Status: UNINITIALIZED";
        }
        else if (result == CheckState.CURRENT)
        {
            return "Version Checker Status: CURRENT";
        }
        else if (result == CheckState.OUTDATED && remoteVersion != null)
        {
            return String.format("Version Checker Status: OUTDATED! Using %s, latest %s, changelog: %s", Reference.VERSION, remoteVersion.getModVersion(), remoteVersion.getChangeLog());
        }
        else if (result == CheckState.ERROR)
        {
            return "Version Checker Status: ERROR";
        }
        else if (result == CheckState.FINAL_ERROR)
        {
            return "Version Checker Status: ENDED WITH ERROR";
        }
        else if (result == CheckState.MC_VERSION_NOT_FOUND)
        {
            return "Version Checker Status: MC VERSION NOT SUPPORTED";
        }
        else
        {
            result = CheckState.ERROR;
            return "Version Checker Status: ERROR";
        }
    }

    public static CheckState getResult()
    {
        return result;
    }

    public static VersionContainer.Version getRemoteVersion()
    {
        return remoteVersion;
    }

    @Override
    public void run()
    {
        int count = 0;

        try
        {
            while (count < VERSION_CHECK_ATTEMPTS - 1 && (result == CheckState.UNINITIALIZED || result == CheckState.ERROR))
            {

                checkVersion();
                count++;
                logResult();

                if (result == CheckState.UNINITIALIZED || result == CheckState.ERROR)
                {
                    Thread.sleep(10000);
                }
            }

            if (result == CheckState.ERROR)
            {
                result = CheckState.FINAL_ERROR;
                logResult();
            }
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public static void execute()
    {
        new Thread(instance).start();
    }
}
