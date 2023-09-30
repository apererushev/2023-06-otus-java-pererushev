package ru.otus.apererushev.proxy;

import lombok.experimental.StandardException;

@StandardException
public class LogProxyException extends RuntimeException {
    public LogProxyException(String message, Throwable cause) {
        super(message, cause);
    }
}
