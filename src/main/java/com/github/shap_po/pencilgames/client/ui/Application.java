package com.github.shap_po.pencilgames.client.ui;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.screen.game.GameScreen;
import com.github.shap_po.pencilgames.client.ui.screen.menu.*;
import com.github.shap_po.pencilgames.client.ui.util.ContentPanel;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

public class Application extends JFrame {
    private final Map<ScreenState, ContentPanel> menus = new HashMap<>();
    private final JPanel contentPanel = new JPanel();

    public Application() {
        super();
        setTitle("Pencil Games");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // populate menus
        menus.put(ScreenState.MAIN_MENU, new MainMenu(this));
        menus.put(ScreenState.JOIN_MENU, new JoinMenu(this));
        menus.put(ScreenState.HOST_MENU, new HostMenu(this));
        menus.put(ScreenState.SETTINGS_MENU, new SettingsMenu(this));
        menus.put(ScreenState.START_GAME_MENU, new StartGameMenu(this));
        menus.put(ScreenState.WAITING_MENU, new WaitingMenu(this));

        setContentPane(contentPanel);

        setMainMenu();
    }

    /**
     * Sets the main content of the window.
     * Most of the time it's better to use {@link #setContentState} instead.
     *
     * @param contentPanel panel to set
     */
    public void setContent(ContentPanel contentPanel) {
        this.contentPanel.removeAll();
        this.contentPanel.add(contentPanel);
        this.contentPanel.revalidate();
        this.contentPanel.repaint();
    }

    /**
     * Sets the state and content of the window.
     *
     * @param state        new state
     * @param contentPanel new content
     * @return true if the state was successfully set
     */
    public boolean setContentState(ScreenState state, ContentPanel contentPanel) {
        setContent(contentPanel);

        return true;
    }

    /**
     * Sets the state of the window.
     * Content of the window will be set to the corresponding menu panel from {@link #menus} if present.
     *
     * @param state new state
     * @return true if the state was successfully set
     */
    public boolean setContentState(ScreenState state) {
        if (!menus.containsKey(state)) {
            PencilGamesClient.LOGGER.warn("Unknown menu state: {}", state);
            return false;
        }

        return setContentState(state, menus.get(state));
    }

    public void setGameScreen(GameScreen<?> gameScreen) {
        setContentState(ScreenState.GAME_SCREEN, gameScreen);
    }

    public void setMainMenu() {
        setContentState(ScreenState.MAIN_MENU);
    }

    public enum ScreenState {
        MAIN_MENU,
        JOIN_MENU,
        HOST_MENU,
        SETTINGS_MENU,
        START_GAME_MENU,
        WAITING_MENU,
        GAME_SCREEN;
    }
}
