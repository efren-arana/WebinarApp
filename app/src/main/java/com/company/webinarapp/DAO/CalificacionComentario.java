package com.company.webinarapp.DAO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CalificacionComentario {
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("rate")
    @Expose
    private int rate;
    @SerializedName("comment")
    @Expose
    private String comment;

    //ID
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    // RATE
    public int getRate() { return rate; }

    public void setRate(int rate) {
        this.rate = rate;
    }

    //COMENTARIO

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

}
