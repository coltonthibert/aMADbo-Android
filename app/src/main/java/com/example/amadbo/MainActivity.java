package com.example.amadbo;

import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.customview.widget.Openable;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.amadbo.api.AmiiboUpdateManager;
import com.example.amadbo.databinding.ActivityMainBinding;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private BottomNavigationView navView;
    private NavController navController;
    private static TextView toolbarMainText;
    private Settings settings;

    /**
     * OnCreate method to set up the main activity.
     * @param savedInstanceState the saved instance state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settings = Settings.getInstance(getApplicationContext());
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Play background music
        playBackgroundMusic();

        // Update the amiibo data
        updateAmiibo();

        // Set up the custom action bar
        setUpActionBar();

        // Set up the floating action button
        setUpFab();

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_myAmiibo, R.id.nav_amiiboseries, R.id.nav_home, R.id.nav_search, R.id.nav_settings).build();

        navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.bottomNavigationView, navController);

        // Set Default Nav Fragment to be Home
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        bottomNav.getMenu().findItem(R.id.nav_home).setChecked(true);

        //----- v CUSTOM ACTION BAR CODE: REMOVE LATER IF ISSUES ARISE v -----//
        // Find the toolbar text in the custom toolbar layout
        toolbarMainText = findViewById(R.id.toolbarMainText);

        // Add a destination changed listener to update the title
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            // Update the title based on the current destination
            setTitleName(destination.getLabel().toString());
        });
        //----- ^ CUSTOM ACTION BAR CODE: REMOVE LATER IF ISSUES ARISE ^ -----//

        // Set up the navigation view
        setUpNavigationView();

    }

    /**
     * Update the amiibo data after a delay.
     */
    private void updateAmiibo() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                AmiiboUpdateManager.checkAndUpdateAmiibos(MainActivity.this);
            }
        }, 500);
    }

    /**
     * Method to play the background music.
     */
    public void playBackgroundMusic() {
        AppAudioService.playBackgroundMusic(getApplicationContext(), R.raw.bgm_reverb);
    }

    /**
     * Method to stop the background music.
     */
    public void stopBackgroundMusic() {
        AppAudioService.stopBackgroundMusic();
    }

    /**
     * Override the onSupportNavigateUp method to navigate up when the back button is pressed.
     */
    @Override
    public boolean onSupportNavigateUp() {
        navController = Navigation.findNavController(this,R.id.nav_host_fragment_activity_main);
        AppAudioService.playSound(getApplicationContext(), R.raw.next_screen_rev, false);
        return NavigationUI.navigateUp(navController, (Openable) null) || super.onSupportNavigateUp();
    }

    /**
     * Override the onPause method to pause the background music when the activity is paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
        // Stop background music when the activity is paused
        AppAudioService.pauseBackgroundMusic();
    }

    /**
     * Override the onResume method to resume the background music when the activity is resumed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        // Resume background music when the activity is resumed
        AppAudioService.resumeBackgroundMusic();
    }

    /**
     * Method to set the title of the custom action bar.
     *
     * @param name The name of the title.
     */
    public static void setTitleName(String name) {
        toolbarMainText.setText(name);
    }

    /**
     * Method to set up the custom action bar.
     */
    private void setUpActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
            actionBar.setCustomView(R.layout.toolbar_title_layout);
        }
    }

    /**
     * Set up the floating action button to navigate to the "Home" section.
     */
    private void setUpFab() {
        // Find the FloatingActionButton
        FloatingActionButton fab = findViewById(R.id.fab);

        // Set OnClickListener to navigate to the "Home" section when clicked
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the "Home" destination
                navController.navigate(R.id.nav_home);
                AppAudioService.playSound(getApplicationContext(), R.raw.home_button ,false);
            }
        });
    }

    /**
     * Set up the navigation view with custom animations and transitions.
     */
    private void setUpNavigationView() {
        navView = findViewById(R.id.bottomNavigationView);
        navView.setOnItemSelectedListener(item -> {
            int selectedItem = navView.getSelectedItemId();
            int destinationId = item.getItemId();
            NavOptions navOptions;
            // Get the index of the selected item and the destination in the menu
            int selectedIndex = getMenuIndexById(selectedItem);
            int destinationIndex = getMenuIndexById(destinationId);
            // Check if the selected item is the same as the destination
            if (selectedItem == destinationId) {
                return false;
            }
            // Check the direction of navigation and apply animations accordingly
            if (destinationIndex > selectedIndex) {
                // Destination is to the right
                navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.trans_in)
                        .setExitAnim(R.anim.trans_out)
                        .setPopEnterAnim(R.anim.trans_backin)
                        .setPopExitAnim(R.anim.trans_backout)
                        .build();
            } else {
                // Destination is to the left or same position
                navOptions = new NavOptions.Builder()
                        .setEnterAnim(R.anim.trans_backin)
                        .setExitAnim(R.anim.trans_backout)
                        .setPopEnterAnim(R.anim.trans_in)
                        .setPopExitAnim(R.anim.trans_out)
                        .build();
            }
            AppAudioService.playSound(getApplicationContext(), R.raw.next_page, false);
            navController.navigate(destinationId, null,
                    Settings.getAnimationPref(getApplicationContext()) ? navOptions : null);
            return true;
        });
    }


    /**
     * Method to get the index of the menu item by its ID.
     *
     * @param itemId The ID of the menu item.
     * @return The index of the menu item in the navigation view.
     */
    private int getMenuIndexById(int itemId) {
        Menu menu = navView.getMenu(); // Get the menu from the navigation view
        for (int i = 0; i < menu.size(); i++) { // Iterate through the menu items
            if (menu.getItem(i).getItemId() == itemId) { // Check if the item ID matches
                return i; // Return the index of the item
            }
        }
        return -1; // Return -1 if no item was found
    }



}