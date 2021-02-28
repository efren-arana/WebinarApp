package com.company.webinarapp.DAO;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationWebinar implements Parcelable {
    @SerializedName("latitud")
    @Expose
    private String latitud;
    @SerializedName("longitud")
    @Expose
    private String longitud;

    public LocationWebinar() {
    }

    protected LocationWebinar(Parcel in) {
        latitud = in.readString();
        longitud = in.readString();
    }

    public static final Parcelable.Creator<LocationWebinar> CREATOR = new Parcelable.Creator<LocationWebinar>() {
        @Override
        public LocationWebinar createFromParcel(Parcel in) {
            return new LocationWebinar(in);
        }

        @Override
        public LocationWebinar[] newArray(int size) {
            return new LocationWebinar[size];
        }
    };

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(latitud);
        dest.writeString(longitud);
    }
}
