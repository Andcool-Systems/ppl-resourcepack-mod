package com.andcool;

import com.andcool.config.UserConfig;
import com.andcool.loader.Loader;
import com.andcool.rpManager.rpManager;
import com.google.gson.JsonObject;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.util.Objects;

@Environment(EnvType.CLIENT)
public class MainClient implements ClientModInitializer {
    public static String name = "PPL pack updater";
    public static final Logger LOGGER = LogManager.getLogger(name);
    public static Boolean yetAnotherConfigLibV3 = FabricLoader.getInstance().getModContainer("yet_another_config_lib_v3").isPresent();
    public static String titleScreenMessage = "";
    private static String message = "";
    private static int retries = 2;
    private static boolean working = false;
    private static boolean firstload = true;


    public static void betterLog(Level level, String message) {
        LOGGER.log(level, String.format("[%s]: %s", name, message));
    }

    @Override
    public void onInitializeClient() {
        ScreenEvents.AFTER_INIT.register((client, screen, scaledWidth, scaledHeight) -> {
            if (screen instanceof TitleScreen && firstload) {
                firstload = false;
                UserConfig.load();

                if (UserConfig.ENABLE) {
                    downloadPack();
                    ClientTickEvents.START_CLIENT_TICK.register(this::onClientStarted);
                }
            }
        });
    }

    public static void downloadPack() {
        retries = UserConfig.RETRIES;
        Thread downloadThread = new Thread(() -> {
            try {
                if (MainClient.working) return;
                MainClient.working = true;

                titleScreenMessage = String.format("[%s] Получение информации о ресурспаке...", name);
                JsonObject apiResponse = Loader.fetch();  // Fetch resource pack data from API
                JsonObject packData = apiResponse.get(UserConfig.ONLY_EMOTES ? "emotes" : "main").getAsJsonObject();

                String version = packData.get("version").getAsString();
                String url = packData.get("url").getAsString();
                String originalChecksum = packData.get("checksum").getAsString();
                String date = packData.get("lastModified").getAsJsonObject().get("ru").getAsString();

                String filename = String.format("pepeland_%s_%s.zip", version, UserConfig.ONLY_EMOTES ? "emotes" : "main");

                boolean initiallyEnabled = rpManager.is_enabled();
                File file = new File("./resourcepacks/" + filename);

                if (
                        version.equals(UserConfig.VERSION) &&
                        file.exists() &&
                        originalChecksum.equals(Loader.toSHA("./resourcepacks/" + filename))
                ) {
                    betterLog(Level.INFO, "Pack already up to date");
                    titleScreenMessage = "";
                    return;
                }

                while (retries + 1 > 0) {
                    betterLog(Level.INFO, "Downloading resourcepack...");
                    if (retries == UserConfig.RETRIES) {
                        titleScreenMessage = String.format("[%s] Получение ресурспака...", name);
                    }

                    Loader.download_file(url, "./resourcepacks/", filename);  // Download new
                    String checksum = Loader.toSHA("./resourcepacks/" + filename);
                    if (originalChecksum.equals(checksum)) {
                        break;
                    }
                    betterLog(Level.WARN, "Checksums didn't math! Retrying...");
                    --retries;

                    if (retries == 0) {
                        titleScreenMessage = String.format("[%s] Не удалось получить пак (файл повреждён)", name);
                        return;
                    } else {
                        titleScreenMessage = String.format("[%s] Не удалось получить пак (файл повреждён)\nОсталось попыток: %s", name, retries);
                    }
                }

                if (initiallyEnabled || Objects.equals(UserConfig.VERSION, "null")) {
                    rpManager.disableAll();  // Disable older resourcepacks
                    rpManager.enable_resourcepack_and_reload(filename);  // Enable new resource pack
                }
                message = String.format("[%s] Ресурспак обновлён до версии %s (%s)", name, version, date);
                titleScreenMessage = "";

                UserConfig.VERSION = version;
                UserConfig.save();

            } catch (Exception e) {
                betterLog(Level.ERROR, "Error:" + e);
                titleScreenMessage = String.format("[%s] Не удалось скачать пак", name);
            } finally {
                MainClient.working = false;
            }
        });

        downloadThread.start();  // Start background thread
    }


    private void onClientStarted(MinecraftClient client) {
        if (client.world != null && !message.isEmpty() && client.inGameHud != null) {
            client.inGameHud.getChatHud().addMessage(Text.of(message));
            message = "";
        }
    }
}