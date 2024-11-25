package com.github.shap_po.pencilgames.client.ui;

import com.github.shap_po.pencilgames.client.PencilGamesClient;
import com.github.shap_po.pencilgames.client.ui.screen.game.GameScreen;
import com.github.shap_po.pencilgames.client.ui.screen.menu.*;
import com.github.shap_po.pencilgames.client.ui.util.ContentPanel;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class GameWindow extends JFrame {
    private final List<ScreenState> stateHistory = new Stack<>();
    private final Map<ScreenState, ContentPanel> menus = new HashMap<>();
    private final JPanel contentPanel = new JPanel();

    public GameWindow() {
        super();
        setTitle("Pencil Games");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // populate menus
        menus.put(ScreenState.MAIN_MENU, new MainMenu(this));
        menus.put(ScreenState.JOIN_MENU, new JoinMenu(this));
        menus.put(ScreenState.HOST_MENU, new HostMenu(this));
        menus.put(ScreenState.SETTINGS_MENU, new SettingsMenu(this));
        menus.put(ScreenState.START_GAME_MENU, new StartGameScreen(this));

        setContentPane(contentPanel);

        updateContent();
    }

    /**
     * Get the current state of the window from the history stack.
     * If the stack is empty, the main menu will be returned.
     *
     * @return current state
     */
    public ScreenState getMenuState() {
        if (stateHistory.isEmpty()) {
            stateHistory.add(ScreenState.MAIN_MENU);
        }
        return stateHistory.getLast();
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
     * @param pushHistory  whether to add the new state to the history stack
     * @return true if the state was successfully set
     */
    public boolean setContentState(ScreenState state, ContentPanel contentPanel, boolean pushHistory) {
        if (pushHistory) {
            this.stateHistory.add(state);
        }

        setContent(contentPanel);

        return true;
    }

    /**
     * Sets the state of the window.
     * Content of the window will be set to the corresponding menu panel from {@link #menus} if present.
     *
     * @param state       new state
     * @param pushHistory whether to add the new state to the history stack
     * @return true if the state was successfully set
     */
    public boolean setContentState(ScreenState state, boolean pushHistory) {
        if (!menus.containsKey(state)) {
            PencilGamesClient.LOGGER.warn("Unknown menu state: {}", state);
            return false;
        }

        return setContentState(state, menus.get(state), pushHistory);
    }

    /**
     * Sets the state of the window.
     * Content of the window will be set to the corresponding menu panel from {@link #menus} if present.
     * The state will be pushed to the history stack.
     *
     * @param state new state
     * @return true if the state was successfully set
     */
    public boolean setContentState(ScreenState state) {
        return setContentState(state, true);
    }

    /**
     * Updates the content of the window to match the current state.
     *
     * @return true if the content was successfully updated
     */
    private boolean updateContent() {
        return setContentState(getMenuState(), false);
    }

    public void setGameScreen(GameScreen<?> gameScreen) {
        setContentState(ScreenState.GAME_SCREEN, gameScreen, false);
    }

    /**
     * Go back in the state history and update the window content.
     *
     * @param confirmText text to show in the confirmation dialog.
     *                    if null, no confirmation dialog will be shown
     */
    public void back(@Nullable String confirmText) {
        if (stateHistory.isEmpty()) {
            return;
        }

        if (confirmText != null) {
            int confirmed = JOptionPane.showConfirmDialog(this, confirmText, "Confirm", JOptionPane.YES_NO_OPTION);
            if (confirmed != JOptionPane.YES_OPTION) {
                return;
            }
        }

        boolean result = false;

        // some states (like the game screen) can't be re-created, skip them
        while (!result) {
            stateHistory.removeLast();
            result = updateContent();
        }
    }

    /**
     * Go back in the state history and update the window content.
     *
     * @param confirm whether to show a confirmation dialog with the default text
     */
    public void back(boolean confirm) {
        back(confirm ? "Are you sure you want to go back?" : null);
    }

    /**
     * Go back in the state history and update the window content.
     */
    public void back() {
        back(false);
    }

    public enum ScreenState {
        MAIN_MENU,
        JOIN_MENU,
        HOST_MENU,
        SETTINGS_MENU,
        START_GAME_MENU,
        GAME_SCREEN;
    }
}
