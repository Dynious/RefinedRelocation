package com.dynious.refinedrelocation.command;

import com.dynious.refinedrelocation.helper.LogHelper;
import com.google.gson.JsonSyntaxException;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import org.apache.commons.io.FileUtils;
import paulscode.sound.SoundSystem;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KongaHandler
{
    private static String kongaLoc = "konga" + File.separator + "konga.ogg";
    private static String kongaDl = "https://dl.dropbox.com/s/amdsf0512rda8t3/konga.ogg";
    private static String kongaIdentifier = "konga.ogg";
    private static SoundSystem soundSystem;
    private static ResourceLocation shader = new ResourceLocation("shaders/post/wobble.json");
    private static Future<File> future;

    public static void toggleKonga()
    {
        if (future != null)
            return;

        File file = new File(kongaLoc);

        file.getParentFile().mkdirs();

        if (soundSystem == null)
        {
            init();
        }

        if (soundSystem.playing(kongaIdentifier))
        {
            stopKonga();
            return;
        }

        if (!file.exists())
        {
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            future = executorService.submit(new KongaDownloader());
        }
        else
        {
            playKonga(file);
        }
    }

    public static void checkDownloadedAndPlay()
    {
        if (future != null && future.isDone())
        {
            File file = null;
            try
            {
                file = future.get();
                future = null;
            } catch (Exception e)
            {
                e.printStackTrace();
            }
            if (file != null)
            {
                playKonga(file);
            }
        }
    }

    private static void init()
    {
        SoundManager soundManager = ObfuscationReflectionHelper.getPrivateValue(net.minecraft.client.audio.SoundHandler.class, Minecraft.getMinecraft().getSoundHandler(), "sndManager", "field_147694_f", "V");
        soundSystem = ObfuscationReflectionHelper.getPrivateValue(SoundManager.class, soundManager, "sndSystem", "field_148620_e", "e");
    }

    private static void playKonga(File sound)
    {
        Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("-- ACTIVATING KONGA MODE --"));
        activateKongaShader();
        try
        {
            soundSystem.newStreamingSource(false, kongaIdentifier, sound.toURI().toURL(), sound.getName(), true, 0, 0, 0, 0, 16);
            soundSystem.play(kongaIdentifier);
        }
        catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
    }

    private static void stopKonga()
    {
        soundSystem.stop(kongaIdentifier);
        soundSystem.removeSource(kongaIdentifier);
        deactivateKongaShader();
    }

    private static void activateKongaShader()
    {
        EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
        if (OpenGlHelper.shadersSupported)
        {
            if (renderer.theShaderGroup != null)
            {
                renderer.theShaderGroup.deleteShaderGroup();
            }

            try
            {
                LogHelper.info("Selecting effect " + shader);
                renderer.theShaderGroup = new ShaderGroup(Minecraft.getMinecraft().getTextureManager(), Minecraft.getMinecraft().getResourceManager(), Minecraft.getMinecraft().getFramebuffer(), shader);
                renderer.theShaderGroup.createBindFramebuffers(Minecraft.getMinecraft().displayWidth, Minecraft.getMinecraft().displayHeight);
            }
            catch (IOException ioexception)
            {
                ioexception.printStackTrace();
                LogHelper.warning("Failed to load shader: " + shader);
            }
            catch (JsonSyntaxException jsonsyntaxexception)
            {
                jsonsyntaxexception.printStackTrace();
                LogHelper.warning("Failed to load shader: " + shader);
            }
        }
    }

    private static void deactivateKongaShader()
    {
        EntityRenderer renderer = Minecraft.getMinecraft().entityRenderer;
        if (renderer.theShaderGroup != null)
        {
            renderer.theShaderGroup.deleteShaderGroup();
            renderer.theShaderGroup = null;
        }
    }

    private static class KongaDownloader implements Callable<File>
    {
        @Override
        public File call()
        {
            File file = new File(kongaLoc);

            try
            {
                Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText("-- DOWNLOADING KONGA --"));
                FileUtils.copyURLToFile(new URL(kongaDl), file);
                LogHelper.info("-- FINISHED DOWNLOADING KONGA --");
                return file;
            } catch (java.io.IOException e)
            {
                e.printStackTrace();
            }
            return null;
        }
    }
}
