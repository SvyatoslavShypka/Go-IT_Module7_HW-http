package com.goit.server.exception;

public class ServerInternalErrorException extends ServerException {
    private final int httpCode = 500;

    public ServerInternalErrorException(String message) {
        super(message);
    }

    public ServerInternalErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    @Override
    public int getHttpCode() {
        return this.httpCode;
    }
}
