
package com.example.amadbo.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.widget.SwitchCompat;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.amadbo.AppAudioService;
import com.example.amadbo.R;
import com.example.amadbo.Settings;
import com.example.amadbo.database.AmiiboDatabase;

@SuppressLint("NonConstantResourceId")

/**
 * Fragment for the settings screen.
 * Allows the user to change settings such as disabling animations, music, and sound effects.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener {

    // Switches for animation and sound settings
    private SwitchCompat disableAnimationSwitch;
    private SwitchCompat disableMusicSwitch;
    private SwitchCompat disableSoundSwitch;
    private SwitchCompat searchLayoutSwitch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        // Initialize UI elements
        ConstraintLayout disableAnimation = view.findViewById(R.id.itemOption_Animations);
        disableAnimationSwitch = view.findViewById(R.id.disableAnimSwitch);
        ConstraintLayout disableSound = view.findViewById(R.id.itemOption_Sound);
        disableSoundSwitch = view.findViewById(R.id.disableSoundSwitch);
        ConstraintLayout disableMusic = view.findViewById(R.id.itemOption_Music);
        disableMusicSwitch = view.findViewById(R.id.disableMusicSwitch);
        ConstraintLayout searchLayout = view.findViewById(R.id.itemOption_SearchLayout);
        searchLayoutSwitch = view.findViewById(R.id.searchLayoutSwitch);
        ConstraintLayout clearAppData = view.findViewById(R.id.itemOption_Clear);

        ImageView gearIcon = view.findViewById(R.id.Gear);
        gearIcon.setAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.settings_gear_rotate));

        // Set click listeners for UI elements
        disableAnimation.setOnClickListener(this);
        disableMusic.setOnClickListener(this);
        disableSound.setOnClickListener(this);
        searchLayout.setOnClickListener(this);
        clearAppData.setOnClickListener(this);

        // Update switches based on current preferences
        updateSwitches();

        return view;
    }

    /**
     * Updates switches based on current preferences.
     */
    private void updateSwitches() {
        boolean isAnimationEnabled = !Settings.getAnimationPref(getContext());
        boolean isMusicEnabled = !Settings.getMusicPref(getContext());
        boolean isSoundEnabled = !Settings.getSoundPref(getContext());
        boolean isSearchLayoutEnabled = !Settings.getSearchLayoutPref(getContext());

        disableAnimationSwitch.setChecked(isAnimationEnabled);
        disableMusicSwitch.setChecked(isMusicEnabled);
        disableSoundSwitch.setChecked(isSoundEnabled);
        searchLayoutSwitch.setChecked(isSearchLayoutEnabled);
    }

    /**
     * Handles click events for different toggle switches.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        // Handle click events for different UI elements
        if (v.getId() == R.id.itemOption_Animations) {
            toggleAnimation();
        } else if (v.getId() == R.id.itemOption_Music) {
            toggleMusic();
        } else if (v.getId() == R.id.itemOption_Sound) {
            toggleSound();
        } else if (v.getId() == R.id.itemOption_Clear) {
            showRemoveConfirmation();
        } else if (v.getId() == R.id.itemOption_SearchLayout) {
            toggleSearchLayout();
        }
    }

    /**
     * Toggles animation setting and updates preferences.
     */
    private void toggleAnimation() {
        boolean isChecked = !disableAnimationSwitch.isChecked();
        disableAnimationSwitch.setChecked(isChecked);
        Settings.setAnimationPref(getContext(), !isChecked);
        logStateChange("Animation", isChecked);
    }

    /**
     * Toggles music setting and updates preferences.
     */
    private void toggleMusic() {
        boolean isChecked = !disableMusicSwitch.isChecked();
        disableMusicSwitch.setChecked(isChecked);
        Settings.setMusicPref(getContext(), !isChecked);
        logStateChange("Music", isChecked);
    }

    /**
     * Toggles sound setting and updates preferences.
     */
    private void toggleSound() {
        boolean isChecked = !disableSoundSwitch.isChecked();
        disableSoundSwitch.setChecked(isChecked);
        Settings.setSoundPref(getContext(), !isChecked);
        logStateChange("Sound", isChecked);
    }

    /**
     * Toggles search layout setting and updates preferences.
     */
    private void toggleSearchLayout() {
        boolean isChecked = !searchLayoutSwitch.isChecked();
        logStateChange("Search Layout", isChecked);
        searchLayoutSwitch.setChecked(isChecked);
        Settings.setSearchLayoutPref(getContext(), !isChecked);
    }

    /**
     * Logs state change of settings.
     *
     * @param name     The name of the setting.
     * @param isChecked True if the setting is enabled, false otherwise.
     */
    private void logStateChange(String name, boolean isChecked) {
        String state = isChecked ? "disabled" : "enabled";
        Log.d("SettingsFragment", name + " " + state);
    }

    /**
     * Shows a confirmation dialog before clearing app data.
     */
    private void showRemoveConfirmation() {
        // Create a confirmation dialog to confirm clearing app data
        AlertDialog.Builder confirmationDialogBuilder = new AlertDialog.Builder(requireContext());
        confirmationDialogBuilder.setTitle("Clear app data");
        confirmationDialogBuilder.setMessage("Are you sure you want to clear all data from the app? This action cannot be undone.");
        confirmationDialogBuilder.setIcon(R.drawable.ic_amiibo_error);

        // Set positive button action to clear data and restart the app
        confirmationDialogBuilder.setPositiveButton("Yes", (dialogInterface, i) -> {
            // Clear the app data
            AmiiboDatabase.get(getContext()).clearDatabase(true);

            // Create a dialog to inform the user that data has been cleared and the app will restart
            AlertDialog.Builder restartDialogBuilder = new AlertDialog.Builder(requireContext());
            restartDialogBuilder.setTitle("Data cleared");
            restartDialogBuilder.setMessage("All data has been cleared from the app. The app will now close.");

            // Set positive button action to restart the app
            restartDialogBuilder.setPositiveButton("OK", (dialogInterface1, i1) -> {
                // Close the app
                System.exit(0);
            });

            // Show the restart dialog
            restartDialogBuilder.show();
        });

        // Set negative button action to cancel the operation
        confirmationDialogBuilder.setNegativeButton("No", null);

        AppAudioService.playSound(requireContext(), R.raw.add, false);
        // Show the confirmation dialog
        confirmationDialogBuilder.show();
    }
}
