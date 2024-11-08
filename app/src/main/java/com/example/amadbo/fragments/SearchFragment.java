package com.example.amadbo.fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.amadbo.R;
import com.example.amadbo.Settings;
import com.example.amadbo.adapters.AmiiboSearchRVAdapter;
import com.example.amadbo.api.AmiiboSingleton;
import com.example.amadbo.database.AmiiboDatabase;
import com.example.amadbo.models.Amiibo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Fragment that allows the user to search for Amiibo by name, game series, or Amiibo series.
 * The user can also filter the search results by figure, card, or other types of Amiibo.
 * The search results are displayed in a RecyclerView.
 */
public class SearchFragment extends Fragment {

    // Views
    private EditText searchBar;
    private RecyclerView recyclerView;
    private TextView notFoundText;
    private View noAmiiboFound;

    // === SETTINGS ===
    // Search by
    private RadioGroup searchByGroup;
    private RadioButton searchName;
    private RadioButton searchGameSeries;
    private RadioButton searchAmiiboSeries;
    private String searchBy = "name";
    // Include
    private LinearLayout includeGroup;
    private CheckBox includeFigures;
    private CheckBox includeCards;
    private CheckBox includeOther;
    private List<String> includeList = new ArrayList<>();

    // Data
    private ArrayList<Amiibo> amiibos;
    private AmiiboSearchRVAdapter adapter;

    // Database
    private AmiiboDatabase amiiboDatabase;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        includeList.add("Figure");
        includeList.add("Card");
        includeList.add("Yarn");
        includeList.add("Band");

        searchBar = view.findViewById(R.id.searchText);
        noAmiiboFound = view.findViewById(R.id.no_amiibo_view);
        notFoundText = noAmiiboFound.findViewById(R.id.not_found_text);
        notFoundText.setText(R.string.amiibo_search_prompt);
        recyclerView = view.findViewById(R.id.amiiboSearchRecyclerView);

        amiiboDatabase = new AmiiboDatabase(getActivity());

        amiibos = new ArrayList<>();
        adapter = new AmiiboSearchRVAdapter(amiibos, getActivity());

        recyclerView.setAdapter(adapter);

        // Set the layout manager based on the user's preference
        if (!Settings.getSearchLayoutPref(getContext())){
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }else{
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        }


        //Listen for changes to the radio group
        searchByGroup = view.findViewById(R.id.searchByGroup);
        searchByGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radio_name) {
                searchBy = "name";
            } else if (checkedId == R.id.radio_gameSeries) {
                searchBy = "gameSeries";
            } else if (checkedId == R.id.radio_amiiboSeries) {
                searchBy = "amiiboSeries";
            }
            // Fetch amiibo data with the new searchBy value
            String query = searchBar.getText().toString().trim();
            fetchAmiiboData(query, searchBy, includeList);
        });

        // Listen for changes to the include checkboxes
        includeGroup = view.findViewById(R.id.includeGroup);
        includeFigures = view.findViewById(R.id.checkbox_figures);
        includeCards = view.findViewById(R.id.checkbox_cards);
        includeOther = view.findViewById(R.id.checkbox_other);


        includeFigures.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                includeList.add("Figure");
            } else {
                includeList.remove("Figure");
            }
            String query = searchBar.getText().toString().trim();
            fetchAmiiboData(query, searchBy, includeList);
        });

        includeCards.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                includeList.add("Card");
            } else {
                includeList.remove("Card");
            }
            String query = searchBar.getText().toString().trim();
            fetchAmiiboData(query, searchBy, includeList);
        });

        includeOther.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                includeList.add("Yarn");
                includeList.add("Band");
            } else {
                includeList.remove("Yarn");
                includeList.remove("Band");
            }
            String query = searchBar.getText().toString().trim();
            fetchAmiiboData(query, searchBy, includeList);
        });

        searchBar.addTextChangedListener(getTextWatcher());

        return view;
    }

    /**
     * Returns a TextWatcher that listens for changes to the search bar.
     * When the user types in the search bar, the search results are updated.
     */
    private TextWatcher getTextWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                fetchAmiiboData(query, searchBy, includeList);
            }
        };
    }

    /**
     * Fetches Amiibo data from the local database based on the search query, searchBy value, and includeList.
     * The search results are displayed in the RecyclerView.
     */
    private void fetchAmiiboData(String query, String searchBy, List<String> includeList) {
        if (TextUtils.isEmpty(query)) {
            amiibos.clear();
            adapter.notifyDataSetChanged();
            showNoAmiiboFoundView();
            return;
        }

        // Search in local database
        amiibos = amiiboDatabase.searchAmiibos(query, searchBy, includeList);
        adapter.setAmiibos(amiibos);

        // Show no amiibo found view if no amiibo found, else hide it
        if (amiibos.isEmpty()) {
            showNoAmiiboFoundView();
        } else {
            hideNoAmiiboFoundView();
        }

    }

    /**
     * Shows the "No Amiibo Found" view.
     */
    private void showNoAmiiboFoundView() {
        noAmiiboFound.setVisibility(View.VISIBLE);

        // Set the text to a random value from the array in strings.xml
        String[] noAmiiboFoundTexts = getResources().getStringArray(R.array.no_amiibo_found_strings);
        Random random = new Random();
        notFoundText.setText(noAmiiboFoundTexts[random.nextInt(noAmiiboFoundTexts.length)]);
    }

    /**
     * Hides the "No Amiibo Found" view.
     */
    private void hideNoAmiiboFoundView() {
        noAmiiboFound.setVisibility(View.GONE);
    }
}
