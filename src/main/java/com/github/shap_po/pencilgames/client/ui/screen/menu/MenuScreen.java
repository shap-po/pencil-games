package com.github.shap_po.pencilgames.client.ui.screen.menu;

import com.github.shap_po.pencilgames.client.ui.Application;
import com.github.shap_po.pencilgames.client.ui.util.ContentPanel;

public abstract class MenuScreen extends ContentPanel {
    public MenuScreen(Application root, boolean addBackButton) {
        super(root);

        if (addBackButton) {
            addMainMenuButton();
        }
    }

    public MenuScreen(Application root) {
        this(root, true);
    }
}
