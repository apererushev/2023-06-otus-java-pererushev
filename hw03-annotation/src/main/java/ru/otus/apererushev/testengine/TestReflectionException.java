package ru.otus.apererushev.testengine;

import org.jetbrains.annotations.NotNull;

public class TestReflectionException extends RuntimeException {
    public TestReflectionException(@NotNull final Throwable throwable) {
        super(throwable);
    }
}
