package com.andcool.options;

import com.andcool.MainClient;
import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class ModMenuIntegration implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (MainClient.yetAnotherConfigLibV3) {
            return ConfigScreen::buildScreen;
        } else {
            return null;
        }
    }
}