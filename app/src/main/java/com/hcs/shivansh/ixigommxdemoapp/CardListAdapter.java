package com.hcs.shivansh.ixigommxdemoapp;

import android.content.Context;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.hcs.shivansh.ixigommxdemoapp.model.Card;
import com.hcs.shivansh.ixigommxdemoapp.model.FlightCard;
import com.hcs.shivansh.ixigommxdemoapp.model.FlightFareDetailsUi;

import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by shivansh on 22/06/15.
 */
public class CardListAdapter extends ArrayAdapter<Card> implements FlightResultProvider.FlightResult {
    private static final String TAG = CardListAdapter.class.getCanonicalName();
    private Calendar selectedTime = Calendar.getInstance();
    private Calendar maxDate = Calendar.getInstance();

    public CardListAdapter(Context context, int resource, List<Card> objects) {
        super(context, resource, objects);
        maxDate.add(Calendar.MONTH, 3);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (getItem(position) instanceof FlightCard) {
            final FlightCard flightCard = (FlightCard) getItem(position);
            convertView = layoutInflater.inflate(R.layout.flight_card, null);
            final FlightCardViewHolder flightCardViewHolder = new FlightCardViewHolder(convertView);
            flightCardViewHolder.originTextView.setText(flightCard.getFromCityportKey());
            flightCardViewHolder.destinationTextView.setText(flightCard.getToCityportKey());
            flightCardViewHolder.originTextView.setOnClickListener(getViewClickListener(position, LocationSearch.SearchType.FLIGHT));
            flightCardViewHolder.destinationTextView.setOnClickListener(getViewClickListener(position, LocationSearch.SearchType.FLIGHT));
            flightCardViewHolder.dateTextView.setOnClickListener(getSelectDateListener(position));
            flightCardViewHolder.searchButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flightCard.getFormDate() != null) {
                        new FlightResultProvider(CardListAdapter.this, flightCard, flightCardViewHolder).startFetchingFlightResults();
                        flightCardViewHolder.flightInputData.setVisibility(View.GONE);
                        flightCardViewHolder.flightReesultData.setVisibility(View.VISIBLE);
                    } else {
                        Toast.makeText(getContext(), "PLease select date", Toast.LENGTH_LONG).show();
                    }
                }
            });
            flightCardViewHolder.editFLightDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    flightCardViewHolder.flightInputData.setVisibility(View.VISIBLE);
                    flightCardViewHolder.flightReesultData.setVisibility(View.GONE);
                    if (flightCardViewHolder.flightResultsListAdapter != null) {
                        flightCardViewHolder.flightResultsListAdapter.clear();
                    }
                    flightCardViewHolder.pollingProgressBar.setVisibility(View.VISIBLE);
                }
            });
        } else {
            convertView = layoutInflater.inflate(R.layout.hotel_booking_card, null);
        }
        return convertView;
    }

    private View.OnClickListener getSelectDateListener(final int position) {
        return new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final DatePickerDialog datePickerDialog = DatePickerDialog
                        .newInstance(new DatePickerDialog.OnDateSetListener() {
                                         @Override
                                         public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
                                             Calendar selectedDate = Calendar.getInstance();
                                             selectedDate.set(year, month, day);
                                             TextView textView = (TextView) v;
                                             textView.setText(IxigoDemoApp.getInsatnce().getSimpleDateFormat().format(selectedDate.getTime()));
                                             if (textView.getId() == R.id.from_date_flight_card) {
                                                 ((FlightCard) getItem(position)).setFormDate(textView.getText().toString());
                                             }
                                         }
                                     }, selectedTime.get(Calendar.YEAR), selectedTime.get(Calendar.MONTH),
                                selectedTime.get(Calendar.DAY_OF_MONTH), false);

                datePickerDialog.setMinMaxDate(selectedTime.getTime(), maxDate.getTime());
                datePickerDialog.show(((AppCompatActivity) getContext()).getSupportFragmentManager(), TAG);
            }
        };
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

    @Override
    public void onResult(List<FlightFareDetailsUi> flightFareDetailsUiList, FlightCardViewHolder flightCardViewHolder) {
        FlightResultsListAdapter flightResultsListAdapter = new FlightResultsListAdapter(getContext(), R.layout.flight_result_list_item, flightFareDetailsUiList);
        flightCardViewHolder.flightResultsListAdapter = flightResultsListAdapter;
        flightCardViewHolder.flightResultListView.setAdapter(flightResultsListAdapter);
    }


    public class FlightCardViewHolder {
        @InjectView(R.id.flight_input_data)
        View flightInputData;
        @InjectView(R.id.from)
        TextView originTextView;
        @InjectView(R.id.to)
        TextView destinationTextView;
        @InjectView(R.id.from_date_flight_card)
        TextView dateTextView;
        @InjectView(R.id.search)
        TextView searchButton;

        @InjectView(R.id.flight_result_data)
        View flightReesultData;
        @InjectView(R.id.edit_flight_details)
        View editFLightDetails;
        @InjectView(R.id.polling_progress)
        View pollingProgressBar;
        @InjectView(R.id.flight_result_listView)
        ListView flightResultListView;

        FlightResultsListAdapter flightResultsListAdapter;

        public FlightCardViewHolder(View convertView) {
            ButterKnife.inject(this, convertView);
        }

    }

}
