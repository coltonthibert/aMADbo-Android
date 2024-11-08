package com.example.amadbo.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.amadbo.R;
import com.example.amadbo.fragments.AppGuideFragment;

/**
 * Adapter for the ViewPager2 in the AppGuideFragment
 * Hosts the AppGuideFragment for each page in the ViewPager2
 * Provides the title, description, and image for each page
 */
public class AppGuideAdapter extends FragmentStateAdapter {
    public AppGuideAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return AppGuideFragment.newInstance("Welcome to aMADbo!", "aMADbo is the ultimate app for amiibo collectors. With aMADbo, you can keep track of your amiibo collection, view detailed information about each amiibo, and search for new amiibo.", R.drawable.guide_frame1);
            case 1:
                return AppGuideFragment.newInstance("Search for amiibo", "Use the search feature to find amiibo by name, amiibo series, or game series. You can also filter your search results by figures, cards, or other.", R.drawable.guide_frame2);
            case 2:
                return AppGuideFragment.newInstance("View amiibo details", "Tap on an amiibo to view detailed information, such as release date, series, and character. Tap the \"+\" icon to add the amiibo to your collection.", R.drawable.guide_frame3);
            case 3:
                return AppGuideFragment.newInstance("View usages", "You can view all usages for any Nintendo Switch or 3DS games that an amiibo is compatible with.", R.drawable.guide_frame4);
            case 4:
                return AppGuideFragment.newInstance("Manage your collection", "Keep track of your amiibo collection under the \"My Amiibo\" category. You can remove an amiibo from your collection by long pressing it.", R.drawable.guide_frame5);
            case 5:
                return AppGuideFragment.newInstance("Settings", "Customize your aMADbo experience by adjusting accessibility features, search layout, and more in the settings menu.", R.drawable.guide_frame6);
            default:
                return AppGuideFragment.newInstance("Coming soon!", "More features are coming soon to aMADbo. Stay tuned for updates!", R.drawable.guide_frame1);
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}
