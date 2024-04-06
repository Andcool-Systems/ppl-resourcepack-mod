package com.andcool.config;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import com.andcool.MainClient;

@Environment(EnvType.CLIENT)
public class UserConfig {
    public static String VERSION = "null";
    public static boolean ENABLE = true;

    public static void save(){
        final File configFile = new File("config/pepeland/data.json");
        JSONObject jsonConfig = new JSONObject();
        jsonConfig.put("VERSION", VERSION);
        jsonConfig.put("ENABLE", ENABLE);
        try {
            Files.createDirectories(configFile.toPath().getParent());
            Files.writeString(configFile.toPath(), jsonConfig.toString());
        } catch (IOException e) {
            MainClient.LOGGER.error("IOException: " + e);
        }
    }

    public static void load(){
        final File configFile = new File("config/pepeland/data.json");
        try{
            JSONObject jsonConfig = new JSONObject(Files.readString(configFile.toPath()));
            for(String key : jsonConfig.keySet()){
                switch (key){
                    case "VERSION" -> VERSION = jsonConfig.getString(key);
                    case "ENABLE" -> ENABLE = jsonConfig.getBoolean(key);
                }
            }
        } catch (Exception e){
            MainClient.LOGGER.error("Exception: " + e);
            save();
        }
    }
}