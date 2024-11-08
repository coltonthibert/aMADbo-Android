package com.example.amadbo.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amadbo.AppAudioService;
import com.example.amadbo.R;
import com.example.amadbo.Settings;
import com.example.amadbo.database.AmiiboDatabase;
import com.example.amadbo.models.Amiibo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;


/**
 * Home Fragment for the application.
 * Displays the total number of Amiibo owned by the user, a button to navigate to the App Guide,
 * and a grid of owned Amiibo images.
 */
public class HomeFragment extends Fragment {

    // Views
    private TextView amiiboCount;
    private ImageView currentTotalImageView;
    private Button appGuideButton;
    private ImageView[] amiiboImages = new ImageView[18];


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Get Components
        ConstraintLayout header = view.findViewById(R.id.header_layout);
        amiiboCount = view.findViewById(R.id.amiibo_amount);
        currentTotalImageView = view.findViewById(R.id.currentTotalBg);
        appGuideButton = view.findViewById(R.id.appGuideButton);

        getAmiiboImages(view);

        // Animation
        Animation fadeIn = AnimationUtils.loadAnimation(getContext(), R.anim.fadein_fromtop);

        // Start Animation
        if (Settings.getAnimationPref(getContext())) {
            header.startAnimation(fadeIn);
        }
        // Set Amiibo Count
        int ownedAmiiboCount = AmiiboDatabase.get(getContext()).getOwnedAmiiboCount();
        amiiboCount.setText(String.valueOf(ownedAmiiboCount));

        currentTotalImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigate to my amiibo
                Navigation.findNavController(view).navigate(R.id.nav_myAmiibo);
            }
        });

        appGuideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Navigate to app guide
                AppAudioService.playSound(getContext(), R.raw.next_screen, false);
                Navigation.findNavController(view).navigate(R.id.action_nav_home_to_appGuideVPHostFragment);
            }
        });

        return view;
    }

    /**
     * Get the Amiibo images from the layout file and load the images into the image views.
     * @param view The view of the fragment.
     */
    private void getAmiiboImages(View view) {
        // Get Amiibo Images
        for (int i = 0; i < 18; i++) {
            String id = "homeAmiibo_" + (i+1);
            Log.d("ID", id);
            int resID = getResources().getIdentifier(id, "id", getContext().getPackageName());
            amiiboImages[i] = view.findViewById(resID);
        }
        // Get up to a maximum of 18 amiibo from the owned amiibo table
        ArrayList<Amiibo> ownedAmiibo = AmiiboDatabase.get(getContext()).getAllOwnedAmiibos();
        // Shuffle the list so that the amiibo images are displayed in a random order
        Collections.shuffle(ownedAmiibo);
        // Load the images into the image views
        for (int i = 0; i < ownedAmiibo.size(); i++) {
            // Only display up to 18 amiibo
            if (i >= 18) {
                break;
            }

            Amiibo amiibo = ownedAmiibo.get(i);
            Picasso.get()
                    .load(amiibo.getImage())
                    .resize(400, 600)
                    .into(amiiboImages[i]);
        }
    }
}