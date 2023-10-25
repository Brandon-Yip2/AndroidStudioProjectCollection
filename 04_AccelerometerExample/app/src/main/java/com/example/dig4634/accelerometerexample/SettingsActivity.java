package com.example.dig4634.accelerometerexample;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dig4634.accelerometerexample.AudioService;

public class SettingsActivity extends AppCompatActivity {
    private SharedPreferences sharedPreferences;
    private Switch audioSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        audioSwitch = findViewById(R.id.switch3); // Updated ID

        // Initialize the Switch state based on the stored audio state
        boolean isAudioOn = sharedPreferences.getBoolean("audioOn", true);
        audioSwitch.setChecked(isAudioOn);

        audioSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("audioOn", isChecked);
            editor.apply();

            if (isChecked) {
                startService(new Intent(SettingsActivity.this, AudioService.class));
            } else {
                stopService(new Intent(SettingsActivity.this, AudioService.class));
            }
        });
    }

    public void onBack(View view){
        finish();
    }
}
