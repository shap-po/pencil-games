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

    public ConnectionHandler(Socket socket, NetworkSide side) {
        super("ConnectionHandler-" + side + "-" + socket.getInetAddress());
        this.socket = socket;
        this.side = side;
        try {
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
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public <P extends Packet> void sendPacket(P packet) {
        try {
            outputStream.writeObject(packet);
            outputStream.flush();
        } catch (IOException e) {
            LOGGER.error("Failed to send packet", e);
        }
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

    private @Nullable Packet receivePacket() {
        Object message;

        try {
            message = inputStream.readObject();
        } catch (IOException e) {
            // Connection closed
            running = false;
            return null;
        } catch (ClassNotFoundException e) {
            LOGGER.error("Received malformed packet from {}", socket.getInetAddress());
            return null;
        }

        if (!(message instanceof Packet packet)) {
            LOGGER.error("Received packet of unknown type {} from {}", message.getClass(), socket.getInetAddress());
            return null;
        }

        if (packet.getType().side() != side) {
            LOGGER.error("Received packet of wrong side {} from {}", packet.getType().side(), socket.getInetAddress());
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
