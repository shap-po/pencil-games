package com.github.shap_po.pencilgames.common.network.packet;

import java.io.Serial;
import java.io.Serializable;

public abstract class Packet<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private final T payload;

    public Packet(T payload) {
        this.payload = payload;
    }

    public Packet() {
        this(null);
    }

    public abstract String getId();

    public T getPayload() {
        return payload;
    }
}
