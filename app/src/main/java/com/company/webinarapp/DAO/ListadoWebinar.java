package com.company.webinarapp.DAO;

import java.util.ArrayList;

public class ListadoWebinar {

    public ArrayList<Webinar> getWebinars() {
        return webinars;
    }

    public void setWebinars(ArrayList<Webinar> webinars) {
        this.webinars = webinars;
    }

    private ArrayList<Webinar> webinars;

}
