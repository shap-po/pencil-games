package com.github.shap_po.pencilgames.client.ui;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.menu.HostMenu;
import com.github.shap_po.pencilgames.client.ui.menu.JoinMenu;
import com.github.shap_po.pencilgames.client.ui.menu.MainMenu;
import com.github.shap_po.pencilgames.client.ui.menu.SettingsMenu;
import com.github.shap_po.pencilgames.client.ui.util.MenuPanel;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class GameWindow extends JFrame {
    private final List<State> stateHistory = new Stack<>();
    private final Map<State, MenuPanel> menus = new HashMap<>();

    public GameWindow() {
        super();
        setTitle("Pencil Games");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // populate menus
        menus.put(State.MAIN_MENU, new MainMenu(this));
        menus.put(State.JOIN_MENU, new JoinMenu(this));
        menus.put(State.HOST_MENU, new HostMenu(this));
        menus.put(State.SETTINGS_MENU, new SettingsMenu(this));

        updateMenuState();
    }

    public State getMenuState() {
        if (stateHistory.isEmpty()) {
            stateHistory.add(State.MAIN_MENU);
        }
        return stateHistory.getLast();
    }

    private void setContent(MenuPanel menuPanel) {
        setContentPane(menuPanel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void setMenuState(State state) {
        if (!menus.containsKey(state)) {
            PencilGamesClient.LOGGER.warn("Unknown menu state: {}", state);
            return;
        }

        this.stateHistory.add(state);
        setContent(menus.get(state));
    }

    private void updateMenuState() {
        setContent(menus.get(getMenuState()));
    }

    public void back(boolean confirm, String confirmText) {
        if (stateHistory.isEmpty()) {
            return;
        }

        if (confirm) {
            int confirmed = JOptionPane.showConfirmDialog(this, confirmText, "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirmed != JOptionPane.YES_OPTION) {
                return;
            }
        }

        stateHistory.removeLast();
        updateMenuState();
    }

    public void back(String confirmText) {
        back(true, confirmText);
    }

    public void back(boolean confirm) {
        back(confirm, "Are you sure you want to go back?");
    }

    public void back() {
        back(false, null);
    }

    public enum State {
        MAIN_MENU,
        JOIN_MENU,
        HOST_MENU,
        SETTINGS_MENU;
    }
}
