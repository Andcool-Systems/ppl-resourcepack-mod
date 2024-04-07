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
                if (is_enabled(name)) {
                    break;
                }
                newResourcePacks.add("\"" + name + "\"");

                String newResourcePacksLine = "resourcePacks:[" + String.join(",", newResourcePacks) + "]";
                lines.set(i, newResourcePacksLine);
                break;
            }
        }
        Files.write(optionsFile, lines);
    }

    public static boolean is_enabled(String name) throws IOException {
        Path optionsFile = Path.of("./options.txt");
        List<String> lines = Files.readAllLines(optionsFile);
        boolean found = false;
        for (String line : lines) {
            if (line.startsWith("resourcePacks:[")) {
                String new_line = line.replace("\"", "");
                String[] currentResourcePacks = new_line.substring(new_line
                        .indexOf('[') + 1, new_line
                        .indexOf(']')).split(",");

                found = List.of(currentResourcePacks).contains(name);
                break;
            }
        }
        return found;
    }

    public static void enable_resourcepack_and_reload(String name) {
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
