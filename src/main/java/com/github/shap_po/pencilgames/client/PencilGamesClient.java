package com.github.shap_po.pencilgames.client;

import com.github.shap_po.pencilgames.client.network.ClientGameLobby;
import com.github.shap_po.pencilgames.client.ui.GameWindow;
import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import com.github.shap_po.pencilgames.server.PencilGamesServer;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import org.apache.commons.cli.*;
import org.slf4j.Logger;

import java.io.IOException;

import static java.lang.System.exit;

/**
 * The main entry point for the PencilGames client part.
 */
public class PencilGamesClient {
    public static final Logger LOGGER = LoggerUtils.getLogger();
    public static final GameWindow gameWindow = new GameWindow();
    public static final ClientGameLobby clientLobby = new ClientGameLobby();

    public static final Options CLI_OPTIONS = new Options()
        .addOption(Option.builder("h")
            .argName("host")
            .desc("host the game")
            .build())
        .addOption(Option.builder("c")
            .argName("connect")
            .desc("connect to a server")
            .build())
        .addOption(Option.builder("f")
            .argName("force")
            .desc("try to connect to the server until it is available")
            .hasArg()
            .type(Integer.class)
            .build())
        .addOption(Option.builder("t")
            .argName("title")
            .desc("Window title")
            .hasArg()
            .type(String.class)
            .build())
        .addOptions(PencilGamesServer.CLI_OPTIONS);

    public static void main(String[] args) {
        clientLobby.start();
        clientLobby.onConnect.register(() -> {
            if (!isHost()) {
                gameWindow.setContentState(GameWindow.ScreenState.WAITING_MENU);
            }
        });

        parseArgs(args);

        gameWindow.setVisible(true);
        LOGGER.info("Started client");
    }

    /**
     * Parse command line arguments and process them.
     *
     * @param args command line arguments
     */
    private static void parseArgs(String[] args) {
        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(CLI_OPTIONS, args);

            String title = line.getParsedOptionValue("t");
            if (title != null) {
                gameWindow.setTitle(title);
            }

            Integer port = line.getParsedOptionValue("p");

            boolean shouldConnect = line.hasOption("c");

            if (line.hasOption("h")) {
                if (!line.hasOption("g")) {
                    LOGGER.warn("Host mode requires a game to be specified. Use the -g option.");
                } else {
                    GameFactory<ServerGameLobby, Game<ServerGameLobby>> gameFactory = line.getParsedOptionValue("g");
                    Integer pts = line.getParsedOptionValue("pts");

                    try {
                        PencilGamesServer.start(port, gameFactory, pts);
                    } catch (IOException e) {
                        LOGGER.error("Failed to start server with command line arguments: {}", e.getMessage());
                    }

                    gameWindow.setContentState(GameWindow.ScreenState.START_GAME_MENU);
                    shouldConnect = true; // auto-connect to the server
                }
            }

            boolean force = line.hasOption("f");

            if (shouldConnect) {
                if (force) {
                    // knock on the door until it is open
                    while (!clientLobby.isConnected()) {
                        try {
                            clientLobby.connect("localhost", port);
                        } catch (IOException e) {
                            LOGGER.error("Failed to connect to server. Retrying...");
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                } else {
                    try {
                        clientLobby.connect("localhost", port);
                    } catch (IOException e) {
                        LOGGER.error("Failed to connect to server with command line arguments: {}", e.getMessage());
                    }
                }
            }

        } catch (ParseException e) {
            LOGGER.error("Failed to parse command line arguments: {}", e.getMessage());
            exit(1);
        }
    }

    public static boolean isHost() {
        return PencilGamesServer.serverGameLobby != null;
    }
}
