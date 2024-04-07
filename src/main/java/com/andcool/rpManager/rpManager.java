package com.andcool.rpManager;


import com.andcool.MainClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.zip.ZipException;
import java.util.List;

@Environment(EnvType.CLIENT)
public class rpManager {
    public static void enable_in_otions(String name) throws IOException {
        Path optionsFile = Path.of("./options.txt");
        List<String> lines = Files.readAllLines(optionsFile);

        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith("resourcePacks:[")) {
                String currentResourcePacksLine = lines.get(i);
                String[] currentResourcePacks = currentResourcePacksLine.substring(currentResourcePacksLine
                        .indexOf('[') + 1, currentResourcePacksLine
                        .indexOf(']')).split(",");

                List<String> newResourcePacks = new ArrayList<>(List.of(currentResourcePacks));
                if (newResourcePacks.contains(name)) {
                    return;
                }
                newResourcePacks.add("\"" + name + "\"");

                String newResourcePacksLine = "resourcePacks:[" + String.join(",", newResourcePacks) + "]";
                lines.set(i, newResourcePacksLine);
                break;
            }
        }
        Files.write(optionsFile, lines);
    }

    public static void enable_resourcepack_and_reload(String name) throws ZipException {
        ResourcePackManager resourcePackManager = MinecraftClient.getInstance().getResourcePackManager();
        resourcePackManager.scanPacks();
        ResourcePackProfile profile = resourcePackManager.getProfile(name);

        if (profile == null) return;
        resourcePackManager.enable(profile.getName());
        MainClient.LOGGER.info("Reloading...");
        resourcePackManager.scanPacks();
        MinecraftClient.getInstance().reloadResourcesConcurrently();
    }

    public static void delete(String path) {
        File file = new File(path);
        if (file.delete()) {
            MainClient.LOGGER.info("Deleted old resourcepack");
            return;
        }
        MainClient.LOGGER.warn("Failed to delete old resourcepack");
    }
}
