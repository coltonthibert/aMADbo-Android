package com.example.amadbo.api;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.amadbo.AppAudioService;
import com.example.amadbo.R;
import com.example.amadbo.database.AmiiboDatabase;
import com.example.amadbo.models.Amiibo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

/**
 * Manages the updating of Amiibo data from the API.
 */
public class AmiiboUpdateManager {

    private static final String LAST_UPDATE_KEY = "last_update_timestamp";
    private static final long UPDATE_INTERVAL = 7 * 24 * 60 * 60 * 1000; // 7 days in milliseconds
    private static Dialog dialog;

    /**
     * Checks if an update is necessary and fetches Amiibo data from the API if required.
     * @param context The context of the calling activity.
     */
    public static void checkAndUpdateAmiibos(final Context context) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        long lastUpdate = sharedPreferences.getLong(LAST_UPDATE_KEY, 0);
        long currentTime = Calendar.getInstance().getTimeInMillis();

        // Check if the update interval has elapsed or if the database is empty
        if (currentTime - lastUpdate >= UPDATE_INTERVAL || AmiiboDatabase.get(context).getAmiiboCount() == 0)   {
            // Perform update
            fetchAmiibosFromApi(context);

            // Update last update timestamp
            sharedPreferences.edit().putLong(LAST_UPDATE_KEY, currentTime).apply();

            // Display the progress dialog
            showProgressDialog(context);
        }
    }

    /**
     * Displays a progress dialog while updating Amiibo data.
     * @param context The context of the calling activity or application.
     */
    private static void showProgressDialog(Context context) {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.progress_dialog);
        dialog.setCancelable(false); // Prevent dialog from being canceled by pressing back button

        ProgressBar progressBar = dialog.findViewById(R.id.aum_progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Dismiss dialog after update completes
        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                // Handle any post-update tasks here
            }
        });
        AppAudioService.playSound(context, R.raw.load, true);
        dialog.show();
    }

    /**
     * Fetches Amiibo data from the API and updates the local database.
     * @param context The context of the calling activity or application.
     */
    private static void fetchAmiibosFromApi(Context context) {
        String url = "https://www.amiiboapi.com/api/amiibo/";
        AmiiboDatabase amiiboDatabase = AmiiboDatabase.get(context);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

                try {
                    amiiboDatabase.clearDatabase(false); // Clear the database before updating
                    amiiboDatabase.addGwimblyAmiibo(); // Add the Gwimbly amiibo (Very important)
                    JSONArray amiiboArray = response.getJSONArray("amiibo");
                    for (int i = 0; i < amiiboArray.length(); i++) {
                        JSONObject amiiboObject = amiiboArray.getJSONObject(i);
                        Amiibo amiibo = new Amiibo();
                        amiibo.setName(amiiboObject.getString("name"));
                        amiibo.setAmiiboSeries(amiiboObject.getString("amiiboSeries"));
                        amiibo.setReleaseNA(amiiboObject.getJSONObject("release").getString("na"));
                        amiibo.setGameSeries(amiiboObject.getString("gameSeries"));
                        amiibo.setType(amiiboObject.getString("type"));
                        amiibo.setHead(amiiboObject.getString("head"));
                        amiibo.setTail(amiiboObject.getString("tail"));
                        amiibo.setImage(amiiboObject.getString("image"));

                        // Add amiibo to the local database
                        amiiboDatabase.addAmiibo(amiibo);
                    }
                    // Dismiss the progress dialog after fetching is complete
                    if (dialog != null && dialog.isShowing()) {
                        AppAudioService.stopSound();
                        dialog.dismiss();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", error.getLocalizedMessage());
            }
        });
        AmiiboSingleton.getInstance(context).getRequestQueue().add(request);
    }
}
