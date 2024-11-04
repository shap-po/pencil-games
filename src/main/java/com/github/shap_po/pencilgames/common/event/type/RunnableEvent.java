package com.github.shap_po.pencilgames.common.event.type;

import com.github.shap_po.pencilgames.common.event.Event;

public class RunnableEvent extends Event<Runnable> implements Runnable {
    @Override
    public void run() {
        getHandlers().forEach(Runnable::run);
    }

    public static RunnableEvent create() {
        return new RunnableEvent();
    }
}