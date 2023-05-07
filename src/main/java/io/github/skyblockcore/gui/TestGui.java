package io.github.skyblockcore.gui;

import io.github.skyblockcore.gui.elements.GuiElement;

public class TestGui extends TexturedGui {

    public TestGui() {
        super("gui/globaltradesystem");
    }

    @Override
    public void elementClicked(double mouseX, double mouseY, int button, GuiElement element) {
        System.out.println("Clicked element with id: " + element.getId());
    }
}
