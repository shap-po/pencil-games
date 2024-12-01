package com.github.shap_po.pencilgames.common.network;


import com.github.shap_po.pencilgames.common.event.type.ConsumerEvent;
import com.github.shap_po.pencilgames.common.event.type.RunnableEvent;
import com.github.shap_po.pencilgames.common.network.packet.Packet;
import com.github.shap_po.pencilgames.common.util.LoggerUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Base class for both client and server connection handlers.
 */
public class ConnectionHandler extends Thread {
    public static final int DEFAULT_PORT = 7920;

    public final RunnableEvent onConnect = RunnableEvent.create();
    public final RunnableEvent onDisconnect = RunnableEvent.create();
    public final ConsumerEvent<Packet> onPacket = ConsumerEvent.create();

    protected final Socket socket;

    private final Logger LOGGER = LoggerUtils.getParentLogger(); // not static as different subclasses might have different loggers
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final NetworkSide side;

    private boolean running;

    /**
     * Constructor.
     *
     * @param socket socket to use
     * @param side   network side. Used to prevent deadlocks.
     * @throws IOException if connection fails
     */
    public ConnectionHandler(Socket socket, NetworkSide side) throws IOException {
        super("ConnectionHandler/" + side + "/" + socket.getInetAddress());
        this.socket = socket;
        this.side = side;

        // Make sure to initialize input/output streams in the correct order to avoid deadlocks
        if (side == NetworkSide.SERVER) {
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
            inputStream = new ObjectInputStream(socket.getInputStream());
        } else {
            inputStream = new ObjectInputStream(socket.getInputStream());
            outputStream = new ObjectOutputStream(socket.getOutputStream());
            outputStream.flush();
        }
        running = true;
    }

    @Override
    public void run() {
        onConnect.run();

        while (running) {
            Packet packet = receivePacket();
            if (packet == null) break;

            onPacket.accept(packet);
        }

        close();
    }

    /**
     * Sends a packet to the other end of the connection.
     *
     * @param packet the packet
     */
    public void sendPacket(Packet packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to send packet {}", e.getMessage());
        }
    }

    /**
     * Receives a packet from the other end of the connection.
     *
     * @return the received packet or null if failed
     */
    private @Nullable Packet receivePacket() {
        Object message;

        try {
            message = inputStream.readObject();
        } catch (IOException e) {
            // Connection closed
            running = false;
            return null;
        } catch (ClassNotFoundException e) {
            LOGGER.warn("Received malformed packet from {}", socket.getInetAddress());
            return null;
        }

        if (!(message instanceof Packet packet)) {
            LOGGER.warn("Received packet of unknown type {} from {}", message.getClass(), socket.getInetAddress());
            return null;
        }

        if (packet.getType().side() != side) {
            LOGGER.error("Received a \"{}\" packet of wrong side \"{}\" from {}", packet.getType(), packet.getType().side(), socket.getInetAddress());
            return null;
        }

        return packet;
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            LOGGER.error("Failed to close socket", e);
        } finally {
            running = false;
            onDisconnect.run();
            this.interrupt();
        }
    }
}
