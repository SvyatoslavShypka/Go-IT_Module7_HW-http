package com.goit.server.exception;

import lombok.Getter;

public class ServerNotFoundException extends ServerException {
    @Getter
    private final int httpCode = 400;

    public ServerNotFoundException(String message) {
        super(message);
    }

    public ServerNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
