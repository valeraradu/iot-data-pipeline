package com.iot.app.spark.model;

import java.io.Serializable;

public class TemperatureEvent implements Serializable {

    private String longitude;
    private String latitude;
    private Double temp;

    public TemperatureEvent() {

    }

    public TemperatureEvent(String latitude, String longitude, Double temp) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
        this.temp = temp;
    }


    public String getLongitude() {
        return longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public Double getTemp() {
        return temp;
    }


}
