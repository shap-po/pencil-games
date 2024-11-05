package com.github.shap_po.pencilgames.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class LoggerUtils {
    private static final StackWalker STACK_WALKER = StackWalker.getInstance(StackWalker.Option.RETAIN_CLASS_REFERENCE);

    public static Logger getParentLogger() {
        // find the first top level class with a LOGGER field
        return STACK_WALKER.walk(
            stackFrameStream -> stackFrameStream
                .map(StackWalker.StackFrame::getDeclaringClass)
                .map(clazz -> {
                    try {
                        return (Logger) clazz.getField("LOGGER").get(null);
                    } catch (Exception e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst()
                // fallback to calling class
                .orElse(getLogger())
        );
    }

    public static Logger getLogger() {
        return getLogger(STACK_WALKER.getCallerClass());
    }

    public static Logger getLogger(Class<?> clazz) {
        return LoggerFactory.getLogger(clazz.getSimpleName());
    }
}
