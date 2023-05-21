package io.github.skyblockcore.gui.elements;

import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class TextureElement extends GuiElement {

    final int textureX, textureY, offsetX, offsetY, sizeX, sizeY;
    final Identifier texture;

    public TextureElement(int textureX, int textureY, int textureSizeX, int textureSizeY, int offsetX, int offsetY, Identifier texture, String id) {
        super(id);
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.textureX = textureX;
        this.textureY = textureY;
        this.sizeX = textureSizeX;
        this.sizeY = textureSizeY;
        this.texture = texture;
    }

    @Override
    public void render(MatrixStack matrices, int defaultX, int defaultY, int textureWidth, int textureHeight, int mouseX, int mouseY, boolean isMouseDown) {
        drawImage(matrices, defaultX + offsetX, defaultY + offsetY, textureX, textureY, sizeX, sizeY, sizeX, sizeY, textureWidth, textureHeight, texture);
    }

}
