package com.dynious.refinedrelocation.version;

import java.util.List;

public class VersionContainer
{
    public List<Version> versionList;

    public VersionContainer(List<Version> versionList)
    {
        this.versionList = versionList;
    }

    public Version getLatestFromMcVersion(String McVersion)
    {
        for (Version version : versionList)
        {
            if (version.getMcVersion().equalsIgnoreCase(McVersion))
            {
                return version;
            }
        }
        return null;
    }

    public static class Version
    {
        private String mcVersion;
        private String modVersion;
        private String changeLog;

        private Version(String mcVersion, String modVersion, String changeLog)
        {
            this.mcVersion = mcVersion;
            this.modVersion = modVersion;
            this.changeLog = changeLog;
        }

        public String getMcVersion()
        {
            return mcVersion;
        }

        public String getModVersion()
        {
            return modVersion;
        }

        public String getChangeLog()
        {
            return changeLog;
        }
    }
}
