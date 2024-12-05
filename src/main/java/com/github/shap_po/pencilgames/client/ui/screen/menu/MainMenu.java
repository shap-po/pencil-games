package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.ui.Application;

import javax.swing.*;

public class MainMenu extends MenuScreen {
    public MainMenu(Application root) {
        super(root, false);
    }

    @Override
    protected void populate() {
        add(new JLabel("Pencil Games"));

        addButton("Join Game", e -> {
            root.setContentState(Application.ScreenState.JOIN_MENU);
        });
        addButton("Host Game", e -> {
            root.setContentState(Application.ScreenState.HOST_MENU);
        });
        addButton("Settings", e -> {
            root.setContentState(Application.ScreenState.SETTINGS_MENU);
        });
        addButton("Exit", e -> {
            System.exit(0);
        });
    }
}
