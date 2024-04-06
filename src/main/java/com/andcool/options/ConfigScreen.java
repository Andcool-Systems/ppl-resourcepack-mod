package com.andcool.options;

import com.andcool.MainClient;
import com.andcool.config.UserConfig;
import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class ConfigScreen {
    public static Screen buildScreen(Screen currentScreen) {
        YetAnotherConfigLib.Builder screen = YetAnotherConfigLib.createBuilder()
                .title(Text.of("PepeLand resorce updater"))
                .save(ConfigScreen::save);
        screen.category(new MainConfig().getCategory());
        return screen.build().generateScreen(currentScreen);
    }

    private static void save() {
        MainClient.LOGGER.info("Saving settings...");
        UserConfig.save();
    }
}