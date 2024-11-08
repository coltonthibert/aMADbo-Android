package com.example.amadbo.adapters;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amadbo.AppAudioService;
import com.example.amadbo.R;
import com.example.amadbo.Settings;
import com.example.amadbo.models.Amiibo;

import java.util.ArrayList;

/**
 * Adapter class for managing series of Amiibo items in a RecyclerView.
 */
public class SeriesAdapter extends RecyclerView.Adapter<SeriesAdapter.CustomViewHolder> {
    private ArrayList<Amiibo> amiibos; // List of Amiibo items
    private Context context; // Context for accessing resources and preferences

    /**
     * Constructor for SeriesAdapter.
     * @param amiibos List of Amiibo items to display.
     * @param context Context of the application.
     */
    public SeriesAdapter(ArrayList<Amiibo> amiibos, Context context) {
        this.amiibos = amiibos;
        this.context = context;
    }

    /**
     * Creates a new ViewHolder instance when needed.
     * @param parent The parent ViewGroup.
     * @param viewType The view type of the new View.
     * @return A new CustomViewHolder instance.
     */
    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_view, parent, false);
        return new CustomViewHolder(view);
    }

    /**
     * Binds data to the views within the ViewHolder.
     * @param holder The ViewHolder to bind data to.
     * @param position The position of the item within the RecyclerView.
     */
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Amiibo amiibo = amiibos.get(position);
        holder.amiibo_series.setText(amiibo.getAmiiboSeries());
    }

    /**
     * Gets the total number of items in the dataset held by the adapter.
     * @return The total number of items.
     */
    @Override
    public int getItemCount() {
        return amiibos != null ? amiibos.size() : 0;
    }

    /**
     * ViewHolder class for managing individual item views.
     */
    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView amiibo_series;
        protected Animation zoomIn = AnimationUtils.loadAnimation(context, R.anim.view_scaleup);
        protected Animation fadeText = AnimationUtils.loadAnimation(context, R.anim.fade_textout);
        protected ConstraintLayout layout;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.amiibo_series = itemView.findViewById(R.id.amiibo_series);
            this.layout = itemView.findViewById(R.id.series_screen);
            itemView.setOnClickListener(this);


            // Apply bounce animation to Amiibo
            animateView(layout);
        }

        /**
         * Handles click events on item views.
         * @param v The view that was clicked.
         */
        public void onClick(View v) {
            itemView.setZ(100f);
            int delay = 0;
            // Check if animation is enabled in settings
            if (Settings.getAnimationPref(context)){
                itemView.startAnimation(zoomIn);
                amiibo_series.startAnimation(fadeText);
                AppAudioService.playSound(context, R.raw.series_view2, false);
                delay = 250;
            }
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String seriesName = amiibos.get(position).getAmiiboSeries();
                        Bundle extra = new Bundle();
                        extra.putString("series", seriesName);
                        Navigation.findNavController(v).navigate(R.id.action_nav_amiiboseries_to_nav_seriesshow, extra);
                    }
                }
            }, delay);
        }

    }

    /**
     * Sets the Amiibo data for the adapter.
     * @param amiibos The list of Amiibo items to set.
     */
    public void setSeries(ArrayList<Amiibo> amiibos) {
        this.amiibos = amiibos;
        notifyDataSetChanged();
    }

    /**
     * Method to animate a view if animation is enabled.
     *
     * @param view View to be animated.
     */
    private void animateView(View view) {
        // Check if animation is enabled
        if (!Settings.getAnimationPref(context)) return;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.view_anim);
        view.startAnimation(animation);
    }
}
