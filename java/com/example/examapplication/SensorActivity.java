package com.example.examapplication;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class SensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor lightSensor, accelerometer, temperatureSensor;

    private TextView lightTextView, motionTextView, temperatureTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        // Initialize the TextViews
        lightTextView = findViewById(R.id.lightTextView);
        motionTextView = findViewById(R.id.motionTextView);
        temperatureTextView = findViewById(R.id.temperatureTextView);

        // Get the SensorManager system service
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        // Get sensors
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);

        // Register listeners to start receiving data
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
        }
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // Handle the data from sensors

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
            // Light sensor data
            float lightValue = event.values[0];
            lightTextView.setText("Light level: " + lightValue + " lux");
        }

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            // Accelerometer data (motion detection)
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            motionTextView.setText("Motion detected: x=" + x + ", y=" + y + ", z=" + z);
        }

        if (event.sensor.getType() == Sensor.TYPE_AMBIENT_TEMPERATURE) {
            // Temperature sensor data
            float temperature = event.values[0];
            temperatureTextView.setText("Ambient temperature: " + temperature + "°C");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle changes in sensor accuracy if needed
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister the sensor listener to save battery
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Re-register the sensors to start receiving data again
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_UI);
        }
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        }
        if (temperatureSensor != null) {
            sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_UI);
        }
    }
}
