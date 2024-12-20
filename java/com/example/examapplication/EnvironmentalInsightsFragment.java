package com.example.examapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

public class EnvironmentalInsightsFragment extends Fragment {

    private TextView lightTextView, motionTextView, temperatureTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment layout
        View rootView = inflater.inflate(R.layout.activity_environmental_insights_fragment, container, false);

        // Initialize the TextViews
        lightTextView = rootView.findViewById(R.id.lightTextView);
        motionTextView = rootView.findViewById(R.id.motionTextView);
        temperatureTextView = rootView.findViewById(R.id.temperatureTextView);

        // Example: Set text (you can replace this with actual sensor data)
        lightTextView.setText("Light level: 80%");
        motionTextView.setText("Motion detected: Yes");
        temperatureTextView.setText("Ambient temperature: 24°C");

        return rootView;
    }
}
