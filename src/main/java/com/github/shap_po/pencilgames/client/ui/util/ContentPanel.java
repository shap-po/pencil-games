package com.github.shap_po.pencilgames.client.ui.util;

import com.github.shap_po.pencilgames.client.ui.GameWindow;

import javax.swing.*;
import java.awt.event.ActionListener;

public abstract class ContentPanel extends JPanel {
    protected final GameWindow root;

    /**
     * Creates a new ContentPanel
     *
     * @param root       root window
     * @param doPopulate whether to populate the panel on creation.
     *                   Sometimes, some variables need to be set before populating
     */
    public ContentPanel(GameWindow root, boolean doPopulate) {
        super(false);
        this.root = root;

        // vertical layout
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        // center contents
        setAlignmentX(CENTER_ALIGNMENT);
        setAlignmentY(CENTER_ALIGNMENT);

        if (doPopulate) {
            populate();
        }
    }

    /**
     * Creates a new ContentPanel and populates it
     *
     * @param root root window
     */
    public ContentPanel(GameWindow root) {
        this(root, true);
    }

    protected void addButton(String text, ActionListener actionListener) {
        JButton button = new JButton(text);
        button.addActionListener(actionListener);
        add(button);
    }

    protected void addBackButton(boolean confirm) {
        addButton("Back", e -> root.back(confirm));
    }

    protected void addBackButton() {
        addBackButton(false);
    }

    abstract protected void populate();
}
