package com.andcool.options;

import dev.isxander.yacl3.api.YetAnotherConfigLib;
import net.minecraft.client.gui.screen.Screen;
import com.andcool.Main;
import net.minecraft.text.Text;
import com.andcool.config.UserConfig;
import com.andcool.options.MainConfig;

public class ConfigScreen {
    public static Screen buildScreen (Screen currentScreen) {
        YetAnotherConfigLib.Builder screen = YetAnotherConfigLib.createBuilder()
                .title(Text.of("PepeLand resorce updater"))
                .save(ConfigScreen::save);
        screen.category(new MainConfig().getCategory());
        return screen.build().generateScreen(currentScreen);
    }

    private static void save(){
        Main.LOGGER.info("Saving settings...");
        UserConfig.save();
    }
}