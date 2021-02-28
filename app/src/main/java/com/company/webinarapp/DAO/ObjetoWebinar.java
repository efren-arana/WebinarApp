package com.company.webinarapp.DAO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ObjetoWebinar {
    @SerializedName("webinar")
    @Expose
    private Webinar webinars;
    private String error;

    public Webinar getWebinars() {
        return webinars;
    }

    public void setWebinars(Webinar webinars) {
        this.webinars = webinars;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
