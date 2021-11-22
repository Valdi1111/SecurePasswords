package org.valdi.securepasswords.core.auth;

public enum Result {
    INVALID_TOKEN("invalid_token", "Token not found."),
    UNAUTHORIZED("unauthorized", "Not permitted, insufficient scope."),
    INVALID_USERNAME("invalid_username", "Username not registered."),
    WRONG_PASSWORD("wrong_password", "Wrong password for the given username."),
    SUCCESS(null, null);

    private final String error;
    private final String message;

    Result(String error, String message) {
        this.error = error;
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
