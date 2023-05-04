package io.github.skyblockcore.gui.elements;

import io.github.skyblockcore.gui.TexturedGui;

public interface Interactable {

    int getScreenX(TexturedGui gui);
    int getScreenY(TexturedGui gui);

    int getSizeX();
    int getSizeY();

    void interact();

}
