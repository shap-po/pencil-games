package com.github.shap_po.pencilgames.client.ui.screen.game;

import com.github.shap_po.pencilgames.client.game.abc.field.ClientFieldGame;
import com.github.shap_po.pencilgames.client.ui.GameWindow;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FieldGameScreen<C> extends GameScreen<ClientFieldGame<C>> {
    private List<List<JButton>> buttons;

    public FieldGameScreen(GameWindow root, ClientFieldGame<C> game) {
        super(root, game);
    }

    @Override
    protected void populate() {
        JPanel panel = new JPanel();
        panel.setLayout(new javax.swing.BoxLayout(panel, javax.swing.BoxLayout.Y_AXIS));

        buttons = new ArrayList<>();

        for (int y = 0; y < game.getGameField().getHeight(); y++) {
            JPanel row = new JPanel();
            row.setLayout(new javax.swing.BoxLayout(row, javax.swing.BoxLayout.X_AXIS));

            List<JButton> rowButtons = new ArrayList<>();

            for (int x = 0; x < game.getGameField().getWidth(); x++) {
                C value = game.getGameField().get(x, y);
                String text = value == null ? "" : value.toString();

                JButton button = new JButton(text);

                int finalX = x;
                int finalY = y;

                button.addActionListener(e -> {
                    C result = game.move(finalX, finalY);

                    if (result != null) {
                        button.setText(result.toString());
                    }
                });

                row.add(button);
                rowButtons.add(button);

            }
            panel.add(row);
            buttons.add(rowButtons);
        }

        add(panel);
    }

    public void setCell(int x, int y, C newState) {
        buttons.get(y).get(x).setText(newState.toString());
    }
}
