package org.valdi.securepasswords.core.auth;

import com.google.gson.annotations.Expose;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class Authenticated {
    @Expose
    private int id;
    @Expose
    private String token;
    @Expose
    private long date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public String getFormattedDate() {
        Date dateObj = new Date(date);
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        format.setTimeZone(TimeZone.getTimeZone("Europe/Rome"));
        return format.format(dateObj);
    }

    @Override
    public String toString() {
        return "Authenticated{" +
                "id=" + id +
                ", token='" + token + '\'' +
                ", date=" + date +
                '}';
    }
}
