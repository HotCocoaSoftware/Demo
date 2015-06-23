package com.hcs.shivansh.ixigommxdemoapp.model;

import java.util.Calendar;

/**
 * Created by shivansh on 23/06/15.
 */
public class FlightCard implements Card {
    private Calendar checkInDate;
    private Calendar checkOutDate;
    private String fromCityportKey = "From";
    private String toCityportKey = "To";

    public Calendar getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Calendar checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Calendar getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Calendar checkOutDate) {
        this.checkOutDate = checkOutDate;
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
