package com.hcs.shivansh.ixigommxdemoapp.model;

import java.util.List;

/**
 * Created by shivansh on 23/06/15.
 */
public class FlightCard implements Card {
    private String formDate;
    private String fromCityportKey = "From";
    private String toCityportKey = "To";
    private List<FlightResults> flightResultsList;

    public String getFormDate() {
        return formDate;
    }

    public void setFormDate(String formDate) {
        this.formDate = formDate;
    }

    public List<FlightResults> getFlightResultsList() {
        return flightResultsList;
    }

    public void setFlightResultsList(List<FlightResults> flightResultsList) {
        this.flightResultsList = flightResultsList;
    }

    public String getFromCityportKey() {
        return fromCityportKey;
    }

    public void setFromCityportKey(String fromCityportKey) {
        this.fromCityportKey = fromCityportKey;
    }

    public String getToCityportKey() {
        return toCityportKey;
    }

    public void setToCityportKey(String toCityportKey) {
        this.toCityportKey = toCityportKey;
    }
}
