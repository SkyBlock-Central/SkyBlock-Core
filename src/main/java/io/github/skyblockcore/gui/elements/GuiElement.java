package io.github.skyblockcore.gui.elements;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public abstract class GuiElement {

    private final String id;
    public GuiElement(String id) { this.id = id; }
    public String getId() { return id; }

    public abstract void render(MatrixStack matrices, int defaultX, int defaultY, int textureWidth, int textureHeight, int mouseX, int mouseY, boolean isMouseDown);
    /**
     * 
     * @param matrixStack the matrix stack used for rendering
     * @param screenX the X coordinate of the element
     * @param screenY the Y coordinate of the element
     * @param imgX the left-most coordinate of the texture region
     * @param imgY the top-most coordinate of the texture region
     * @param width the width of the element
     * @param height the height of the element
     * @param regionWidth width of the to-be-rendered region on the texture
     * @param regionHeight height of the to-be-rendered region on the texture
     * @param imgWidth the width of the entire texture
     * @param imgHeight the height of the entire texture
     * @param texture the identifier of the texture
     */
    public static void drawImage(MatrixStack matrixStack, int screenX, int screenY, int imgX, int imgY, int width, int height, int regionWidth, int regionHeight, int imgWidth, int imgHeight, Identifier texture) {
        RenderSystem.setShaderTexture(0, texture);
        Screen.drawTexture(matrixStack, screenX, screenY, width, height, imgX, imgY, regionWidth, regionHeight, imgWidth, imgHeight);
    }

}
