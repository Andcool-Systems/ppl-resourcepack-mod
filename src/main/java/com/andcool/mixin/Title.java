package com.andcool.mixin;

import com.andcool.MainClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class Title {
    @Inject(method = "render", at = @At("RETURN"))
    private void drawText(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TextRenderer text_renderer = MinecraftClient.getInstance().textRenderer;
        final MultilineText multilineText = MultilineText.create(text_renderer, Text.literal(MainClient.titleScreenMessage), 1920 / 2);
        multilineText.draw(context, 5, 5, 16, 0xffffff);
    }
}
