package com.andcool.mixins;

import com.andcool.MainClient;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.MultilineText;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public abstract class Title extends Screen {
    protected Title(Text title) {
        super(title);
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void drawText(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        TextRenderer text_renderer = MinecraftClient.getInstance().textRenderer;
        final MultilineText multilineText = MultilineText.create(text_renderer, Text.literal(MainClient.titleScreenMessage), width / 2);
        multilineText.draw(context, 5, 5, 16, 0xffffff);
        //context.drawText(text_renderer, MainClient.titleScreenMessage, 5, 5, 0xffffff, false);
    }
}
