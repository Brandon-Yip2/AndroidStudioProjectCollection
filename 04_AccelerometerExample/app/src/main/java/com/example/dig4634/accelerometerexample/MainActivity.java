package com.example.dig4634.accelerometerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements SensorEventListener, SurfaceHolder.Callback {


    Integer Player_Radius = 25;
    Paint red_fill;
    Paint white_stroke;
    Paint white_text;
    Bitmap planet;
    Bitmap trophy;
    Bitmap sun;
    Bitmap line;
    Bitmap line2;
    Bitmap vision;

    Bitmap background;

    int previous_x = 50;
    int previous_y = 50;


    ArrayList<Integer> HorlineXs = new ArrayList<Integer>(Arrays.asList(10, 175, 505, 670, 835, 175, 505, 670, 340, 505, 670, 835, 175, 340, 505, 670, 10, 670, 835, 340, 505, 340, 505, 10, 175, 340, 670, 835));
    ArrayList<Integer> HorlineYs = new ArrayList<Integer>(Arrays.asList(10, 10, 10, 10, 10, 175, 175, 175, 340, 340, 340, 340, 505, 505, 505, 505, 670, 670, 670, 835, 835, 1165, 1165, 1330, 1330, 1330, 1330, 1330));

    ArrayList<Integer> VerlineXs = new ArrayList<Integer>(Arrays.asList(-75, -75, -75, -75, -75, -75, -75, -75, 90, 90, 90, 90, 255, 255, 255, 420, 420, 420, 420, 585, 585, 585,750, 750, 750, 915, 915, 915, 915, 915, 915, 915, 915));
    ArrayList<Integer> VerlineYs = new ArrayList<Integer>(Arrays.asList(90, 255,420, 585, 750, 915, 1080, 1245, 420, 750, 1080, 1245, 255, 585, 1080, 90, 750, 915, 1245, 585, 750, 1080, 915, 1080, 1245, 90, 255, 420, 585, 750, 915, 1080, 1245, 1410));
    SurfaceHolder holder=null;

    Animator my_animator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        red_fill=new Paint();
        red_fill.setColor(Color.RED);
        red_fill.setStyle(Paint.Style.FILL);

        white_stroke=new Paint();
        white_stroke.setColor(Color.WHITE);
        white_stroke.setStyle(Paint.Style.STROKE);
        white_stroke.setStrokeWidth(10);

        white_text=new Paint();
        white_text.setColor(Color.WHITE);
        white_text.setTextSize(100);

        planet=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.planet),200,200,false);
        line=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.whiteline1),200,200,false);
        line2=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.whiteline2),200,200,false);
        vision=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.vision2),3700,3700,false);
        background=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.blackback),3700,3700,false);
        trophy=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.trophy),100,100,false);



        SensorManager manager=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        Sensor accelerometer=manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if(accelerometer!=null){
            manager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_NORMAL,SensorManager.SENSOR_DELAY_UI);
        }

        SurfaceView my_surface=findViewById(R.id.surfaceView);
        my_surface.getHolder().addCallback(this);


        my_animator=new Animator(this);
        my_animator.start();

    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        acc_x=sensorEvent.values[0];
        acc_y=sensorEvent.values[1];
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }

    float acc_x=0;
    float acc_y=0;
    int planet_x_position=25;
    int planet_y_position=25;

    int trophy_x_position = 550;
    int trophy_y_position = 1450;

    int sun_x_position=500;
    int sun_y_position=600;

    public void update(int width, int height){

        sun_y_position+=5;
        previous_x = planet_x_position;
        previous_y = planet_y_position;

        planet_x_position-=acc_x*2;
        planet_y_position+=acc_y*2;

        if(planet_x_position<25)planet_x_position=25;
        else if(planet_x_position>width-25)planet_x_position=width-25;

        if(planet_y_position<25)planet_y_position=25;
        else if(planet_y_position>height-25)planet_y_position=height-25;


        if (checkCollisionsHorizontal() || checkCollisionsVertical()){
            planet_x_position = previous_x;
            planet_y_position = previous_y;
        }

        if (checkWon(trophy_x_position, trophy_y_position)){
            //Start win activity
            Intent winnerIntent = new Intent(getBaseContext(),WinnerActivity.class);
            startActivity(winnerIntent);
        }

    }

    public boolean checkWon(int wonX, int wonY) {
        int circleCenterX = planet_x_position;
        int circleCenterY = planet_y_position;

        // Define the threshold distance for considering it "close"
        int thresholdDistance = 50; // Adjust this value as needed

        // Calculate the distance between circleCenter and (wonX, wonY)
        double distance = Math.sqrt(
                Math.pow(circleCenterX - wonX, 2) + Math.pow(circleCenterY - wonY, 2)
        );

        // If the distance is less than the threshold, return true; otherwise, return false
        return distance < thresholdDistance;
    }

    public boolean checkCollisionsVertical(){

        for (int i = 0; i<VerlineXs.size(); i++){

            int circleCenterX = planet_x_position;
            int circleCenterY = planet_y_position;
            int lineStartY = VerlineYs.get(i);
            int lineLength = 180;
            int closestY;
            int closestX = VerlineXs.get(i) + 100;


            if (circleCenterY+Player_Radius < lineStartY) {
                //Line is above the vertical line
                closestY = lineStartY;
            } else if (circleCenterY-Player_Radius > lineStartY + lineLength) {
                closestY = lineStartY + lineLength;
            } else {
                closestY = circleCenterY;
            }


            float distance = (float) Math.sqrt((circleCenterX - closestX) * (circleCenterX - closestX) +
                    (circleCenterY - closestY) * (circleCenterY - closestY));


            if (distance <= Player_Radius) {
                Log.d("Example","COLLIDING Vertical " + i);
                return true;
            }


        }
        return false;

    }

    public boolean checkCollisionsHorizontal(){

        for (int i = 0; i<HorlineXs.size(); i++){

            int circleCenterX = planet_x_position;
            int circleCenterY = planet_y_position;
            int lineStartX = HorlineXs.get(i);
            int lineLength = 180;
            int closestX;
            int closestY = HorlineYs.get(i) + 100;


            if (circleCenterX+Player_Radius < lineStartX) {
                closestX = lineStartX;  // Circle is to the left of the line segment
            } else if (circleCenterX-Player_Radius > lineStartX + lineLength) {
                closestX = lineStartX + lineLength;  // Circle is to the right of the line segment
            } else {
                closestX = circleCenterX;  // Circle is above the line segment
            }


            float distance = (float) Math.sqrt((circleCenterX - closestX) * (circleCenterX - closestX) +
                    (circleCenterY - closestY) * (circleCenterY - closestY));


            if (distance <= Player_Radius) {
                Log.d("Example","COLLIDING horizontal " + i);
                return true;
            }


        }
        return false;

    }

    public void draw(){

        if(holder==null)return;

        Canvas c=holder.lockCanvas();

        update(c.getWidth(),c.getHeight());

        c.drawColor(Color.rgb(0,0,0));

        Paint dotPaint = new Paint();
        dotPaint.setColor(Color.RED);
        dotPaint.setStyle(Paint.Style.FILL);
        c.drawCircle(planet_x_position, planet_y_position, Player_Radius, dotPaint);
        drawLines(c);
        c.drawBitmap(vision, planet_x_position-1840, planet_y_position-1840, null);
        c.drawBitmap(trophy, trophy_x_position, trophy_y_position, null);

        holder.unlockCanvasAndPost(c);
    }

    public void drawLines(Canvas c){

        for (int i = 0; i<HorlineXs.size(); i++){
            c.drawBitmap(line, HorlineXs.get(i), HorlineYs.get(i), null);
        }

        for (int i = 0; i<VerlineXs.size(); i++){
            c.drawBitmap(line2, VerlineXs.get(i), VerlineYs.get(i), null);
        }
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        Log.d("Example","Surface is created");
        holder=surfaceHolder;

        draw();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        Log.d("Example","Surface changed");
        holder=surfaceHolder;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        holder=null;
    }

    @Override
    public void onDestroy(){

        my_animator.finish();
        SensorManager manager=(SensorManager) getSystemService(Context.SENSOR_SERVICE);
        manager.unregisterListener(this,manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));

        super.onDestroy();
    }
}
