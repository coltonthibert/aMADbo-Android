package com.example.amadbo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.example.amadbo.AppAudioService;
import com.example.amadbo.R;
import com.example.amadbo.adapters.AppGuideAdapter;

/**
 * AppGuideVPHostFragment hosts the ViewPager2 for the app guide.
 * It uses a PageTransformer to animate the transition between pages.
 */
public class AppGuideVPHostFragment extends Fragment {

    /**
     * onCreateView method for the AppGuideVPHostFragment.
     * Initializes the ViewPager2 and sets the adapter for the ViewPager2.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_guide_v_p_host, container, false);

        // Set up ViewPager2
        ViewPager2 viewPager2 = view.findViewById(R.id.appGuideViewPager);

        // Set adapter for ViewPager2
        viewPager2.setAdapter(new AppGuideAdapter(getActivity()));

        viewPager2.setPageTransformer(new DepthPageTransformer());

        // Return the view
        return view;
    }

    /**
     * DepthPageTransformer class for the ViewPager2.
     * This class animates the transition between pages in the ViewPager2.
     */
    public class DepthPageTransformer implements ViewPager2.PageTransformer {
        private static final float MIN_SCALE = 0.75f;

        public void transformPage(View view, float position) {
            int pageWidth = view.getWidth();

            if (position < -1) { // [-Infinity,-1)
                // This page is way off-screen to the left.
                view.setAlpha(0f);

            } else if (position <= 0) { // [-1,0]
                // Use the default slide transition when moving to the left page
                view.setAlpha(1f);
                view.setTranslationX(0f);
                view.setScaleX(1f);
                view.setScaleY(1f);

            } else if (position <= 1) { // (0,1]
                // Fade the page out.
                view.setAlpha(1 - position);



                // Counteract the default slide transition
                view.setTranslationX(pageWidth * -position);

                // Scale the page down (between MIN_SCALE and 1)
                float scaleFactor = MIN_SCALE
                        + (1 - MIN_SCALE) * (1 - Math.abs(position));
                view.setScaleX(scaleFactor);
                view.setScaleY(scaleFactor);

            } else { // (1,+Infinity]

                // This page is way off-screen to the right.
                view.setAlpha(0f);
                Animation topBarIn = AnimationUtils.loadAnimation(getContext(), R.anim.guide_popin_topbar);
                ImageView topbar = view.findViewById(R.id.topBar_Guide);
                topbar.startAnimation(topBarIn);
            }
        }
    }
}