package com.andcool;

import com.andcool.config.UserConfig;
import com.andcool.loader.Loader;
import com.andcool.rpManager.rpManager;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


@Environment(EnvType.CLIENT)
public class MainClient implements ClientModInitializer {
    public static String name = "Ppl updater";
    public static final Logger LOGGER = LoggerFactory.getLogger(name);
    public static String FILE_NAME = "pepeland.zip";
    public static Boolean yetAnotherConfigLibV3 = FabricLoader.getInstance().getModContainer("yet_another_config_lib_v3").isPresent();
    public static String titleScreenMessage = "";
    private static String message = "";
    private static int retries = 2;

    @Override
    public void onInitializeClient() {
        UserConfig.load();

        retries = UserConfig.RETRIES;
        Thread downloadThread = new Thread(() -> {
            try {
                titleScreenMessage = String.format("[%s] Получение информации о ресурспаке...", name);
                JSONObject apiResponse = Loader.fetch();  // Fetch resource pack data from API
                JSONObject packData = apiResponse.getJSONObject(UserConfig.ONLY_EMOTES ? "emotes" : "main");

                String version = packData.getString("version");
                String url = packData.getString("url");
                String originalChecksum = packData.getString("checksum");
                String date = packData.getJSONObject("lastModified").getString("ru");
                boolean initiallyEnabled = rpManager.is_enabled("file/" + FILE_NAME);
                if (version.equals(UserConfig.VERSION)) {
                    LOGGER.info("Pack already up to date");
                    titleScreenMessage = "";
                    return;
                }

                while (retries + 1 > 0) {
                    LOGGER.info("Downloading resourcepack...");
                    if (retries == UserConfig.RETRIES) {
                        titleScreenMessage = String.format("[%s] Получение ресурспака...", name);
                    }

                    rpManager.delete("./resourcepacks/" + FILE_NAME);  // Delete old resourcepack
                    Loader.download_file(url, "./resourcepacks/", FILE_NAME);  // Download new
                    String checksum = Loader.toSHA("./resourcepacks/" + FILE_NAME);
                    if (originalChecksum.equals(checksum)) {
                        break;
                    }
                    LOGGER.warn("Checksums didn't math! Retrying...");
                    --retries;

                    if (retries == 0) {
                        titleScreenMessage = String.format("[%s] Не удалось получить пак (файл повреждён)", name);
                        return;
                    } else {
                        titleScreenMessage = String.format("[%s] Не удалось получить пак (файл повреждён)\nОсталось попыток: %s", name, retries);
                    }
                }

                if (initiallyEnabled || Objects.equals(UserConfig.VERSION, "null")) {
                    rpManager.enable_resourcepack_and_reload("file/" + FILE_NAME);  // Enable new resource pack
                    rpManager.enable_in_otions("file/" + FILE_NAME); // enable resourcepack in options.txt if not
                }
                message = String.format("[%s] Ресурспак обновлён до версии %s (%s)", name, version, date);
                titleScreenMessage = "";

                UserConfig.VERSION = version;
                UserConfig.save();

            } catch (Exception e) {
                LOGGER.error("Error:" + e);
                titleScreenMessage = String.format("[%s] Не удалось скачать пак: %s", name, e);
            }
        });
        if (UserConfig.ENABLE) {
            downloadThread.start();  // Start background thread
            ClientTickEvents.START_CLIENT_TICK.register(this::onClientStarted);
        }
    }

    private void onClientStarted(@NotNull MinecraftClient client) {
        if (client.world != null && !message.isEmpty() && client.inGameHud != null) {
            client.inGameHud.getChatHud().addMessage(Text.of(message));
            message = "";
        }
    }
}