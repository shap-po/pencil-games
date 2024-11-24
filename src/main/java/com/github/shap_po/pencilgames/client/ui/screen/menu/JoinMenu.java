package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.util.MenuPanel;
import com.github.shap_po.pencilgames.client.ui.util.NumberField;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;

import javax.swing.*;

public class JoinMenu extends MenuPanel {
    public JoinMenu(GameWindow root) {
        super(root);

        add(new JLabel("IP:"));
        JTextField ipField = new JTextField("localhost", 10);
        add(ipField);

        add(new JLabel("Port:"));
        NumberField portField = new NumberField(String.valueOf(ConnectionHandler.DEFAULT_PORT), 10);
        add(portField);

        addButton("Join", e -> {
            String ip = ipField.getText();
            int port = portField.getValue();
            PencilGamesClient.LOGGER.info("Joining game at {}:{}", ip, port);
        });

        addBackButton();
    }
}
