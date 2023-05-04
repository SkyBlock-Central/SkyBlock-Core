package io.github.skyblockcore.gui.elements;

import io.github.skyblockcore.gui.TexturedGui;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class SwitchElement extends GuiElement implements Interactable {

    final int offsetX, offsetY, enabledX, enabledY, disabledX, disabledY, sizeX, sizeY;
    final Identifier texture;

    boolean enabled;

    public SwitchElement(int enabledX, int enabledY, int disabledX, int disabledY, int offsetX, int offsetY, int sizeX, int sizeY, Identifier texture, boolean startState) {
        this.enabledX = enabledX;
        this.enabledY = enabledY;
        this.disabledX = disabledX;
        this.disabledY = disabledY;
        this.offsetX = offsetX;
        this.offsetY = offsetY;
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.texture = texture;

        enabled = startState;
    }

    @Override
    public void render(MatrixStack matrices, int defaultX, int defaultY, int textureWidth, int textureHeight, int mouseX, int mouseY, boolean isMouseDown) {
        int actualX = defaultX + offsetX;
        int actualY = defaultY + offsetY;

        int textureX = enabled ? enabledX : disabledX;
        int textureY = enabled ? enabledY : disabledY;
        drawImage(matrices, actualX, actualY, textureX, textureY, sizeX, sizeY, sizeX, sizeY, textureWidth, textureHeight, texture);
    }

    @Override
    public int getScreenX(TexturedGui gui) {
        return gui.defaultX + offsetX;
    }

    @Override
    public int getScreenY(TexturedGui gui) {
        return gui.defaultY + offsetY;
    }

    @Override
    public int getSizeX() {
        return sizeX;
    }

    @Override
    public int getSizeY() {
        return sizeY;
    }

    @Override
    public void interact() {
        enabled = !enabled;
    }

}
