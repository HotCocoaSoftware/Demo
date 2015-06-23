package com.hcs.shivansh.ixigommxdemoapp.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shivansh on 23/06/15.
 */
public class FlightCityResponse {
    @SerializedName("r")
    private List<FlightCity> flightCityList;

    public List<FlightCity> getFlightCityList() {
        return flightCityList;
    }

    public List<String> getflightCityNames() {
        List<String> nameList = new ArrayList<>();
        for (FlightCity flightCity : flightCityList) {
            nameList.add(flightCity.getFlightCityName());
        }
        return nameList;
    }
}
