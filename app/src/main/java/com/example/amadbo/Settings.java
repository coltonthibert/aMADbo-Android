package com.example.amadbo;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

/**
 * Manages application settings using SharedPreferences.
 */
public class Settings {

    // Singleton instance
    private static Settings instance;
    // Shared preferences file name
    private static final String PREFS_NAME = "amadbo_prefs";

    // Keys for settings
    private static final String ANIMATION_PREF_KEY = "animation_disabled";
    public static final String SOUND_PREF_KEY = "sound_disabled";
    public static final String MUSIC_PREF_KEY = "music_disabled";
    public static final String SEARCH_LAYOUT_PREF_KEY = "search_layout";

    // Shared preferences object
    private final SharedPreferences preferences;

    // Private constructor to enforce singleton pattern and initialize preferences
    private Settings(Context context) {
        preferences = context.getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        // Default value for animation preference
        preferences.edit().putBoolean(ANIMATION_PREF_KEY, false).apply();
        // Default value for sound preference
        preferences.edit().putBoolean(SOUND_PREF_KEY, false).apply();
        // Default value for search layout preference
        preferences.edit().putBoolean(SEARCH_LAYOUT_PREF_KEY, false).apply();
        // Default value for music preference
        preferences.edit().putBoolean(MUSIC_PREF_KEY, false).apply();
    }

    /**
     * Gets the singleton instance of Settings.
     *
     * @param context The context of the application.
     * @return The singleton instance of Settings.
     */
    public static synchronized Settings getInstance(Context context) {
        if (instance == null) {
            instance = new Settings(context.getApplicationContext());
        }
        return instance;
    }

    /**
     * Gets the shared preferences.
     *
     * @return The shared preferences.
     */
    public SharedPreferences getPreferences() {
        return preferences;
    }

    /**
     * Gets the shared preferences file name.
     *
     * @return The shared preferences file name.
     */
    public static String getPrefsName() {
        return PREFS_NAME;
    }

    /**
     * Gets the animation preference key.
     *
     * @return The animation preference key.
     */
    public static String getAnimationPrefKey() {
        return ANIMATION_PREF_KEY;
    }

    /**
     * Gets the sound preference key.
     *
     * @return The sound preference key.
     */
    public static String getSoundPrefKey() {
        return SOUND_PREF_KEY;
    }

    /**
     * Gets the music preference key.
     *
     * @return The music preference key.
     */
    public static String getMusicPrefKey() {
        return MUSIC_PREF_KEY;
    }

    /**
     * Gets the search layout preference key.
     *
     * @return The search layout preference key.
     */
    public static String getSearchLayoutPrefKey() {
        return SEARCH_LAYOUT_PREF_KEY;
    }

    /**
     * Sets the animation preference.
     *
     * @param context The context of the application.
     * @param value   The value to set for the animation preference.
     */
    public static void setAnimationPref(Context context, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(ANIMATION_PREF_KEY, value).apply();
    }

    /**
     * Gets the animation preference.
     *
     * @param context The context of the application.
     * @return The value of the animation preference.
     */
    public static boolean getAnimationPref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getBoolean(ANIMATION_PREF_KEY, true);
    }

    /**
     * Sets the sound preference.
     *
     * @param context The context of the application.
     * @param value   The value to set for the sound preference.
     */
    public static void setSoundPref(Context context, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("Settings", "Sound preference set to " + value);
        preferences.edit().putBoolean(SOUND_PREF_KEY, value).apply();
    }

    /**
     * Gets the sound preference.
     *
     * @param context The context of the application.
     * @return The value of the sound preference.
     */
    public static boolean getSoundPref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("Settings", "Sound preference is " + preferences.getBoolean(SOUND_PREF_KEY, true));
        return preferences.getBoolean(SOUND_PREF_KEY, true);

    }

    /**
     * Sets the search layout preference.
     *
     * @param context The context of the application.
     * @param value   The value to set for the search layout preference.
     */
    public static void setSearchLayoutPref(Context context, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(SEARCH_LAYOUT_PREF_KEY, value).apply();
        Log.d("Settings", "Search layout preference set to " + value);
    }

    /**
     * Gets the search layout preference.
     *
     * @param context The context of the application.
     * @return The value of the search layout preference.
     */
    public static boolean getSearchLayoutPref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("Settings", "Search layout preference is " + preferences.getBoolean(SEARCH_LAYOUT_PREF_KEY, true));
        return preferences.getBoolean(SEARCH_LAYOUT_PREF_KEY, true);
    }

    /**
     * Sets the music preference.
     *
     * @param context The context of the application.
     * @param value   The value to set for the music preference.
     */
    public static void setMusicPref(Context context, boolean value) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putBoolean(MUSIC_PREF_KEY, value).apply();

        // Play or stop background music based on preference
        if (value) {
            AppAudioService.playBackgroundMusic(context, R.raw.bgm_reverb);
        } else {
            AppAudioService.stopBackgroundMusic();
        }

        Log.d("Settings", "Music preference set to " + value);
    }

    /**
     * Gets the music preference.
     *
     * @param context The context of the application.
     * @return The value of the music preference.
     */
    public static boolean getMusicPref(Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        Log.d("Settings", "Music preference is " + preferences.getBoolean(MUSIC_PREF_KEY, true));
        return preferences.getBoolean(MUSIC_PREF_KEY, true);
    }
}
