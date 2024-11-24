package com.github.shap_po.pencilgames.client.ui.menu;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.util.MenuPanel;
import com.github.shap_po.pencilgames.client.ui.util.NumberField;
import com.github.shap_po.pencilgames.server.game.ServerGameFactoryRegistry;
import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.util.Identifier;

import javax.swing.*;

public class HostMenu extends MenuPanel {
    public HostMenu(GameWindow root) {
        super(root);

        add(new JLabel("Port:"));
        NumberField portField = new NumberField(String.valueOf(ConnectionHandler.DEFAULT_PORT), 10);
        add(portField);

        JComboBox<Identifier> gameTypes = new JComboBox<>();
        for (Identifier id : ServerGameFactoryRegistry.REGISTRY.getKeys()) {
            gameTypes.addItem(id);
        }
        add(gameTypes);

        addButton("Host", e -> {
            int port = portField.getValue();
            Identifier gameType = (Identifier) gameTypes.getSelectedItem();
            PencilGamesClient.LOGGER.info("Hosting {} game at {}", gameType, port);
        });

        addBackButton();
    }
}
