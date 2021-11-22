package org.valdi.securepasswords.core.auth;

import com.google.gson.annotations.Expose;

public class Unauthenticated {
    @Expose
    private String error;
    @Expose
    private String message;

    public Unauthenticated() {

    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Unauthenticated{" +
                "error='" + error + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
