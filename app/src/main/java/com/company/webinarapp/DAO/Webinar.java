package com.company.webinarapp.DAO;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Webinar implements Serializable {

    @SerializedName("createAt")
    @Expose
    private String createAt;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("duration")
    @Expose
    private Integer duration;
    @SerializedName("finishDate")
    @Expose
    private String finishDate;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("imageUrl")
    @Expose
    private String imageUrl;
    @SerializedName("location")
    @Expose
    private LocationWebinar locationWebinar;
    @SerializedName("nameExpositor")
    @Expose
    private String nameExpositor;
    @SerializedName("observacion")
    @Expose
    private String observacion;
    @SerializedName("startDate")
    @Expose
    private String startDate;
    @SerializedName("statusPost")
    @Expose
    private String StatusPostObject;
    @SerializedName("statusWebinar")
    @Expose
    private String statusWebinar;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("urlWebinar")
    @Expose
    private String urlWebinar;

    public LocationWebinar getLocationWebinar() {
        return locationWebinar;
    }

    public void setLocationWebinar(LocationWebinar locationWebinar) {
        this.locationWebinar = locationWebinar;
    }


    public String getStatusPostObject() {
        return StatusPostObject;
    }

    public void setStatusPostObject(String statusPostObject) {
        StatusPostObject = statusPostObject;
    }




    // Getter Methods

    public String getCreateAt() {
        return createAt;
    }

    public String getDescription() {
        return description;
    }

    public int getDuration() {
        return duration;
    }

    public String getFinishDate() {
        return finishDate;
    }

    public int getId() {
        return id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getNameExpositor() {
        return nameExpositor;
    }

    public String getObservacion() {
        return observacion;
    }

    public String getStartDate() {
        return startDate;
    }


    public String getStatusWebinar() {
        return statusWebinar;
    }

    public String getTitle() {
        return title;
    }

    public String getUrlWebinar() {
        return urlWebinar;
    }

    // Setter Methods

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setFinishDate(String finishDate) {
        this.finishDate = finishDate;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setNameExpositor(String nameExpositor) {
        this.nameExpositor = nameExpositor;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setStatusWebinar(String statusWebinar) {
        this.statusWebinar = statusWebinar;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrlWebinar(String urlWebinar) {
        this.urlWebinar = urlWebinar;
    }
}
