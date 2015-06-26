package com.hcs.shivansh.ixigommxdemoapp;

import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hcs.shivansh.ixigommxdemoapp.model.FlightCard;
import com.hcs.shivansh.ixigommxdemoapp.model.FlightFareDetailsUi;
import com.hcs.shivansh.ixigommxdemoapp.model.api.FlightDetailsResponse;
import com.hcs.shivansh.ixigommxdemoapp.model.api.FlightFare;
import com.hcs.shivansh.ixigommxdemoapp.model.api.FlightSearchProvidersResponse;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by shivansh on 26/06/15.
 */
public class FlightResultProvider {
    private static final String TAG = FlightResultProvider.class.getCanonicalName();
    private final CardListAdapter.FlightCardViewHolder flightCardViewHolder;
    private FlightResult flightResult;
    private FlightCard flightCard;
    private HashSet<FlightFareDetailsUi> flghtFareUiListSet;

    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            volleyError.printStackTrace();
        }
    };
    private List<Integer> searchProviders;
    private String searchToken;
    private Response.Listener<FlightSearchProvidersResponse> flightSearchProviderResponseListener = new Response.Listener<FlightSearchProvidersResponse>() {
        @Override
        public void onResponse(FlightSearchProvidersResponse flightSearchProvidersResponse) {
            searchProviders = flightSearchProvidersResponse.getSearchProviders();
            searchToken = flightSearchProvidersResponse.getSearchToken();
            startPolling();

        }
    };
    private Comparator<? super FlightFareDetailsUi> mFareAscendingComparator = new Comparator<FlightFareDetailsUi>() {
        @Override
        public int compare(FlightFareDetailsUi lhs, FlightFareDetailsUi rhs) {
            return lhs.getFlightTotalFare() - rhs.getFlightTotalFare();
        }
    };
    private Response.Listener<FlightDetailsResponse> flightPollingResponseListener = new Response.Listener<FlightDetailsResponse>() {
        @Override
        public void onResponse(FlightDetailsResponse flightDetailsResponse) {
            HashMap<Integer, FlightDetailsResponse.OutR> results = flightDetailsResponse.getResults();
            if (results != null) {
                for (Integer providerKey : results.keySet()) {
                    searchProviders.remove(providerKey);
                    searchProviders.remove(providerKey);
                    startPolling();
                    updateFlightFareListUi(flightDetailsResponse.getResults().get(providerKey).getFaresListWrapper().getFares());
                }
                List<FlightFareDetailsUi> FlightFareDetailsUi = new ArrayList(flghtFareUiListSet);
                Collections.sort(FlightFareDetailsUi, mFareAscendingComparator);
                flightResult.onResult(FlightFareDetailsUi, flightCardViewHolder);
            }
        }
    };

    private void updateFlightFareListUi(HashMap<String, List<FlightFare>> fares) {
        if (flghtFareUiListSet == null) {
            flghtFareUiListSet = new HashSet<>();
        }
        for (String flightName : fares.keySet()) {
            FlightFare flightFare = fares.get(flightName).get(0);
            flghtFareUiListSet.add(new FlightFareDetailsUi(flightName, getTotalFare(flightFare)));
        }
    }

    private int getTotalFare(FlightFare flightFare) {
        if (flightFare.getTotalFare() != null) {
            return ((int) Double.parseDouble(flightFare.getTotalFare()));
        } else {
            return ((int) (Double.parseDouble(flightFare.getBaseFare()) + Double.parseDouble(flightFare.getFare()) + Double.parseDouble(flightFare.getTax()) - Double.parseDouble(flightFare.getDiscount())));
        }

    }

    public FlightResultProvider(FlightResult flightResult, FlightCard flightCard, CardListAdapter.FlightCardViewHolder finalConvertView) {
        this.flightResult = flightResult;
        this.flightCard = flightCard;
        this.flightCardViewHolder = finalConvertView;
    }

    public void startFetchingFlightResults() {
        makeRequestToGetFlightSearchProviders();
    }

    private void makeRequestToGetFlightSearchProviders() {
        GsonRequest request = new GsonRequest<>(Request.Method.GET, "http://www.ixigo.com/api/flights/search?apiKey=wguels!2$&origin=" + flightCard.getFromCityportKey() + "&destination=" + flightCard.getToCityportKey() + "&leave=" + flightCard.getFormDate() + "&adults=1&children=0&infants=0&class=e&locale=IN&ixi_src=iximaad&mobile=true", FlightSearchProvidersResponse.class, null, flightSearchProviderResponseListener, errorListener, null);
        request.setTag(TAG);
        IxigoDemoApp.getInsatnce().getRequestQueue().cancelAll(TAG);
        IxigoDemoApp.getInsatnce().getRequestQueue().add(request);
    }

    private void startPolling() {
        if (searchProviders.size() > 0) {
            TextUtils.join(",", searchProviders);
            makeRequestToGetFlightResults(TextUtils.join(",", searchProviders), searchToken);
        }
    }

    private void makeRequestToGetFlightResults(String searchProviders, String searchProviderKey) {
        Log.v("Polling   :","http://www.ixigo.com/api/flights/search/poll/" + searchProviderKey + "?searchProviderIds=" + searchProviders + "&ixi_src=iximaad&apiKey=wguels!2$&locale=IN&ixi_src=iximaad");
        GsonRequest request = new GsonRequest<>(Request.Method.GET, "http://www.ixigo.com/api/flights/search/poll/" + searchProviderKey + "?searchProviderIds=" + searchProviders + "&ixi_src=iximaad&apiKey=wguels!2$&locale=IN&ixi_src=iximaad", FlightDetailsResponse.class, null, flightPollingResponseListener, errorListener, null);
        request.setTag(TAG);
        IxigoDemoApp.getInsatnce().getRequestQueue().add(request);
    }


    public interface FlightResult {
        void onResult(List<FlightFareDetailsUi> flightFareDetailsUiList, CardListAdapter.FlightCardViewHolder flightCardViewHolder);

    }
}
