package com.github.shap_po.pencilgames.client.ui.menu;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.util.NumberField;
import com.github.shap_po.pencilgames.client.ui.util.MenuPanel;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;

import javax.swing.*;

public class HostMenu extends MenuPanel {
    public HostMenu(GameWindow root) {
        super(root);

        add(new JLabel("Port:"));
        NumberField portField = new NumberField(String.valueOf(ConnectionHandler.DEFAULT_PORT), 10);
        add(portField);

        addButton("Host", e -> {
            int port = portField.getValue();
            PencilGamesClient.LOGGER.info("Hosting game at {}", port);
        });

        addBackButton();
    }
}
