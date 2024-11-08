package com.example.amadbo.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.amadbo.AppAudioService;
import com.example.amadbo.R;
import com.example.amadbo.models.Usage;

import java.util.ArrayList;

/**
 * Adapter class for managing amiibo usage data in a RecyclerView.
 */
public class UsageAdapter extends RecyclerView.Adapter<UsageAdapter.CustomViewHolder> {
    private ArrayList<Usage> usages;
    private Context context;
    private int lastPosition = -1;

    /**
     * Constructor for the UsageAdapter.
     *
     * @param usages  The list of Usage objects to be displayed.
     * @param context The context in which the adapter is used.
     */
    public UsageAdapter(ArrayList<Usage> usages, Context context) {
        this.usages = usages;
        this.context = context;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.usage_item, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Usage usage = usages.get(position);
        holder.bind(usage);
        setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return usages != null ? usages.size() : 0;
    }

    class CustomViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        protected TextView usageTitle;
        protected TextView usageDetails;
        protected MotionLayout motionLayout;
        protected ImageView arrowButton;
        private boolean isExpanded = false;

        /**
         * Constructor for the CustomViewHolder.
         *
         * @param itemView The view for each item.
         */
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.usageTitle = itemView.findViewById(R.id.Usage_Title);
            this.usageDetails = itemView.findViewById(R.id.Usage_Details);
            this.motionLayout = itemView.findViewById(R.id.MotionLayout);
            this.arrowButton = itemView.findViewById(R.id.Button_Expand);
            itemView.setOnClickListener(this);
        }

        /**
         * Binds the data to the ViewHolder.
         *
         * @param usage The Usage object to bind.
         */
        public void bind(Usage usage) {
            usageTitle.setText(usage.getGameName());
            usageDetails.setText(usage.getUsage());
        }

        /**
         * Handles click events for expanding/collapsing the item.
         *
         * @param v The clicked view.
         */
        @Override
        public void onClick(View v) {
            if (!isExpanded) {
                motionLayout.transitionToEnd();
                arrowButton.setImageResource(R.drawable.ic_baseline_arrow_circle_up_24);
                AppAudioService.playSound(context, R.raw.usage_open, false);
            } else {
                motionLayout.transitionToStart();
                arrowButton.setImageResource(R.drawable.ic_baseline_arrow_drop_down_circle_24);
                AppAudioService.playSound(context, R.raw.usage_close, false);
            }
            isExpanded = !isExpanded;
        }
    }

    /**
     * Animates the items when they are scrolled into view.
     *
     * @param viewToAnimate The view to be animated.
     * @param position      The position of the item in the RecyclerView.
     */
    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    /**
     * Sets the list of usages and notifies the adapter of the data change.
     *
     * @param usages The list of Usage objects.
     */
    public void setUsages(ArrayList<Usage> usages) {
        this.usages = usages;
        notifyDataSetChanged();
    }
}
