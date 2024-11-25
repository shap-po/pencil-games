package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.client.ui.util.ContentPanel;

public abstract class MenuScreen extends ContentPanel {
    public MenuScreen(GameWindow root, boolean addBackButton) {
        super(root);

        if (addBackButton) {
            addBackButton();
        }
    }

    public MenuScreen(GameWindow root) {
        this(root, true);
    }
}
