package com.example.amadbo.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amadbo.MainActivity;
import com.example.amadbo.R;
import com.example.amadbo.adapters.MyAmiiboAdapter;
import com.example.amadbo.database.AmiiboDatabase;
import com.example.amadbo.models.Amiibo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * Fragment to display the owned amiibo
 * Hosts a RecyclerView to display the owned amiibo
 * Has a button to copy the owned amiibo to the clipboard
 */
public class MyAmiiboFragment extends Fragment {
    ArrayList<Amiibo> ownedAmiibos;
    AmiiboDatabase amiiboDatabase;
    RecyclerView myAmiiboRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_amiibo, container, false);

        myAmiiboRecyclerView = view.findViewById(R.id.myAmiiboRecyclerView);

        // Get the owned amiibos from the database
        amiiboDatabase = new AmiiboDatabase(getActivity());
        ownedAmiibos = amiiboDatabase.getAllOwnedAmiibos();

        // Display the owned amiibos
        MyAmiiboAdapter adapter = new MyAmiiboAdapter(ownedAmiibos, getActivity());
        myAmiiboRecyclerView.setAdapter(adapter);
        myAmiiboRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), getSpanCount()));

        // Display the amount of owned amiibo in the activity title
        MainActivity.setTitleName("My Amiibo" + " (" + ownedAmiibos.size() + ")");

        // Get the copy to clipboard button
        FloatingActionButton copyToClipboard = view.findViewById(R.id.copyToClipboardButton);
        copyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyToClipboard(); // Copy the owned amiibo to the clipboard
            }
        });

        return view;
    }


    /**
     * onResume method to update the title of the activity
     */
    @Override
    public void onResume() {
        super.onResume();
        MainActivity.setTitleName("My Amiibo" + " (" + ownedAmiibos.size() + ")");
    }


    /**
     * Get the amount of columns to display in the RecyclerView
     * @return The amount of columns to display
     */
    private int getSpanCount() {
        if (getResources().getConfiguration().orientation == 2 // Landscape
                || getResources().getConfiguration().screenWidthDp >= 600) { // Tablet
            return 4;
        } else { // Portrait
            return 2;
        }
    }

    /**
     * Copy the owned amiibo to the clipboard
     */
    private void copyToClipboard() {
        StringBuilder amiiboList = new StringBuilder();
        amiiboList.append("Check out my amiibo collection!\n\n");

        for (Amiibo amiibo : ownedAmiibos) {
            amiiboList.append(amiibo.getName()).append(" (").append(amiibo.getAmiiboSeries()).append(")\n");
        }

        amiiboList.append("\nCopied from the aMADbo app for Android");

        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(AppCompatActivity.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("Amiibo List", amiiboList.toString());
        clipboard.setPrimaryClip(clip);

        if (clipboard.hasPrimaryClip()) {
            Toast.makeText(getActivity(), "Amiibo list copied to clipboard", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), "Failed to copy amiibo list to clipboard", Toast.LENGTH_SHORT).show();
        }
    }

}