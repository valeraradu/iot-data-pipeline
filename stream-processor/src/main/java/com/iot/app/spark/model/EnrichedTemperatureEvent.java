package com.iot.app.spark.model;

public class EnrichedTemperatureEvent {
    private String longitude;
    private String latitude;
    private Double temp;
    private Double avgTemp;

    public EnrichedTemperatureEvent() {

    }

    public EnrichedTemperatureEvent(
            String latitude,
            String longitude,
            Double temp,
            Double avgTemp) {
        super();
        this.longitude = longitude;
        this.latitude = latitude;
        this.temp = temp;
        this.avgTemp = avgTemp;
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

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getAvgTemp() {
        return avgTemp;
    }

    public void setAvgTemp(Double avgTemp) {
        this.avgTemp = avgTemp;
    }
}
