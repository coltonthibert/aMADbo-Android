package com.example.amadbo.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.amadbo.R;

/**
 * AppGuideFragment displays a guide for aspects of the app.
 * It contains a title, body, and image.
 * Use the {@link AppGuideFragment#newInstance} factory method to create an instance of this fragment.
 */
public class AppGuideFragment extends Fragment {

    // The fragment initialization parameters
    private static final String ARG_GUIDE_TITLE = "guideTitle";
    private static final String ARG_GUIDE_BODY = "guideBody";
    public static final String ARG_GUIDE_IMAGE = "guideImage";

    // The guide title, body, and image
    private String guideTitle;
    private String guideBody;
    private int guideImage;

    public AppGuideFragment() {
        // Required empty public constructor
    }

    /**
     * Create a new instance of the app guide fragment.
     *
     * @param guideTitle The title of the guide.
     * @param guideBody The body of the guide.
     * @param guideImage The image of the guide.
     * @return A new instance of fragment AppGuideFragment.
     */
    public static AppGuideFragment newInstance(String guideTitle, String guideBody, int guideImage) {
        AppGuideFragment fragment = new AppGuideFragment();
        Bundle args = new Bundle();
        args.putString(ARG_GUIDE_TITLE, guideTitle);
        args.putString(ARG_GUIDE_BODY, guideBody);
        args.putInt(ARG_GUIDE_IMAGE, guideImage);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * onCreate method for the AppGuideFragment.
     * Initializes the guide title, body, and image.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            guideTitle = getArguments().getString(ARG_GUIDE_TITLE);
            guideBody = getArguments().getString(ARG_GUIDE_BODY);
            guideImage = getArguments().getInt(ARG_GUIDE_IMAGE);
        }
    }

    /**
     * onCreateView method for the AppGuideFragment.
     * Inflates the layout for the fragment and initializes the guide title, body, and image.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_app_guide, container, false);

        // Initialize the guide title, body, and image
        TextView guideTitleTextView = view.findViewById(R.id.guideTitle);
        TextView guideBodyTextView = view.findViewById(R.id.guideBody);
        ImageView guideImageView = view.findViewById(R.id.guideScreenshot);

        // Play Animations for certain Items
        ImageView topBarBG = view.findViewById(R.id.topBar_Guide);


        // Set the guide title, body, and image if they are not null
        if (guideTitle != null) {
            guideTitleTextView.setText(guideTitle);
        }
        if (guideBody != null) {
            guideBodyTextView.setText(guideBody);
        }
        if (guideImage != 0) {
            guideImageView.setImageResource(guideImage);
        }

        // Return the view
        return view;
    }
}