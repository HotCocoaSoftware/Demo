package com.hcs.shivansh.ixigommxdemoapp.model.api;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shivansh on 26/06/15.
 */
public class FlightFare {
    @SerializedName("bf")
    String baseFare;
    @SerializedName("t")
    String tax;
    @SerializedName("f")
    String fare;
    @SerializedName("d")
    String discount;
    @SerializedName("tf")
    String totalFare;

    public String getBaseFare() {
        return baseFare;
    }

    public String getTax() {
        return tax;
    }

    public String getFare() {
        return fare;
    }

    public String getDiscount() {
        return discount;
    }

    public String getTotalFare() {
        return totalFare;
    }
}
