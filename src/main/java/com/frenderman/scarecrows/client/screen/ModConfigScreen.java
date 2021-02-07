package com.frenderman.scarecrows.client.screen;

import com.frenderman.scarecrows.common.core.Scarecrows;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;

public class ModConfigScreen extends Screen {

    /**
     * Storing the forge config screen here
     * so we can return to it when the "No"
     * button is pressed.
     */
    private final Screen forgeConfigScreen;

    /**
     * Default color that Minecraft uses when
     * drawing Strings on a Screen.
     */
    private static final int standardColor = 16777215;


    public ModConfigScreen(Screen forgeConfigScreen) {
        super(new TranslationTextComponent(Scarecrows.MOD_ID + ".screen.config.title"));
        this.forgeConfigScreen = forgeConfigScreen;
    }

    @Override
    public void init(@Nonnull Minecraft client, int width, int height) {
        super.init(client, width, height);

        this.addButton(new Button((width / 2) - 30, this.height / 2, 60, 20, new TranslationTextComponent(Scarecrows.MOD_ID + ".screen.config.no_button"), button -> {
            minecraft.displayGuiScreen(this.forgeConfigScreen);
        }));
    }

    @Override
    public void render(@Nonnull MatrixStack matrixStack, int x, int y, float partialTicks) {
        this.renderBackground(matrixStack);
        drawCenteredString(matrixStack, font, this.getTitle(), width / 2, 30, standardColor);
        super.render(matrixStack, x, y, partialTicks);
    }
}
