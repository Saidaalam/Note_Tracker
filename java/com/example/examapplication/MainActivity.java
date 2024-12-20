package com.example.examapplication;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int GALLERY_REQUEST_CODE = 200;
    private ImageView imageView;
    private Spinner moodSpinner;
    private EditText notesEditText;
    private Button saveButton, viewHistoryButton, shareButton, openCameraButton, openMapButton;
    private ArrayList<String> moodEntries;

    // Permission launcher for camera permission request
    private ActivityResultLauncher<String> requestCameraPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    openCamera();
                } else {
                    Toast.makeText(MainActivity.this, "Camera permission denied", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize views
        moodSpinner = findViewById(R.id.moodSpinner);
        notesEditText = findViewById(R.id.notesEditText);
        saveButton = findViewById(R.id.saveButton);
        viewHistoryButton = findViewById(R.id.viewHistoryButton);
        shareButton = findViewById(R.id.shareButton);
        openCameraButton = findViewById(R.id.captureButton);
        imageView = findViewById(R.id.imageView);

        moodEntries = new ArrayList<>();

        // Set up listeners for buttons
        saveButton.setOnClickListener(v -> saveMoodEntry());
        viewHistoryButton.setOnClickListener(v -> viewMoodHistory());
        shareButton.setOnClickListener(v -> shareMoodEntry());

        Intent intent = new Intent(MainActivity.this, SensorActivity.class);
        startActivity(intent);


        openCameraButton.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                // Request camera permission
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA);
            } else {
                openCamera(); // Permission already granted for older versions
            }
        });

        Button captureButton = findViewById(R.id.captureButton);
        Button galleryButton = findViewById(R.id.galleryButton);

        captureButton.setOnClickListener(view -> openCamera());
        galleryButton.setOnClickListener(view -> openGallery());
    }

    // Save mood entry to the list
    private void saveMoodEntry() {
        String mood = moodSpinner.getSelectedItem().toString();
        String notes = notesEditText.getText().toString().trim();
        if (notes.isEmpty()) {
            Toast.makeText(this, "Please enter some notes", Toast.LENGTH_SHORT).show();
        } else {
            String entry = "Mood: " + mood + ", Notes: " + notes;
            moodEntries.add(entry);
            Toast.makeText(this, "Mood saved", Toast.LENGTH_SHORT).show();
            Log.d("MoodEntry", entry);
            notesEditText.setText("");
        }
    }

    // Open the Mood History Activity
    private void viewMoodHistory() {
        Intent intent = new Intent(this, MoodHistoryActivity.class);
        intent.putStringArrayListExtra("moodEntries", moodEntries);
        startActivity(intent);
    }

    // Share mood entry using an intent
    private void shareMoodEntry() {
        String mood = moodSpinner.getSelectedItem().toString();
        String notes = notesEditText.getText().toString().trim();
        if (notes.isEmpty()) {
            Toast.makeText(this, "Please enter some notes", Toast.LENGTH_SHORT).show();
        } else {
            String shareText = "I'm feeling " + mood + ". Notes: " + notes;
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, "Share Mood"));
        }
    }

    // Open camera to capture an image
    private void openCamera() {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
        } else {
            Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
        }
    }

    // Open gallery to select an image
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
    }

    // Handle the result from camera or gallery
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_REQUEST_CODE) {
                // Get the captured image as a Bitmap
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                imageView.setImageBitmap(photo);
            } else if (requestCode == GALLERY_REQUEST_CODE) {
                // Get the selected image from gallery
                Uri imageUri = data.getData();
                try {
                    Bitmap selectedImage = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    imageView.setImageBitmap(selectedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
