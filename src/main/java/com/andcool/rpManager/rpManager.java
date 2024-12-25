package com.andcool.rpManager;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackProfile;

@Environment(EnvType.CLIENT)
public class rpManager {

    public static boolean is_enabled() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.getResourcePackManager().scanPacks();

        for (ResourcePackProfile pack : minecraftClient.getResourcePackManager().getProfiles()) {
            if (pack.getDescription().getString().contains("PepeLand Pack"))
                return true;
        }

        return false;
    }

    public static void enable_resourcepack_and_reload(String filename) {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.getResourcePackManager().scanPacks();

        for (ResourcePackProfile pack : minecraftClient.getResourcePackManager().getProfiles()) {
            if (pack.getId().contains(filename))
                minecraftClient.getResourcePackManager().enable(pack.getId());
        }

        minecraftClient.options.refreshResourcePacks(minecraftClient.getResourcePackManager());
    }

    public static void disableAll() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        minecraftClient.getResourcePackManager().scanPacks();

        for (ResourcePackProfile pack : minecraftClient.getResourcePackManager().getProfiles()) {
            if (pack.getDescription().getString().contains("PepeLand Pack"))
                minecraftClient.getResourcePackManager().disable(pack.getId());
        }
    }
}
