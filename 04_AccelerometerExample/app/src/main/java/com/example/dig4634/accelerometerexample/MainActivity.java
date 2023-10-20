package com.example.dig4634.accelerometerexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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


    Integer Player_Radius = 50;
    Paint red_fill;
    Paint white_stroke;
    Paint white_text;
    Bitmap planet;
    Bitmap sun;
    Bitmap line;
    Bitmap line2;


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
        line=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.line),200,200,false);
        line2=Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(),R.drawable.line2),200,200,false);




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

    String message="Go to the sun.";
    float acc_x=0;
    float acc_y=0;
    int planet_x_position=0;
    int planet_y_position=0;

    int sun_x_position=500;
    int sun_y_position=600;

    public void update(int width, int height){

        sun_y_position+=5;

        planet_x_position-=acc_x*2;
        planet_y_position+=acc_y*2;

        if(planet_x_position<50)planet_x_position=50;
        else if(planet_x_position>width-50)planet_x_position=width-50;

        if(planet_y_position<50)planet_y_position=50;
        else if(planet_y_position>height-50)planet_y_position=height-50;

        checkCollisionsHorizontal();
        checkCollisionsVertical();
    }

    public void checkCollisionsVertical(){

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
            }


        }

    }

    public void checkCollisionsHorizontal(){

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
            }


        }

    }

    public void draw(){

        if(holder==null)return;

        Canvas c=holder.lockCanvas();

        update(c.getWidth(),c.getHeight());

        c.drawColor(Color.rgb(210,210,255));

        Paint dotPaint = new Paint();
        dotPaint.setColor(Color.RED);
        dotPaint.setStyle(Paint.Style.FILL);
        c.drawCircle(planet_x_position, planet_y_position, Player_Radius, dotPaint);
        c.drawCircle(980,1680,5,dotPaint);
        drawLines(c);

        c.drawText(message, 20, c.getHeight()-20, white_text);

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
