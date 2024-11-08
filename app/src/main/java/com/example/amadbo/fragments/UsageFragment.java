/**
 * A fragment that displays the usage details of an Amiibo, including games and their usage information.
 */
package com.example.amadbo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.amadbo.MainActivity;
import com.example.amadbo.R;
import com.example.amadbo.adapters.UsageAdapter;
import com.example.amadbo.api.AmiiboSingleton;
import com.example.amadbo.models.Usage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A fragment that displays the usage details of an Amiibo, including games and their usage information.
 * The usage details are fetched from the amiibo API and displayed in a RecyclerView.
 */
public class UsageFragment extends Fragment {
    private static int mode = 0;
    private static String amiiboHead;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_usage, container, false);

        // Instantiate RecyclerView and Adapter
        RecyclerView recyclerView = view.findViewById(R.id.List_Usage);
        UsageAdapter adapter = new UsageAdapter(new ArrayList<>(), getContext());

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        if (getArguments() != null) {
            amiiboHead = getArguments().getString("amiiboHead");
            mode = getArguments().getInt("mode");
        }

        Log.d("UsageFragment", "Mode: " + mode);

        fetchAmiiboUsageData(mode, adapter);

        return view;
    }

    /**
     * Fetches Amiibo usage data based on the specified mode and updates the adapter with the retrieved data.
     *
     * @param mode    The mode of usage (1 for switch games, 2 for 3DS games).
     * @param adapter The UsageAdapter to update with the fetched data.
     */
    private void fetchAmiiboUsageData(int mode, UsageAdapter adapter) {
        String url = "https://www.amiiboapi.com/api/amiibo/?head=" + amiiboHead + "&showusage";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    ArrayList<Usage> usageList = new ArrayList<>();
                    JSONArray gamesArray = new JSONArray();
                    JSONObject amiiboObj = response.getJSONArray("amiibo").getJSONObject(0);
                    if (mode != 1){ // 3DS Layout
                        gamesArray = amiiboObj.getJSONArray("games3DS");
                        // Set Title to 3DS Usage
                        MainActivity.setTitleName("3DS Usage");
                    }else { // SWITCH LAYOUT
                        gamesArray = amiiboObj.getJSONArray("gamesSwitch");
                        // Set Title to Switch Usage
                        MainActivity.setTitleName("Switch Usage");

                    }
                    for (int i = 0; i < gamesArray.length(); i++) {
                        JSONObject game = gamesArray.getJSONObject(i);
                        JSONArray amiiboUsageArray = game.getJSONArray("amiiboUsage");
                        for (int j = 0; j < amiiboUsageArray.length(); j++) {
                            JSONObject amiiboUsage = amiiboUsageArray.getJSONObject(j);
                            String usage = amiiboUsage.getString("Usage");
                            boolean write = amiiboUsage.getBoolean("write");
                            String gameName = game.getString("gameName");
                            usageList.add(new Usage(gameName, usage, write));
                            Log.d("Amiibo Info", "Usage: " + usage + ", write: " + write + ", gameName: " + gameName);
                        }
                    }
                    adapter.setUsages(usageList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("UsageFragment", error.getMessage());
            }
        });
        AmiiboSingleton.getInstance(getContext()).getRequestQueue().add(request);
    }
}
