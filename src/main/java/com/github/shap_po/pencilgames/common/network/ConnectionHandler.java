package com.github.shap_po.pencilgames.common.network;


import com.github.shap_po.pencilgames.common.event.type.ConsumerEvent;
import com.github.shap_po.pencilgames.common.event.type.RunnableEvent;
import com.github.shap_po.pencilgames.common.network.packet.Packet;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ConnectionHandler extends Thread {
    public static final int DEFAULT_PORT = 7920;

    public final RunnableEvent onConnect = RunnableEvent.create();
    public final RunnableEvent onDisconnect = RunnableEvent.create();
    public final ConsumerEvent<Packet> onPacket = ConsumerEvent.create();

    protected final Socket socket;

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
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        onConnect.run();
        while (running) {
            try {
                Packet packet = (Packet) inputStream.readObject();
                onPacket.accept(packet);
            } catch (ClassNotFoundException e) {
                System.err.println("Received malformed packet from " + socket.getInetAddress());
            } catch (EOFException e) {
                // Connection closed
                running = false;
            } catch (IOException e) {
                e.printStackTrace();
                running = false;
            }
        }
        close();
    }

    public void close() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            running = false;
            onDisconnect.run();
            this.interrupt();
        }
    }
}
