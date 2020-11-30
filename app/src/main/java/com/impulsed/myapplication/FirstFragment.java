package com.impulsed.myapplication;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.net.Inet4Address;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FirstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FirstFragment extends Fragment{
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //argument for display page number
    private static final String ARG_POSITION = "position";
    private String position;
    public FirstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position Parameter 1.
     * @return A new instance of fragment FirstFragment.
     */
    public static FirstFragment newInstance(String position) {
        FirstFragment fragment = new FirstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_POSITION, position);
        fragment.setArguments(args);
        Log.d(Debudding.TAG, "New instance of Fragment created on position " + position);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            position = getArguments().getString(ARG_POSITION);
        }
        Log.d(Debudding.TAG, "Fragment onCreate was called");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_first, container, false);
        TextView tv = v.findViewById(R.id.textNumber);
        tv.setText(position);
        ImageButton bMinus = v.findViewById(R.id.buttonMinus);

        //visibility of minus button
        if (Integer.parseInt(position)>1)
            bMinus.setVisibility(View.VISIBLE);
        else
            bMinus.setVisibility(View.INVISIBLE);
        Log.d(Debudding.TAG, "OnCreateView");
        return v;
    }

}