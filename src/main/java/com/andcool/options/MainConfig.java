package com.andcool.options;

import com.andcool.config.UserConfig;
import dev.isxander.yacl3.api.ConfigCategory;
import dev.isxander.yacl3.api.Option;
import dev.isxander.yacl3.api.OptionDescription;
import dev.isxander.yacl3.impl.controller.BooleanControllerBuilderImpl;
import dev.isxander.yacl3.impl.controller.IntegerSliderControllerBuilderImpl;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;

@Environment(EnvType.CLIENT)
public class MainConfig {
    public ConfigCategory getCategory() {
        UserConfig.load();
        ConfigCategory.Builder category = ConfigCategory.createBuilder()
                .name(Text.of("Основные"));
        category.option(Option.createBuilder(boolean.class)
                .description(OptionDescription.createBuilder().text(Text.of("Текущая версия ресурспака")).build())
                .name(Text.of("Текущая версия: " + UserConfig.VERSION))
                .binding(false, () -> false, newVal -> {
                })
                .controller(BooleanControllerBuilderImpl::new)
                .available(false)
                .build());
        category.option(Option.createBuilder(boolean.class)
                .description(OptionDescription.createBuilder()
                        .text(Text.of("Проверять наличие обновлений и обновлять ресурспак при каждом входе в Minecraft."))
                        .build())
                .name(Text.of("Включить автообновление"))
                .binding(true, () -> UserConfig.ENABLE, (newVal) -> {
                    UserConfig.ENABLE = newVal;
                    UserConfig.dirty = true;
                })
                .controller(BooleanControllerBuilderImpl::new)
                .build());
        category.option(Option.createBuilder(boolean.class)
                .description(OptionDescription.createBuilder()
                        .text(Text.of("Загружать пак, содержащий только смайлы."))
                        .build())
                .name(Text.of("Только смайлы"))
                .binding(false, () -> UserConfig.ONLY_EMOTES, newVal -> {
                    UserConfig.ONLY_EMOTES = newVal;
                    UserConfig.VERSION = "null";
                    UserConfig.dirty = true;
                })
                .controller(BooleanControllerBuilderImpl::new)
                .build());
        category.option(Option.createBuilder(int.class)
                .description(OptionDescription.createBuilder().text(Text.of("Количество попыток скачать ресурспак.")).build())
                .name(Text.of("Количество попыток"))
                .binding(2, () -> UserConfig.RETRIES, newVal -> UserConfig.RETRIES = newVal)
                .controller(opt -> new IntegerSliderControllerBuilderImpl(opt).range(1, 10).step(1))
                .build());
        return category.build();
    }
}