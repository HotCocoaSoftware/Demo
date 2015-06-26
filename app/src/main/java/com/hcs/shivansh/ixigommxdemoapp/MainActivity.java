/*
 * Copyright 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hcs.shivansh.ixigommxdemoapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hcs.shivansh.ixigommxdemoapp.model.Card;
import com.hcs.shivansh.ixigommxdemoapp.model.FlightCard;
import com.hcs.shivansh.ixigommxdemoapp.model.HotelCard;
import com.hcs.shivansh.ixigommxdemoapp.model.api.FlightCityResponse;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationSearch.LocationSearchCallBacks {
    private static final String TAG = MainActivity.class.getCanonicalName();
    ArrayAdapter<Card> cardListAdapter;
    int selectedPositioninListForLocationSearch;
    int forOrToFlightLocationSelected; // 1 for from and 2 for to
    private List<Card> listOfCards;
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            volleyError.printStackTrace();
        }
    };
    private Response.Listener<FlightCityResponse> nearByLocationResponseListener = new Response.Listener<FlightCityResponse>() {
        @Override
        public void onResponse(FlightCityResponse flightCityResponse) {
            getFLightCard().setFromCityportKey(flightCityResponse.getFlightCityList().get(0).getFlightCityKey());
            cardListAdapter.notifyDataSetChanged();
        }
    };
    private BroadcastReceiver locationFetchedReciever;

    private FlightCard getFLightCard() {
        return (FlightCard) listOfCards.get(0);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         locationFetchedReciever = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                makeNearByLocationRequest();
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction("Location");
        registerReceiver(locationFetchedReciever, filter);
        listOfCards = getListOfCards();
        cardListAdapter = new CardListAdapter(this, R.layout.hotel_booking_card, listOfCards);
        ListView listView = getListView();
        listView.setAdapter(cardListAdapter);
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    cardListAdapter.remove(cardListAdapter.getItem(position));
                                }
                                cardListAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
    }

    private void makeNearByLocationRequest() {
        unregisterReceiver(locationFetchedReciever);
        Location location = IxigoDemoApp.getInsatnce().getmCurrentLocation();
        GsonRequest request = new GsonRequest<FlightCityResponse>(Request.Method.GET, "http://www.ixigo.com/api/flights/locations/nearby?apiKey=wguels!2$&lat=" + location.getLatitude() + "&long=" + location.getLongitude() + "&locale=IN", FlightCityResponse.class, null, nearByLocationResponseListener, errorListener, null);
        request.setTag(TAG);
        IxigoDemoApp.getInsatnce().getRequestQueue().cancelAll(TAG);
        IxigoDemoApp.getInsatnce().getRequestQueue().add(request);
    }

    private List<Card> getListOfCards() {
        List<Card> cards = new ArrayList<>();
        cards.add(new FlightCard());
        cards.add(new HotelCard());
        return cards;
    }

    private ListView getListView() {
        return (ListView) findViewById(R.id.list_view);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void setSelectedPosition(int position) {
        selectedPositioninListForLocationSearch = position;
    }

    @Override
    public void setForOrToFlightLocationSelected(int value) {
        forOrToFlightLocationSelected = value;
    }

    @Override
    public void setSelectedFlightLocation(String locationKey) {
        FlightCard flightCard = (FlightCard) listOfCards.get(selectedPositioninListForLocationSearch);
        if (forOrToFlightLocationSelected == 1) {
            flightCard.setFromCityportKey(locationKey);
        } else {
            flightCard.setToCityportKey(locationKey);
        }
        cardListAdapter.notifyDataSetChanged();
        IxigoDemoApp.getInsatnce().hideKeyboard(getCurrentFocus());
    }

}
