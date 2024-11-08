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
 * RecyclerView Adapter for displaying Amiibo items in the search results.
 */
public class AmiiboSearchRVAdapter extends RecyclerView.Adapter<AmiiboSearchRVAdapter.CustomViewHolder> {

    private ArrayList<Amiibo> amiibos;
    private Context context;

    /**
     * Constructor for AmiiboSearchRVAdapter.
     *
     * @param amiibos List of Amiibo items to display.
     * @param context Context of the application.
     */
    public AmiiboSearchRVAdapter(ArrayList<Amiibo> amiibos, Context context) {
        this.amiibos = amiibos;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Use different layout depending on user preference
        int layoutIdForListItem = Settings.getSearchLayoutPref(context)
                ? R.layout.amiibo_cardview_list : R.layout.amiibo_cardview_list2;
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutIdForListItem, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Amiibo amiibo = amiibos.get(position);
        holder.amiibo_name.setText(amiibo.getName());
        holder.amiibo_series.setText(amiibo.getAmiiboSeries());
        // Use Picasso to load image from URL into ImageView
        Picasso.get()
                .load(amiibo.getImage())
                .placeholder(R.drawable.ic_amiibo_placeholder)
                .error(R.drawable.ic_amiibo_error)
                .into(holder.amiibo_image);
    }

    @Override
    public int getItemCount() {
        return amiibos != null ? amiibos.size() : 0;
    }

    /**
     * Custom ViewHolder class for holding views of each Amiibo item.
     */
    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView amiibo_name;
        protected TextView amiibo_series;
        protected ImageView amiibo_image;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.amiibo_name = itemView.findViewById(R.id.amiibo_name);
            this.amiibo_series = itemView.findViewById(R.id.amiibo_series);
            this.amiibo_image = itemView.findViewById(R.id.detail_image);

            // Set click listener
            itemView.setOnClickListener(this);
        }

        // Show details on selected Amiibo //
        @Override
        public void onClick(View view) {
            // Get the clicked amiibo
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Amiibo amiibo = amiibos.get(position);

                // Navigate to detail fragment with Amiibo data
                Bundle extra = new Bundle();
                extra.putParcelable("amiibo", amiibo);

                AppAudioService.playSound(context, R.raw.next_screen, false);
                Navigation.findNavController(view).navigate(R.id.action_nav_search_to_nav_detail, extra);
            }
        }
    }

    /**
     * Sets the Amiibo data for the adapter.
     * @param amiibos The list of Amiibo items to set.
     */
    public void setAmiibos(ArrayList<Amiibo> amiibos) {
        this.amiibos = amiibos;
        notifyDataSetChanged();
    }
}
