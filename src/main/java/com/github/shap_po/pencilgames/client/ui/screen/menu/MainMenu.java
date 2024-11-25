package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.ui.GameWindow;

import javax.swing.*;

public class MainMenu extends MenuScreen {
    public MainMenu(GameWindow root) {
        super(root, false);
    }

    @Override
    protected void populate() {
        add(new JLabel("Pencil Games"));

        addButton("Join Game", e -> {
            root.setContentState(GameWindow.ScreenState.JOIN_MENU);
        });
        addButton("Host Game", e -> {
            root.setContentState(GameWindow.ScreenState.HOST_MENU);
        });
        addButton("Settings", e -> {
            root.setContentState(GameWindow.ScreenState.SETTINGS_MENU);
        });
        addButton("Exit", e -> {
            System.exit(0);
        });
    }
}
