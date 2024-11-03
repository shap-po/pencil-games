package com.github.shap_po.pencilgames.server;

import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * The main entry point for the PencilGames server part.
 * All server-related logic use LOGGER from this class.
 */
public class PencilGamesServer {
    public static Logger LOGGER = LoggerFactory.getLogger(PencilGamesServer.class);

    public static void main(String[] args) throws IOException {
        ServerGameLobby serverGameLobby = new ServerGameLobby();
        serverGameLobby.start();
    }
}
