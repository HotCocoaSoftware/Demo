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

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.hcs.shivansh.ixigommxdemoapp.model.Card;
import com.hcs.shivansh.ixigommxdemoapp.model.FlightCard;
import com.hcs.shivansh.ixigommxdemoapp.model.HotelCard;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LocationSearch.LocationSearchCallBacks {
    ArrayAdapter<Card> cardListAdapter;
    int selectedPositioninListForLocationSearch;
    int forOrToFlightLocationSelected; // 1 for from and 2 for to
    private List<Card> listOfCards;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfCards = getListOfCards();
        cardListAdapter = new CardListAdapter(this, R.layout.hotel_booking_card, listOfCards);
        getListView().setAdapter(cardListAdapter);
        ListView listView = getListView();
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
