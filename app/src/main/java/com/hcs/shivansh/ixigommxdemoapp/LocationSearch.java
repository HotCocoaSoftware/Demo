package com.hcs.shivansh.ixigommxdemoapp;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.hcs.shivansh.ixigommxdemoapp.model.api.FlightCity;
import com.hcs.shivansh.ixigommxdemoapp.model.api.FlightCityResponse;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LocationSearchCallBacks} interface
 * to handle interaction events.
 * Use the {@link LocationSearch#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LocationSearch extends android.support.v4.app.Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "search_type";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = LocationSearch.class.getCanonicalName();

    // TODO: Rename and change types of parameters
    private SearchType searchType;
    private LocationSearchCallBacks mListener;
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(LocationSearch.this.getActivity(), "Some network occured while fetching", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
        }
    };
    private List<FlightCity> flightCityList;
    private Response.Listener<FlightCityResponse> flightCityResponseListener = new Response.Listener<FlightCityResponse>() {
        @Override
        public void onResponse(FlightCityResponse flightCityResponse) {
            if (flightCityResponse.getFlightCityList() != null && flightCityResponse.getFlightCityList().size() > 0) {
                locationListAdapter.clear();
                locationListAdapter.addAll(flightCityResponse.getflightCityNames());
                locationListAdapter.notifyDataSetChanged();
                flightCityList = flightCityResponse.getFlightCityList();
            }
            Log.v(TAG, "response recieved");
            progressBar.setVisibility(View.GONE);
        }
    };
    private ArrayAdapter<String> locationListAdapter;
    private ArrayList<String> cityNameList;
    private ProgressBar progressBar;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LocationSearch.
     */
    // TODO: Rename and change types and number of parameters
    public static LocationSearch newInstance(SearchType searchType) {
        //

        LocationSearch fragment = new LocationSearch();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, searchType.getValue());
        fragment.setArguments(args);
        return fragment;
    }

    public LocationSearch() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if (getArguments().getInt(ARG_PARAM1) == 1) {
                searchType = SearchType.HOTEL_CITY;
            } else {
                searchType = SearchType.FLIGHT;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_location_search, container, false);
        final EditText loactionEditTextView = (EditText) view.findViewById(R.id.location_auto_complete_text_view);
        loactionEditTextView.requestFocus();
        ListView locationListView = (ListView) view.findViewById(R.id.location_list);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        cityNameList = new ArrayList<String>();
        locationListAdapter = new ArrayAdapter<String>(LocationSearch.this.getActivity(), android.R.layout.simple_list_item_1, cityNameList);
        locationListView.setAdapter(locationListAdapter);
        loactionEditTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3) {
                    makeFlightSearchRequest(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        view.findViewById(R.id.icon_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loactionEditTextView.setText("");
                locationListAdapter.clear();
                locationListAdapter.notifyDataSetChanged();
            }
        });
        locationListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (searchType.equals(SearchType.FLIGHT)) {
                    ((LocationSearchCallBacks) getActivity()).setSelectedFlightLocation(flightCityList.get(position).getFlightCityKey());
                }
                getActivity().onBackPressed();
            }
        });
        return view;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface LocationSearchCallBacks {
        // TODO: Update argument type and name
        public void setSelectedPosition(int position);

        public void setForOrToFlightLocationSelected(int value);

        public void setSelectedFlightLocation(String locationKey);
    }

    public enum SearchType {
        FLIGHT(0), HOTEL_CITY(1);
        private int value;

        SearchType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    private void makeFlightSearchRequest(String query) {
        GsonRequest request = new GsonRequest<FlightCityResponse>(Request.Method.GET, "http://www.ixigo.com/api/flights/locations/search?apiKey=wguels!2$&q=" + query, FlightCityResponse.class, null, flightCityResponseListener, errorListener, getFlightCityParammap(query));
        request.setTag(TAG);
        IxigoDemoApp.getInsatnce().getRequestQueue().cancelAll(TAG);
        IxigoDemoApp.getInsatnce().getRequestQueue().add(request);
        progressBar.setVisibility(View.VISIBLE);
    }

    private Map<String, String> getFlightCityParammap(String query) {
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("apiKey", "wguels!2$");
        paramMap.put("q", query);
        return paramMap;
    }

}
