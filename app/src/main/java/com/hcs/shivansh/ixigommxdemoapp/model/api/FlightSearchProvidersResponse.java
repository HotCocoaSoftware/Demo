package com.hcs.shivansh.ixigommxdemoapp.model.api;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by shivansh on 26/06/15.
 */
public class FlightSearchProvidersResponse {
    private List<Integer> searchProviders;
    @SerializedName("searchToken")
    private String searchToken;

    public List<Integer> getSearchProviders() {
        return searchProviders;
    }

    public String getSearchToken() {
        return searchToken;
    }
}
