package com.github.shap_po.pencilgames.server;

import com.github.shap_po.pencilgames.common.game.Game;
import com.github.shap_po.pencilgames.common.game.GameFactory;
import com.github.shap_po.pencilgames.common.util.Identifier;
import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import com.github.shap_po.pencilgames.server.game.ServerGameFactoryRegistry;
import com.github.shap_po.pencilgames.server.network.ServerGameLobby;
import org.apache.commons.cli.*;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;

import static java.lang.System.exit;

/**
 * The main entry point for the PencilGames server part.
 * All server-related logic use LOGGER from this class.
 */
public class PencilGamesServer {
    public static final Logger LOGGER = LoggerUtils.getLogger();
    public static final Options CLI_OPTIONS = new Options()
        .addOption(Option.builder("p")
            .argName("port")
            .desc("port to use")
            .hasArg()
            .type(Integer.class)
            .build())
        .addOption(Option.builder("g")
            .argName("game")
            .desc(
                "id of the game to start.\n" +
                    "List of all available games: " + ServerGameFactoryRegistry.REGISTRY.getKeys()
            )
            .hasArg()
            .converter((s) -> {
                Identifier id = Identifier.fromString(s);
                GameFactory<ServerGameLobby, Game<ServerGameLobby>> factory = ServerGameFactoryRegistry.REGISTRY.get(id);
                if (factory == null) {
                    throw new IllegalArgumentException("Unknown game: " + id);
                }
                return factory;
            })
            .build())
        .addOption(Option.builder()
            .longOpt("pts")
            .desc("number of players to accept before automatically starting the game")
            .hasArg()
            .type(Integer.class)
            .build());

    public static @Nullable ServerGameLobby serverGameLobby;

    public static void main(String[] args) throws IOException {
        Integer port = null;
        GameFactory<ServerGameLobby, Game<ServerGameLobby>> gameFactory = null;
        Integer pts = null;

        try {
            CommandLineParser parser = new DefaultParser();
            CommandLine line = parser.parse(CLI_OPTIONS, args);

            port = line.getParsedOptionValue("p");
            gameFactory = line.getParsedOptionValue("g");
            pts = line.getParsedOptionValue("pts");

        } catch (ParseException e) {
            LOGGER.error("Failed to parse command line arguments: {}", e.getMessage());
            exit(1);
        }

        start(port, gameFactory, pts);
    }

    public static void start(Integer port, GameFactory<ServerGameLobby, Game<ServerGameLobby>> gameFactory, Integer pts) throws IOException {
        LOGGER.info("Starting server with options: port={}, game={}", port, gameFactory);

        serverGameLobby = new ServerGameLobby(port);

        serverGameLobby.start();

        // start the game when enough players connect
        serverGameLobby.onPlayerConnect.register((player) -> {
            if (pts != null && serverGameLobby.getPlayerManager().getPlayerCount() >= pts) {
                serverGameLobby.startGame(gameFactory);
            }
        });
    }
}
