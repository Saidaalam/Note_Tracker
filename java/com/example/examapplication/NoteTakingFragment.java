package com.example.examapplication;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.fragment.app.Fragment;

public class NoteTakingFragment extends Fragment {

    private EditText noteEditText;
    private Button saveNoteButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the fragment's layout
        View rootView = inflater.inflate(R.layout.activity_note_taking_fragment, container, false);

        // Initialize the EditText and Button from the inflated layout
        noteEditText = rootView.findViewById(R.id.noteEditText);
        saveNoteButton = rootView.findViewById(R.id.saveNoteButton);

        // Set click listener for the button
        saveNoteButton.setOnClickListener(view -> {
            String note = noteEditText.getText().toString().trim();

            if (TextUtils.isEmpty(note)) {
                Toast.makeText(getActivity(), "Please write something", Toast.LENGTH_SHORT).show();
            } else {
                // Save the note logic (e.g., storing in SharedPreferences or a database)
                Toast.makeText(getActivity(), "Note saved", Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
