package com.example.amadbo.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amadbo.AppAudioService;
import com.example.amadbo.R;
import com.example.amadbo.Settings;
import com.example.amadbo.database.AmiiboDatabase;
import com.example.amadbo.models.Amiibo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Adapter for displaying Amiibo data in the My Amiibo Fragment.
 */
public class MyAmiiboAdapter extends RecyclerView.Adapter<MyAmiiboAdapter.CustomViewHolder> {
    private ArrayList<Amiibo> amiibos;
    private Context context;

    /**
     * Constructor for MyAmiiboAdapter.
     *
     * @param amiibos List of Amiibo items to display.
     * @param context Context of the application.
     */
    public MyAmiiboAdapter(ArrayList<Amiibo> amiibos, Context context) {
        this.amiibos = amiibos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAmiiboAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.amiibo_cardview_grid, parent, false);
        return new MyAmiiboAdapter.CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAmiiboAdapter.CustomViewHolder holder, int position) {
        Amiibo amiibo = amiibos.get(position);
        holder.amiibo_name.setText(amiibo.getName());
        holder.amiibo_series.setText(amiibo.getAmiiboSeries());
        // Use Picasso to load image from URL into ImageView
        Picasso.get()
                .load(amiibo.getImage())
                .placeholder(R.drawable.ic_amiibo_placeholder)
                .error(R.drawable.ic_amiibo_error)
                .into(holder.amiibo_image);

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                // Create an alert dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Remove Amiibo");
                builder.setIcon(R.drawable.ic_amiibo);
                builder.setMessage("Are you sure you want to remove the amiibo \"" + amiibo.getName() + "\" from your collection?");
                builder.setPositiveButton("Yes", (dialogInterface, i) -> {
                    // Delete the Amiibo from the database
                    AmiiboDatabase.get(context).setNotOwnedAmiibo(amiibo.getId());
                    // Remove the Amiibo from the list
                    amiibos.remove(amiibo);
                    // Notify the adapter that the data set has changed
                    notifyDataSetChanged();

                    // Play sound
                    AppAudioService.playSound(context, R.raw.alert, false);

                    Toast.makeText(context, amiibo.getName().toUpperCase()
                            + " removed from collection", Toast.LENGTH_SHORT).show();
                });
                builder.setNegativeButton("No", (dialogInterface, i) -> {
                    // Do nothing
                });
                builder.create().show();
                AppAudioService.playSound(context, R.raw.add, false);
                return true;
            }
        });

        // Apply bounce animation to Amiibo
        animateView(holder.amiibo_image);
    }

    @Override
    public int getItemCount() {
        return amiibos != null ? amiibos.size() : 0;
    }

    /**
     * ViewHolder class for holding views of individual Amiibo items.
     */
    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected CardView cardView;
        protected TextView amiibo_name;
        protected TextView amiibo_series;
        protected ImageView amiibo_image;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.gridView);
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

                // Play sound
                AppAudioService.playSound(context, R.raw.next_screen, false);

                // Check if animation is enabled
                if (!Settings.getAnimationPref(context)){
                    Navigation.findNavController(view).navigate(R.id.action_nav_myAmiibo_to_nav_detail_no_anim, extra);
                }else{
                    Navigation.findNavController(view).navigate(R.id.action_nav_myAmiibo_to_nav_detail, extra);
                }

            }
        }
    }

    /**
     * Method to animate a view if animation is enabled.
     *
     * @param view View to be animated.
     */
    private void animateView(View view) {
        // Check if animation is enabled
        if (!Settings.getAnimationPref(context)) return;
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.trans_myamiibo_in);
        view.startAnimation(animation);
    }
}
