package com.hcs.shivansh.ixigommxdemoapp.model;

/**
 * Created by shivansh on 26/06/15.
 */
public class FlightFareDetailsUi {
    private String flightName;
    private int flightTotalFare;

    public FlightFareDetailsUi(String flightName, int flightTotalFare) {
        this.flightName = flightName;
        this.flightTotalFare = flightTotalFare;
    }

    public String getFlightName() {
        return flightName;
    }

    public void setFlightName(String flightName) {
        this.flightName = flightName;
    }

    public int getFlightTotalFare() {
        return flightTotalFare;
    }

    public void setFlightTotalFare(int flightTotalFare) {
        this.flightTotalFare = flightTotalFare;
    }

    @Override
    public boolean equals(Object o) {
        return this.flightName.equals(((FlightFareDetailsUi)o).getFlightName());
    }

    @Override
    public int hashCode() {
        return flightTotalFare;
    }
}
