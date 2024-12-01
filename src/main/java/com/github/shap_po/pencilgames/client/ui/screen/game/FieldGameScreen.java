package com.github.shap_po.pencilgames.client.ui.screen.game;

import com.github.shap_po.pencilgames.client.game.abc.field.ClientFieldGame;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class FieldGameScreen<C> extends GameScreen<ClientFieldGame<C>> {
    private List<List<JButton>> buttons;
    private TriConsumer<Integer, Integer, C> changeHandler = (x, y, c) -> {};

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
                JButton button = new JButton();

                int finalX = x;
                int finalY = y;

                button.addActionListener(e -> {
                    C result = game.move(finalX, finalY);
                    changeHandler.accept(finalX, finalY, result);
                });

                row.add(button);
                rowButtons.add(button);

            }
            panel.add(row);
            buttons.add(rowButtons);
        }

        game.getGameField().onChange.register(event -> {
            changeHandler.accept(event.x(), event.y(), event.newValue());
        });

        add(panel);
    }

    /**
     * Updates the buttons to match the current state of the game field.
     * This method should be called by game after the screen is initialized and the {@link #changeHandler} is set.
     * <p>
     * This method should not be used after each move, as cell changes are handled by the change handler automatically.
     */
    public void redraw() {
        game.getGameField().forEach((x, y, c) -> {
            buttons.get(y).get(x).setText(c == null ? "" : c.toString());
        });
    }

    public void setChangeHandler(TriConsumer<Integer, Integer, @Nullable C> changeHandler) {
        this.changeHandler = changeHandler;
    }

    public JButton getButton(int x, int y) {
        return buttons.get(y).get(x);
    }
}
