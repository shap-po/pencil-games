package com.github.shap_po.pencilgames.server;

import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import org.slf4j.Logger;

import java.io.IOException;

/**
 * The main entry point for the PencilGames server part.
 * All server-related logic use LOGGER from this class.
 */
public class PencilGamesServer {
    public static final Logger LOGGER = LoggerUtils.getLogger();
    public static ServerGameLobby serverGameLobby;

    public static void main(String[] args) throws IOException {
        serverGameLobby = new ServerGameLobby();
        serverGameLobby.start();
    }
}
