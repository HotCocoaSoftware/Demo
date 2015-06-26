package com.hcs.shivansh.ixigommxdemoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hcs.shivansh.ixigommxdemoapp.model.FlightFareDetailsUi;

import java.util.List;

/**
 * Created by shivansh on 26/06/15.
 */
public class FlightResultsListAdapter extends ArrayAdapter<FlightFareDetailsUi> {

    private final int resourceId;

    public FlightResultsListAdapter(Context context, int resourceId, List<FlightFareDetailsUi> flightFareDetailsUiList) {
        super(context, resourceId, flightFareDetailsUiList);
        this.resourceId = resourceId;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(resourceId, null);
        ((TextView) convertView.findViewById(R.id.flight_key)).setText(getItem(position).getFlightName());
        ((TextView) convertView.findViewById(R.id.flight_price)).setText(String.valueOf(getItem(position).getFlightTotalFare()));
        return convertView;
    }

    @Override
    public int getCount() {
        if (super.getCount() < 5) {
            return super.getCount();
        } else {
            return 5;
        }
    }
}
