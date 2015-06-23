package com.hcs.shivansh.ixigommxdemoapp.model;

import java.util.Calendar;

/**
 * Created by shivansh on 23/06/15.
 */
public class HotelCard implements Card {

    private Calendar checkInDate;
    private Calendar checkOutDate;

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
}
