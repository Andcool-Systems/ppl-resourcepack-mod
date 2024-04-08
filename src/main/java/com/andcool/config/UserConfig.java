package com.andcool.config;

import com.andcool.MainClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.apache.logging.log4j.Level;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Environment(EnvType.CLIENT)
public class UserConfig {
    public static String VERSION = "null";
    public static boolean ENABLE = true;
    public static int RETRIES = 2;
    public static boolean ONLY_EMOTES = false;

    /*
    Save config to file
     */
    public static void save() {
        final File configFile = new File("config/pepeland/data.json");
        JSONObject jsonConfig = new JSONObject();
        jsonConfig.put("VERSION", VERSION);
        jsonConfig.put("ENABLE", ENABLE);
        jsonConfig.put("RETRIES", RETRIES);
        jsonConfig.put("ONLY_SMILES", ONLY_EMOTES);
        try {
            Files.createDirectories(configFile.toPath().getParent());
            Files.writeString(configFile.toPath(), jsonConfig.toString());
        } catch (IOException e) {
            MainClient.betterLog(Level.ERROR, "IOException: " + e);
        }
    }

    /*
    Load config from file
     */
    public static void load() {
        final File configFile = new File("config/pepeland/data.json");
        try {
            JSONObject jsonConfig = new JSONObject(Files.readString(configFile.toPath()));
            for (String key : jsonConfig.keySet()) {
                switch (key) {
                    case "VERSION" -> VERSION = jsonConfig.getString(key);
                    case "ENABLE" -> ENABLE = jsonConfig.getBoolean(key);
                    case "RETRIES" -> RETRIES = jsonConfig.getInt(key);
                    case "ONLY_SMILES" -> ONLY_EMOTES = jsonConfig.getBoolean(key);
                }
            }
        } catch (Exception e) {
            MainClient.LOGGER.warn("Exception: " + e);
            save();
        }
    }
}