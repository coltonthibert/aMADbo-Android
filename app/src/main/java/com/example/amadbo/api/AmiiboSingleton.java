package com.example.amadbo.api;

import android.content.Context;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Singleton class for managing Volley request queue for Amiibo API.
 */
public class AmiiboSingleton {

    private static AmiiboSingleton instance;
    private RequestQueue requestQueue;
    private static Context context;

    /**
     * Private constructor to prevent instantiation from outside.
     * @param context The application context.
     */
    private AmiiboSingleton(Context context) {
        this.context = context;
    }

    /**
     * Returns the singleton instance of AmiiboSingleton.
     * @param context The application context.
     * @return The singleton instance.
     */
    public static AmiiboSingleton getInstance(Context context) {
        if (instance == null) {
            instance = new AmiiboSingleton(context);
        }
        return instance;
    }

    /**
     * Gets the request queue for making network calls.
     * @return The RequestQueue instance.
     */
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            // Creating new request queue if not already initialized
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }
}
