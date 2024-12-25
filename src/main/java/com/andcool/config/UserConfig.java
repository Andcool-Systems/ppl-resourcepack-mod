package com.andcool.config;

import com.andcool.MainClient;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.JsonHelper;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Environment(EnvType.CLIENT)
public class UserConfig {
    public static String VERSION = "null";
    public static boolean ENABLE = true;
    public static int RETRIES = 2;
    public static boolean ONLY_EMOTES = false;
    public static boolean dirty = false;

    /*
    Save config to file
     */
    public static void save() {
        final File configFile = new File("config/pepeland/data.json");
        JsonObject jsonConfig = new JsonObject();
        jsonConfig.addProperty("VERSION", VERSION);
        jsonConfig.addProperty("ENABLE", ENABLE);
        jsonConfig.addProperty("RETRIES", RETRIES);
        jsonConfig.addProperty("ONLY_SMILES", ONLY_EMOTES);
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
            JsonObject jsonConfig = JsonHelper.deserialize(Files.readString(configFile.toPath()));
            VERSION = jsonConfig.get("VERSION").getAsString();
            ENABLE = jsonConfig.get("ENABLE").getAsBoolean();
            RETRIES = jsonConfig.get("RETRIES").getAsInt();
            ONLY_EMOTES = jsonConfig.get("ONLY_SMILES").getAsBoolean();
        } catch (IOException e) {
            MainClient.betterLog(Level.WARN, "IOException: " + e);
            save();
        }
    }
}