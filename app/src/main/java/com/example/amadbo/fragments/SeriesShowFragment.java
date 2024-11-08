package com.example.amadbo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.amadbo.MainActivity;
import com.example.amadbo.R;
import com.example.amadbo.adapters.SeriesShowcaseAdapter;
import com.example.amadbo.database.AmiiboDatabase;
import com.example.amadbo.models.Amiibo;

import java.util.ArrayList;
import java.util.List;

/**
 * Fragment to display amiibo within a specific amiibo series
 * e.g. Super Smash Bros. series, The Legend of Zelda series
 */
public class SeriesShowFragment extends Fragment {

    private ArrayList<Amiibo> amiibos;
    private SeriesShowcaseAdapter adapter;

    private AmiiboDatabase amiiboDatabase;
    private RecyclerView seriesShowcaseRecyclerView;

    public String series;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_series_show, container, false);

        // Get Components
        seriesShowcaseRecyclerView = view.findViewById(R.id.list);
        adapter = new SeriesShowcaseAdapter(amiibos, getContext());

        // Retrieve bundled Amiibo object
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("series")) {
            series = arguments.getString("series");
            // Set Title to series name
            MainActivity.setTitleName(series);
        }
        Log.d("series Showcase", series);
        fetchAmiiboData();

        // Display the series Amiibo
        seriesShowcaseRecyclerView.setAdapter(adapter);
        seriesShowcaseRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));

        return view;
    }


    /**
     * Fetches the Amiibo data from the database
     */
    private void fetchAmiiboData() {
        // Search in local database
        amiibos = AmiiboDatabase.get(getContext()).searchAmiibos(series, "amiiboSeries", new ArrayList<>());
        adapter.setSeries(amiibos);
    }

}
