package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.Application;

import java.awt.*;

public class WaitingMenu extends MenuScreen {
    public WaitingMenu(Application root) {
        super(root, false);
    }

    @Override
    protected void populate() {
        add(new Label("Waiting for the host to start the game..."));

        addButtonWithConfirm(
            "Disconnect",
            "Are you sure you want to disconnect from the server?",
            e -> {
                PencilGamesClient.CLIENT_LOBBY.disconnect();
            });
    }
}
