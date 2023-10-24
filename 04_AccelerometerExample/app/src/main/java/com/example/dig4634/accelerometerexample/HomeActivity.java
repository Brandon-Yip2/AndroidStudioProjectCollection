package com.example.dig4634.accelerometerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }


    public void onPlayPressed(View view){
        Intent playIntent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(playIntent);

    }

    public void onSettingsPressed(View view){
        Intent settingsIntent = new Intent(getBaseContext(), SettingsActivity.class);
        startActivity(settingsIntent);

    }

    public void ontempPressed(View view){
        Intent winnerIntent = new Intent(getBaseContext(),WinnerActivity.class);
        startActivity(winnerIntent);

    }
}