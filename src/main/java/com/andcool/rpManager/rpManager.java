package com.andcool.rpManager;


import com.andcool.MainClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;

import java.io.File;
import java.util.zip.ZipException;
import com.andcool.Main;

@Environment(EnvType.CLIENT)
public class rpManager {
    public static void enable_resourcepack_and_reload(String name) throws ZipException {
        ResourcePackManager resourcePackManager = MinecraftClient.getInstance().getResourcePackManager();
        resourcePackManager.scanPacks();
        ResourcePackProfile profile = resourcePackManager.getProfile(name);

        if (profile == null) return;
        resourcePackManager.enable(profile.getName());
        MainClient.LOGGER.info("Reloading...");
        MinecraftClient.getInstance().reloadResourcesConcurrently();
    }

    public static void delete(String path){
        File file = new File(path);
        if (file.delete()) {
            MainClient.LOGGER.info("Deleted old resourcepack");
            return;
        }
        MainClient.LOGGER.warn("Failed to delete old resourcepack");
    }
}
