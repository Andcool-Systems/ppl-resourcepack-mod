package com.andcool.options;

import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.impl.controller.BooleanControllerBuilderImpl;
import com.andcool.config.UserConfig;
import net.minecraft.text.Text;

public class MainConfig {
    public ConfigCategory getCategory() {
        UserConfig.load();
        ConfigCategory.Builder category = ConfigCategory.createBuilder()
                .name(Text.of("Основные"));
        category.option(Option.createBuilder(boolean.class)
                .description(OptionDescription.createBuilder().text(Text.of("Текущая версия ресурспака")).build())
                .name(Text.of("Текущая версия: " + UserConfig.VERSION))
                .binding(false, () -> {return false;}, newVal -> {})
                .controller(BooleanControllerBuilderImpl::new)
                .available(false)
                .build());
        category.option(Option.createBuilder(boolean.class)
                .description(OptionDescription.createBuilder().text(Text.of("Проверять наличие обновлений и обновлять ресурспак при каждом входе в Minecraft.")).build())
                .name(Text.of("Включить автообновление"))
                .binding(true, () -> UserConfig.ENABLE, newVal -> UserConfig.ENABLE = newVal)
                .controller(BooleanControllerBuilderImpl::new)
                .build());
        return category.build();
    }
}