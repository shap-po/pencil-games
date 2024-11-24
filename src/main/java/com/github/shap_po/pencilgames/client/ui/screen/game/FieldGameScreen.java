package com.github.shap_po.pencilgames.client.ui.screen.game;

import com.github.shap_po.pencilgames.client.game.abc.field.ClientFieldGame;
import com.github.shap_po.pencilgames.client.ui.GameWindow;

import javax.swing.*;

public class FieldGameScreen<C> extends GameScreen<ClientFieldGame<C>> {
    public FieldGameScreen(GameWindow root, ClientFieldGame<C> game) {
        super(root, game);

        JPanel panel = new JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));
        for (int y = 0; y < game.getGameField().getHeight(); y++) {
            JPanel row = new JPanel();
            row.setLayout(new javax.swing.BoxLayout(row, javax.swing.BoxLayout.X_AXIS));
            for (int x = 0; x < game.getGameField().getWidth(); x++) {
                C value = game.getGameField().get(x, y);
                String text = value == null ? "" : value.toString();

                JButton button = new JButton(text);

                int finalX = x;
                int finalY = y;

                button.addActionListener(e -> game.move(finalX, finalY));

                row.add(button);
            }
            panel.add(row);
        }

        this.root.add(panel);
    }
}
