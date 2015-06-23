package com.hcs.shivansh.ixigommxdemoapp;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.hcs.shivansh.ixigommxdemoapp.model.Card;
import com.hcs.shivansh.ixigommxdemoapp.model.FlightCard;

import java.util.Calendar;
import java.util.List;

/**
 * Created by shivansh on 22/06/15.
 */
public class CardListAdapter extends ArrayAdapter<Card> {
    private static final String TAG = CardListAdapter.class.getCanonicalName();
    private Calendar selectedTime = Calendar.getInstance();
    private Calendar maxDate = Calendar.getInstance();
    private View.OnClickListener mSelectDateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(final View v) {
            final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(new DatePickerDialog.OnDateSetListener() {
                                                                                       @Override
                                                                                       public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                                                                                           Calendar selectedDate = Calendar.getInstance();
                                                                                           selectedDate.set(year, month, day);
                                                                                           ((TextView) v).setText(IxigoDemoApp.getInsatnce().getSimpleDateFormat().format(selectedDate.getTime()));
                                                                                       }
                                                                                   }, selectedTime.get(Calendar.YEAR), selectedTime.get(Calendar.MONTH),
                    selectedTime.get(Calendar.DAY_OF_MONTH), false);

            datePickerDialog.setMinMaxDate(selectedTime.getTime(), maxDate.getTime());
            datePickerDialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), TAG);
        }
    };

    public CardListAdapter(Context context, int resource, List<Card> objects) {
        super(context, resource, objects);
        maxDate.add(Calendar.MONTH, 3);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (getItem(position) instanceof FlightCard) {
            FlightCard flightCard = (FlightCard) getItem(position);
            convertView = layoutInflater.inflate(R.layout.flight_card, null);
            TextView fromFlight = (TextView) convertView.findViewById(R.id.from);
            TextView toFlight = (TextView) convertView.findViewById(R.id.to);
            fromFlight.setText(flightCard.getFromCityportKey());
            toFlight.setText(flightCard.getToCityportKey());
            fromFlight.setOnClickListener(getViewClickListener(position, LocationSearch.SearchType.FLIGHT));
            toFlight.setOnClickListener(getViewClickListener(position, LocationSearch.SearchType.FLIGHT));
        } else {
            convertView = layoutInflater.inflate(R.layout.hotel_booking_card, null);
        }
        convertView.findViewById(R.id.from_date).setOnClickListener(mSelectDateClickListener);
        convertView.findViewById(R.id.to_date).setOnClickListener(mSelectDateClickListener);
        return convertView;
    }

    private View.OnClickListener getViewClickListener(final int position, final LocationSearch.SearchType searchType) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationSearch.LocationSearchCallBacks locationSearchCallBacks = (LocationSearch.LocationSearchCallBacks) getContext();
                locationSearchCallBacks.setSelectedPosition(position);
                if (searchType.equals(LocationSearch.SearchType.FLIGHT)) {
                    FlightCard flightCard = (FlightCard) getItem(position);
                    if (v.getId() == R.id.from) {
                        locationSearchCallBacks.setForOrToFlightLocationSelected(1);
                    } else {
                        locationSearchCallBacks.setForOrToFlightLocationSelected(2);
                    }
                    showLocationSearchFragment(LocationSearch.SearchType.FLIGHT);
                }
            }
        };
    }

    private void showLocationSearchFragment(LocationSearch.SearchType searchType) {
        FragmentTransaction fragmentTransaction = ((MainActivity) getContext()).getSupportFragmentManager().beginTransaction();
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        fragmentTransaction.replace(R.id.fragment_container, LocationSearch.newInstance(searchType)).addToBackStack(TAG).commit();
    }

}
