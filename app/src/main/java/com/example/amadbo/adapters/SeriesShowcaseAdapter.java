package com.example.amadbo.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amadbo.AppAudioService;
import com.example.amadbo.R;
import com.example.amadbo.Settings;
import com.example.amadbo.models.Amiibo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter class for managing series of Amiibo items in a RecyclerView.
 */
public class SeriesShowcaseAdapter extends RecyclerView.Adapter<SeriesShowcaseAdapter.CustomViewHolder> {
    private ArrayList<Amiibo> amiibos; // List of Amiibo items
    private Context context; // Context for accessing resources and preferences

    /**
     * Constructor for SeriesShowcaseAdapter.
     * @param amiibos List of Amiibo items to display.
     * @param context Context of the application.
     */
    public SeriesShowcaseAdapter(ArrayList<Amiibo> amiibos, Context context) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.series_showcase_item, parent, false);
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
        holder.amiibo_name.setText(amiibo.getName());
        Picasso.get()
                .load(amiibo.getImage())
                .placeholder(R.drawable.ic_amiibo_placeholder)
                .error(R.drawable.ic_amiibo_error)
                .into(holder.amiibo_icon);
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
        protected TextView amiibo_name;
        protected ImageView amiibo_icon;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.amiibo_name = itemView.findViewById(R.id.amiibo_name_showcase);
            this.amiibo_icon = itemView.findViewById(R.id.amiibo_icon_showcase);
            itemView.setOnClickListener(this);
        }

        /**
         * Handles click events on item views.
         * @param view The view that was clicked.
         */
        // Show details on selected Amiibo //
        @Override
        public void onClick(View view) {
            // Get the clicked amiibo
            int position = getAdapterPosition();
            AppAudioService.playSound(context, R.raw.next_screen, false);
            if (position != RecyclerView.NO_POSITION) {
                Amiibo amiibo = amiibos.get(position);

                // Navigate to detail fragment with Amiibo data
                Bundle extra = new Bundle();
                extra.putParcelable("amiibo", amiibo);


                if (!Settings.getAnimationPref(context)) { // check if animation is enabled
                    Navigation.findNavController(view).navigate(R.id.action_nav_seriesshow_to_nav_detail_no_anim, extra);
                }else {
                    Navigation.findNavController(view).navigate(R.id.action_nav_seriesshow_to_nav_detail, extra);
                }

            }
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

}
