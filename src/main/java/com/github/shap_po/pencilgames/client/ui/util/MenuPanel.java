package com.github.shap_po.pencilgames.client.ui.util;

import com.github.shap_po.pencilgames.client.ui.GameWindow;

import javax.swing.*;
import java.awt.event.ActionListener;

public class MenuPanel extends JPanel {
    protected final GameWindow root;

    public MenuPanel(GameWindow root) {
        super(false);
        this.root = root;

        // vertical layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // center contents
        setAlignmentX(CENTER_ALIGNMENT);
        setAlignmentY(CENTER_ALIGNMENT);
    }

    protected void addButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        add(button);
    }

    protected void addBackButton() {
        addButton("Back", e -> root.back());
    }
}
