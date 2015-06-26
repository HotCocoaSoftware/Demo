package com.hcs.shivansh.ixigommxdemoapp.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.HashMap;

/**
 * Created by shivansh on 26/06/15.
 */
public class FlightDetailsResponse {

    @SerializedName("results")
    HashMap<Integer, OutR> results;

    public class OutR {
        @SerializedName("outR")
        FaresListWrapper faresListWrapper;

        public FaresListWrapper getFaresListWrapper() {
            return faresListWrapper;
        }
    }

    public HashMap<Integer, OutR> getResults() {
        return results;
    }
}
