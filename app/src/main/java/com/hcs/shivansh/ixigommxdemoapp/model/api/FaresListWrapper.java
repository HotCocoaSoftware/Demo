package com.hcs.shivansh.ixigommxdemoapp.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.List;

/**
 * Created by shivansh on 26/06/15.
 */
public class FaresListWrapper {
    @SerializedName("fares")
    HashMap<String,List<FlightFare>> fares;

    public HashMap<String, List<FlightFare>> getFares() {
        return fares;
    }
}
