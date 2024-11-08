package com.example.amadbo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.amadbo.R;
import com.example.amadbo.adapters.AmiiboSearchRVAdapter;
import com.example.amadbo.adapters.SeriesAdapter;
import com.example.amadbo.api.AmiiboSingleton;
import com.example.amadbo.models.Amiibo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/**
 * Displays a list of all Amiibo series in a RecyclerView.
 * Tapping on a series will display all Amiibo figures in that series.
 */
public class AmiiboSeriesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_series, container, false);


        // Instantiate RecyclerView and Adapter
        RecyclerView recyclerView = view.findViewById(R.id.series_content);
        SeriesAdapter adapter = new SeriesAdapter(new ArrayList<>(), getContext());

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), getSpanCount()));

        // Fetch all amiibo data for the initial load
        fetchAmiiboData(adapter);

        return view;
    }

    /**
     * Fetches all amiibo data from the API and populates the RecyclerView.
     *
     * @param adapter Adapter to populate with data.
     */
    private void fetchAmiiboData(SeriesAdapter adapter) {
        String url = "https://www.amiiboapi.com/api/amiiboseries/";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray seriesArray = response.getJSONArray("amiibo");
                    Set<String> uniqueSeries = new HashSet<>();
                    ArrayList<Amiibo> amiibos = new ArrayList<>();
                    for (int i = 0; i < seriesArray.length(); i++) {
                        JSONObject amiiboObject = seriesArray.getJSONObject(i);
                        String seriesName = amiiboObject.getString("name");
                        // Check if the series name is already added
                        if (!uniqueSeries.contains(seriesName)) {
                            Amiibo amiibo = new Amiibo();
                            amiibo.setAmiiboSeries(seriesName);
                            Log.d("Series", seriesName);
                            amiibos.add(amiibo);
                            uniqueSeries.add(seriesName); // Add the series name to the set
                        }
                    }
                    // Sort the amiibo by series name
                    Collections.sort(amiibos, (a, b) -> a.getAmiiboSeries().compareTo(b.getAmiiboSeries()));
                    adapter.setSeries(amiibos);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("Volley Error: " + error.getLocalizedMessage());
            }
        });
        AmiiboSingleton.getInstance(getContext()).getRequestQueue().add(request);
    }

    /**
     * Returns the number of columns to display in the RecyclerView.
     *
     * @return Number of columns to display.
     */
    private int getSpanCount() {
        // If the device is in landscape mode or is a tablet, display 3 columns, otherwise display 2 columns
        if (getResources().getConfiguration().orientation == 2
                || getResources().getConfiguration().screenWidthDp >= 600) {
            return 4;
        }else {
            return 2;
        }
    }
}
