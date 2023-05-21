package io.github.skyblockcore.gui.elements;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

public class TextElement extends GuiElement {
    final String text;
    final int x, y, color;

    public TextElement(String value, int x, int y, int color, String id) {
        super(id);
        this.text = value;
        this.x = x;
        this.y = y;
        this.color = color;
    }

    @Override
    public void render(MatrixStack matrices, int defaultX, int defaultY, int textureWidth, int textureHeight, int mouseX, int mouseY, boolean isMouseDown) {
        Screen.drawCenteredTextWithShadow(matrices, MinecraftClient.getInstance().textRenderer, text, x + defaultX, y + defaultY, color);
    }
}