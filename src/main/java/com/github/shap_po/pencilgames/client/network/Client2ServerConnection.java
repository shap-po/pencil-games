package com.github.shap_po.pencilgames.client.network;

import com.github.shap_po.pencilgames.common.network.ConnectionHandler;
import com.github.shap_po.pencilgames.common.network.NetworkSide;
import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.Socket;

public class Client2ServerConnection extends ConnectionHandler {
    public static Logger LOGGER = LoggerUtils.getLogger();

    public Client2ServerConnection(String host, int port) throws IOException {
        super(new Socket(host, port), NetworkSide.CLIENT);
        this.onPacket.register((packet) -> ClientPackets.REGISTRY.receive(packet, new ClientPackets.ClientPacketContext()));
        LOGGER.info("Connected to server at {}:{}", host, port);
    }

    public Client2ServerConnection(String host) throws IOException {
        this(host, DEFAULT_PORT);
    }
}
