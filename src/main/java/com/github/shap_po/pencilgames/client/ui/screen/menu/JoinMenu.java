package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.util.NumberField;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;

import javax.swing.*;
import java.io.IOException;

public class JoinMenu extends MenuScreen {
    public JoinMenu(GameWindow root) {
        super(root);
    }

    @Override
    protected void populate() {
        add(new JLabel("IP:"));
        JTextField ipField = new JTextField("localhost", 10);
        add(ipField);

        add(new JLabel("Port:"));
        NumberField portField = new NumberField(String.valueOf(ConnectionHandler.DEFAULT_PORT), 10);
        add(portField);

        addButton("Join", e -> {
            String ip = ipField.getText();
            int port = portField.getValue();

            try {
                PencilGamesClient.clientLobby.connect(ip, port);
            } catch (IOException ex) {
                PencilGamesClient.LOGGER.error("Failed to connect to server: {}", ex.getMessage());
            }
        });
    }
}
