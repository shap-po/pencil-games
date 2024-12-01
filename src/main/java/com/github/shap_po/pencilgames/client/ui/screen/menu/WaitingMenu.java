package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.GameWindow;

import java.awt.*;

public class WaitingMenu extends MenuScreen {
    public WaitingMenu(GameWindow root) {
        super(root, false);
    }

    @Override
    protected void populate() {
        add(new Label("Waiting for the host to start the game..."));

        addButtonWithConfirm(
            "Leave lobby",
            "Are you sure you want to leave the lobby?",
            e -> {
                PencilGamesClient.clientLobby.disconnect();
                root.back();
            });
    }
}
