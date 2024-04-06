package com.andcool;

import com.andcool.loader.Loader;
import com.andcool.rpManager.rpManager;
import com.andcool.config.UserConfig;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.loader.api.FabricLoader;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class MainClient implements ClientModInitializer {
	public static String name = "Resource pack updater";
    public static final Logger LOGGER = LoggerFactory.getLogger(name);
	public static Boolean yetAnotherConfigLibV3 = FabricLoader.getInstance().getModContainer("yet_another_config_lib_v3").isPresent();
	public static String title_screen_message = "";
	private static String message = "";

	@Override
	public void onInitializeClient() {
		UserConfig.load();

		Thread downloadThread = new Thread(() -> {
			try {
				title_screen_message = String.format("[%s] Получение информации о ресурспаке...", name);
				JSONObject api_response = Loader.fetch();  // Fetch resource pack data from API
				String status = api_response.getString("status");
				if (!status.equals("success")) {
					LOGGER.error("Failed to fetch actual data");
					title_screen_message = "";
					return;
				}

				String version = api_response.getString("version");
				String link = api_response.getString("link");
				if (version.equals(UserConfig.VERSION)) {
					LOGGER.info("Pack already up to date");
					title_screen_message = "";
					return;
				}

				LOGGER.info("Downloading resourcepack...");
				title_screen_message = String.format("[%s] Получение ресурспака...", name);
				rpManager.delete("./resourcepacks/pepeland.zip");  // Delete old resourcepack
				Loader.download_file(link, "./resourcepacks/");  // Download new
				rpManager.enable_resourcepack_and_reload("file/pepeland.zip");  // Enable new resource pack
				message = "[PepeLand resourcepack] Ресурспак обновлён до версии " + version;
				title_screen_message = "";
				//UserConfig.VERSION = version;
				//UserConfig.save();

			} catch (IOException | InterruptedException e) {
				LOGGER.error("Error:" + e);
				title_screen_message = String.format("[%s] Не удалось скачать пак: %s", name, e);
			}
        });
		if (UserConfig.ENABLE){
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