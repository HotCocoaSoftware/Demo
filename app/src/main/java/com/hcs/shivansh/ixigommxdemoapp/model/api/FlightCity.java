package com.hcs.shivansh.ixigommxdemoapp.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivansh on 23/06/15.
 */
public class FlightCity {
    @SerializedName("n")
    private String flightCityName;
    @SerializedName("c")
    private String flightCityKey;

    public String getFlightCityName() {
        return flightCityName;
    }

    public String getFlightCityKey() {
        return flightCityKey;
    }
}
