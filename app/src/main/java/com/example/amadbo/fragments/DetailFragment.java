/**
 * A fragment that displays detailed information about an Amiibo and allows users to add or remove it from their collection.
 */
package com.example.amadbo.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amadbo.AppAudioService;
import com.example.amadbo.MainActivity;
import com.example.amadbo.R;
import com.example.amadbo.database.AmiiboDatabase;
import com.example.amadbo.models.Amiibo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.Objects;

/**
 * DetailFragment displays detailed information about an Amiibo and allows users to add or remove it from their collection.
 * It also displays the Amiibo's image, name, release date, type, series, and game series.
 * Users can add or remove the Amiibo from their collection by clicking the floating action button.
 */
public class DetailFragment extends Fragment {

    // Properties
    private Amiibo currentAmiibo;
    boolean isInMyAmiibo = false;

    // Views
    private FloatingActionButton addButton;
    private ImageView amiiboImage;
    private TextView amiiboName;
    private TextView amiiboRelease;
    private TextView amiiboType;
    private TextView amiiboSeries;
    private TextView amiiboGameSeries;
    private ConstraintLayout switchGames;
    private ConstraintLayout _3dsGames;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        // Get the views
        amiiboImage = view.findViewById(R.id.detail_image);
        amiiboName = view.findViewById(R.id.detail_name);
        amiiboRelease = view.findViewById(R.id.detail_release);
        amiiboType = view.findViewById(R.id.detail_type);
        amiiboSeries = view.findViewById(R.id.detail_series);
        amiiboGameSeries = view.findViewById(R.id.detail_gameSeries);
        addButton = view.findViewById(R.id.addButton);
        switchGames = view.findViewById(R.id.Usage_SwitchGames);
        _3dsGames = view.findViewById(R.id.Usage_3DSGames);

        // Get the Amiibo object from the arguments
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey("amiibo")) {
            currentAmiibo = arguments.getParcelable("amiibo");
        }

        // Update the button state based on whether the Amiibo is in the user's collection
        if (AmiiboDatabase.get(getContext()).isInOwnedAmiibos(currentAmiibo.getId())) {
            updateButtonState(R.drawable.ic_baseline_remove_24, ColorStateList.valueOf(getResources().getColor(R.color.fab_red)));
            isInMyAmiibo = true;
        } else {
            updateButtonState(R.drawable.ic_baseline_add_24, ColorStateList.valueOf(getResources().getColor(R.color.fab_green)));
            isInMyAmiibo = false;
        }

        // Set the views based on the Amiibo object
        if (currentAmiibo != null) {
            MainActivity.setTitleName("");
            Picasso.get().load(currentAmiibo.getImage()).into(amiiboImage);
            amiiboName.setText(currentAmiibo.getName());
            amiiboRelease.setText(formatReleaseDate(currentAmiibo.getReleaseNA()));
            amiiboType.setText(currentAmiibo.getType());
            amiiboSeries.setText(currentAmiibo.getAmiiboSeries());
            amiiboGameSeries.setText(currentAmiibo.getGameSeries());
        }

        // Set the click listeners
        switchGames.setOnClickListener(v -> navigateUsage(1));
        _3dsGames.setOnClickListener(v -> navigateUsage(2));

        // Set the click listener for the floating action button
        addButton.setOnClickListener(v -> {
            AppAudioService.playSound(getContext(), R.raw.add, false);
            if (isInMyAmiibo) {
                showRemoveConfirmation();
            } else {
                showAddConfirmation();
            }
        });

        return view;
    }

    /**
     * Navigates to the UsageFragment with the specified mode and Amiibo head.
     *
     * @param mode The mode of usage (1 for switch games, 2 for 3DS games).
     */
    private void navigateUsage(int mode) {
        Bundle bundle = new Bundle();
        bundle.putInt("mode", mode);
        bundle.putString("amiiboHead", currentAmiibo != null ? currentAmiibo.getHead() : "");
        Navigation.findNavController(requireView()).navigate(R.id.action_nav_detail_to_nav_usage, bundle);

        AppAudioService.playSound(getContext(), R.raw.next_screen, false);
    }

    /**
     * Displays a confirmation dialog for removing the Amiibo from the collection.
     */
    private void showRemoveConfirmation() {
        new AlertDialog.Builder(getContext())
                .setTitle("Remove from Collection")
                .setMessage("Do you want to remove " +
                        currentAmiibo.getName() + " from your collection?")
                .setIcon(R.drawable.ic_amiibo)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    AmiiboDatabase.get(getContext()).setNotOwnedAmiibo(currentAmiibo.getId());
                    updateButtonState(R.drawable.ic_baseline_add_24, ColorStateList.valueOf(getResources().getColor(R.color.fab_green)));
                    isInMyAmiibo = false;

                    AppAudioService.playSound(getContext(), R.raw.alert, false);
                    Toast.makeText(getContext(), amiiboName.getText().toString().toUpperCase()
                            + " Removed from Collection", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Displays a confirmation dialog for adding the Amiibo to the collection.
     */
    private void showAddConfirmation() {
        new AlertDialog.Builder(getContext())
                .setTitle("Add to Collection")
                .setMessage("Do you want to add " +
                        currentAmiibo.getName() + " to your collection?")
                .setIcon(R.drawable.ic_amiibo)
                .setPositiveButton("Yes", (dialogInterface, i) -> {
                    AmiiboDatabase.get(getContext()).setOwnedAmiibo(currentAmiibo.getId());
                    updateButtonState(R.drawable.ic_baseline_remove_24, ColorStateList.valueOf(getResources().getColor(R.color.fab_red)));
                    isInMyAmiibo = true;

                    AppAudioService.playSound(getContext(), R.raw.alert, false);
                    Toast.makeText(getContext(), amiiboName.getText().toString().toUpperCase()
                            + " Added to Collection", Toast.LENGTH_LONG).show();
                })
                .setNegativeButton("No", null)
                .show();
    }

    /**
     * Updates the state of the add/remove button with the provided icon resource and color.
     *
     * @param iconResource  The resource ID of the icon to be set on the button.
     * @param colorStateList The ColorStateList to be used for the background tint of the button.
     */
    private void updateButtonState(int iconResource, ColorStateList colorStateList) {
        addButton.setImageResource(iconResource);
        addButton.setBackgroundTintList(colorStateList);
    }

    /**
     * Formats the release date string to a more readable format.
     *
     * @param date The release date string to be formatted.
     * @return The formatted release date string.
     */
    private String formatReleaseDate(String date) {
        // If the date is null, return "N/A". Otherwise, format the date like "December 31, 2021".
        return Objects.equals(date, "null") ? "N/A" :
                new SimpleDateFormat("MMMM d, yyyy", Locale.US)
                        .format(Date.valueOf(date));
    }
}
