package com.andcool.mixins;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import com.andcool.MainClient;

@Mixin(TitleScreen.class)
public abstract class Title extends Screen {
    protected Title(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void drawText(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TextRenderer text_renderer = MinecraftClient.getInstance().textRenderer;
        context.drawText(text_renderer, MainClient.title_screen_message, 5, 5, 0xffffff, false);
    }
}
