package com.device.simulator.model;

import java.io.Serializable;

public class TemperatureEvent implements Serializable {

    private String longitude;
    private String latitude;
    private Integer temp;

    public TemperatureEvent() {

    }

    public TemperatureEvent(String latitude, String longitude, Integer temp) {
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

    public Integer getTemp() {
        return temp;
    }


}
