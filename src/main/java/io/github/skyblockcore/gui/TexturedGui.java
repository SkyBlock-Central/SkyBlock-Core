package io.github.skyblockcore.gui;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.skyblockcore.SkyblockCore;
import io.github.skyblockcore.gui.elements.*;
import net.fabricmc.fabric.api.client.screen.v1.ScreenMouseEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class TexturedGui extends Screen {

    private final String texturePath;

    public TexturedGui(String path) {
        super(Text.literal(path));
        this.texturePath = path;
    }

    boolean isMouseDown = false;

    private Identifier getIdentifier(String path) {
        return new Identifier("skyblock:textures/" + path + ".png");
    }

    @Override
    public void init() {
        ScreenMouseEvents.beforeMouseRelease(this).register(((screen, mouseX, mouseY, button) -> isMouseDown = false));

        InputStream is = SkyblockCore.class.getClassLoader().getResourceAsStream("assets/skyblock/textures/" + texturePath + ".json");
        assert is != null;
        JsonObject root = (JsonObject) JsonParser.parseReader(new InputStreamReader(is));

        try { is.close(); } catch (IOException e) { throw new RuntimeException(e); }

        if (root == null) return;
        JsonObject vars = root.getAsJsonObject("variables");

        int width = vars.getAsJsonArray("gui_size").get(0).getAsInt();
        int height = vars.getAsJsonArray("gui_size").get(1).getAsInt();

        int textureWidth = vars.getAsJsonArray("texture_size").get(0).getAsInt();
        int textureHeight = vars.getAsJsonArray("texture_size").get(1).getAsInt();

        int defaultOffsetX = vars.getAsJsonArray("default_offset").get(0).getAsInt();
        int defaultOffsetY = vars.getAsJsonArray("default_offset").get(1).getAsInt();

        assert client != null;
        setGuiData(width, height, defaultOffsetX, defaultOffsetY, textureWidth, textureHeight, getIdentifier(texturePath));

        JsonArray components = root.getAsJsonArray("components");

        for (JsonElement c : components) {
            JsonObject component = c.getAsJsonObject();
            if (component.get("type").getAsString().equalsIgnoreCase("texture")) {
                int offsetX = component.getAsJsonArray("position_offset").get(0).getAsInt();
                int offsetY = component.getAsJsonArray("position_offset").get(1).getAsInt();

                int textureX = component.getAsJsonArray("texture_position").get(0).getAsInt();
                int textureY = component.getAsJsonArray("texture_position").get(1).getAsInt();

                int sizeX = component.getAsJsonArray("texture_size").get(0).getAsInt();
                int sizeY = component.getAsJsonArray("texture_size").get(1).getAsInt();

                Identifier texture = component.has("texture") ? getIdentifier(component.get("texture").getAsString()) : gui_texture;

                addElement(new TextureElement(textureX, textureY, sizeX, sizeY, offsetX, offsetY, texture));
            }
            else if (component.get("type").getAsString().equalsIgnoreCase("button")) {
                int sizeX = component.getAsJsonArray("texture_size").get(0).getAsInt();
                int sizeY = component.getAsJsonArray("texture_size").get(1).getAsInt();

                int x = component.getAsJsonArray("position_offset").get(0).getAsInt();
                int y = component.getAsJsonArray("position_offset").get(1).getAsInt();

                int clickedX = component.getAsJsonArray("clicked").get(0).getAsInt();
                int clickedY = component.getAsJsonArray("clicked").get(1).getAsInt();

                int hoverX = component.getAsJsonArray("hovered").get(0).getAsInt();
                int hoverY = component.getAsJsonArray("hovered").get(1).getAsInt();

                int normalX = component.getAsJsonArray("default").get(0).getAsInt();
                int normalY = component.getAsJsonArray("default").get(1).getAsInt();

                Identifier texture = component.has("texture") ? getIdentifier(component.get("texture").getAsString()) : gui_texture;

                addElement(new ButtonElement(normalX, normalY, hoverX, hoverY, clickedX, clickedY, x, y, sizeX, sizeY, texture));
            }
            else if (component.get("type").getAsString().equalsIgnoreCase("text")) {
                String value = component.get("value").getAsString();
                int offsetX = component.getAsJsonArray("position_offset").get(0).getAsInt();
                int offsetY = component.getAsJsonArray("position_offset").get(1).getAsInt();

                int r = component.getAsJsonArray("color").get(0).getAsInt();
                int g = component.getAsJsonArray("color").get(1).getAsInt();
                int b = component.getAsJsonArray("color").get(2).getAsInt();

                addElement(new TextElement(value, offsetX, offsetY, new Color(r, g, b).getRGB()));
            }
            else if (component.get("type").getAsString().equalsIgnoreCase("switch")) {
                int sizeX = component.getAsJsonArray("texture_size").get(0).getAsInt();
                int sizeY = component.getAsJsonArray("texture_size").get(1).getAsInt();

                int x = component.getAsJsonArray("position_offset").get(0).getAsInt();
                int y = component.getAsJsonArray("position_offset").get(1).getAsInt();

                int enabledX = component.getAsJsonArray("enabled").get(0).getAsInt();
                int enabledY = component.getAsJsonArray("enabled").get(1).getAsInt();

                int disabledX = component.getAsJsonArray("disabled").get(0).getAsInt();
                int disabledY = component.getAsJsonArray("disabled").get(1).getAsInt();

                boolean state = component.has("start_state") && component.get("start_state").getAsBoolean();

                Identifier texture = component.has("texture") ? getIdentifier(component.get("texture").getAsString()) : gui_texture;

                addElement(new SwitchElement(enabledX, enabledY, disabledX, disabledY, x, y, sizeX, sizeY, texture, state));
            }
            else {
                throw new IllegalArgumentException("Unknown component type.");
            }
        }

    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        super.keyPressed(keyCode, scanCode, modifiers);

        return true;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        super.charTyped(chr, modifiers);

        return true;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        isMouseDown = true;

        getElements().forEach(guiElement -> {
            if (guiElement instanceof Interactable element) {
                if (isMouseOver(mouseX, mouseY, element.getScreenX(this), element.getScreenY(this), element.getSizeX(), element.getSizeY())) return;
                element.interact();
            }
        });
        return true;
    }

    static boolean isMouseOver(double mouseX, double mouseY, int elementX, int elementY, int sizeX, int sizeY) {
        return (
                mouseX >= elementX && mouseX <= elementX + sizeX
                        &&
                        mouseY >= elementY && mouseY <= elementY + sizeY
        );
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
        renderElements(matrices, mouseX, mouseY, isMouseDown);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    private Identifier gui_texture = null;
    private java.util.List<GuiElement> elements;
    public int defaultX;
    public int defaultY;
    private int textureSizeX;
    private int textureSizeY;

    private void setGuiData(int width, int height, int offsetX, int offsetY, int sizeX, int sizeY, Identifier img) {
        MinecraftClient client = MinecraftClient.getInstance();
        elements = new ArrayList<>();
        defaultX = client.getWindow().getScaledWidth() / 2 - width / 2 + offsetX;
        defaultY = client.getWindow().getScaledHeight() / 2 - height / 2 + offsetY;
        textureSizeX = sizeX;
        textureSizeY = sizeY;
        gui_texture = img;
    }

    public void renderElements(MatrixStack matrixStack, int mouseX, int mouseY, boolean isMouseDown) {
        elements.forEach((guiElement -> guiElement.render(matrixStack, defaultX, defaultY, textureSizeX, textureSizeY, mouseX, mouseY, isMouseDown)));
    }

    public void addElement(GuiElement element) { elements.add(element); }

    public List<GuiElement> getElements() { return elements; }

}

