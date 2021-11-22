package org.valdi.securepasswords.core.auth;

import com.google.gson.annotations.Expose;

public class Authorization {
    @Expose
    private String username;
    @Expose
    private String password;

    public Authorization() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Authorization{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
