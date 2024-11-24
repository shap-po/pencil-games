package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.util.MenuPanel;

import javax.swing.*;

public class MainMenu extends MenuPanel {
    public MainMenu(GameWindow root) {
        super(root);
        add(new JLabel("Pencil Games"));

        addButton("Join Game", e -> {
            root.setMenuState(GameWindow.State.JOIN_MENU);
        });
        addButton("Host Game", e -> {
            root.setMenuState(GameWindow.State.HOST_MENU);
        });
        addButton("Settings", e -> {
            root.setMenuState(GameWindow.State.SETTINGS_MENU);
        });
        addButton("Exit", e -> {
            System.exit(0);
        });
    }
}
