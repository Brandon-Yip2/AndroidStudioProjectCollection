package com.example.dig4634.accelerometerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //MediaPlayer mediaPLayer = MediaPlayer.create(this,R.raw.piano);
        //mediaPLayer.start();

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
    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(mediaPlayer != null){
            mediaPlayer.release();
        }
    }
}