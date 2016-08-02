package com.erickson.friendlytouch;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.VelocityTracker;
import android.view.View;
import android.view.View.OnTouchListener;

import com.firebase.client.Firebase;

import java.util.ArrayList;

public class PaintSurface extends SurfaceView implements Runnable, OnTouchListener {

    public static int player = 0;
    public ArrayList<Double> shared_x = new ArrayList<Double>();
    public ArrayList<Double> shared_y = new ArrayList<Double>();
    private Firebase fbRef_x;
    private Firebase fbRef_y;


    SurfaceHolder mHolder; // Surface holder allows to control and monitor the surface
    Thread mThread; // A thread where the painting activities are taking place
    boolean mFlag = false; // A flag which controls the start and stop of the
    static Canvas canvas; // create the canvas to edit

    Paint playerPaint1;
    Paint playerPaint2;
    Paint playerPaint3;
    Paint playerPaint4;

    float Xbegin; // X coord of 1st touch point
    float Ybegin; // Y coord of 1st touch point
    double mX; // X coord while touching
    double mY; // Y coord while touching
    float Xend; // X coord of last touch point
    float Yend; // Y coord of last touch point
    static int ScreenX = 800;
    static int ScreenY = 1200;

    float maxV = 15; // Speed Limit
    float dragV = 5; // Puck Feel drag above this spped
    int VeloScalar = 30;

    float Vxp = 1;
    float Vyp = 1;
    float Rp; // radius of puck
    float Rh; // radius of paddle
    float lastVxh = 0;
    float lastVyh = 0;
    static float Vxh = 0;
    static float Vyh = 0;
    float lastVxo = 0;
    float lastVyo = 0;
    static float Pi = (float) 3.141593;
    int first = 1;

    private Firebase mRef;
    private Firebase mRefTemp;


    Puck[] pucks = new Puck[5];

    static int[] touchCount = new int[12];
    static int fingerCount = 0;

    int glow = 250;

    private VelocityTracker mVelocityTracker = null;

    public PaintSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("PaintSurface", "Start");

        mHolder = getHolder(); // Getting the holder
        mX = 0.5; // Initializing the X position
        mY = 0.5; // Initializing the Y position

        // Initializing the paint objects
        playerPaint1 = new Paint();
        playerPaint1.setColor(0xFF33B5E5); // blue
        playerPaint2 = new Paint();
        playerPaint2.setColor(0xFFFF0000); // red
        playerPaint3 = new Paint();
        playerPaint3.setColor(0xFF00FF00); // green
        playerPaint4 = new Paint();
        playerPaint4.setColor(0xFFAA33AA); // Purple

    }

    public void resume() {
        mThread = new Thread(this);
        mFlag = true;
        mThread.start();
        Log.d("PaintSurface", "resume");
    }

    public void pause() {
        first = 1;
        mFlag = false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Xbegin = event.getX();
                Ybegin = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                mX = event.getX()/ScreenX;
                mY = event.getY()/ScreenY;
                //Log.d("motion","X="+mX+" Y="+mY);
                if (player==0) {
                    fbRef_x = new Firebase("https://crowdweather-414b6.firebaseio.com/x/0");
                    fbRef_y = new Firebase("https://crowdweather-414b6.firebaseio.com/y/0");
                    fbRef_x.setValue(mX);
                    fbRef_y.setValue(mY);
                }
                if (player==1) {
                    fbRef_x = new Firebase("https://crowdweather-414b6.firebaseio.com/x/1");
                    fbRef_y = new Firebase("https://crowdweather-414b6.firebaseio.com/y/1");
                    fbRef_x.setValue(mX);
                    fbRef_y.setValue(mY);
                }
                if (player==2) {
                    fbRef_x = new Firebase("https://crowdweather-414b6.firebaseio.com/x/2");
                    fbRef_y = new Firebase("https://crowdweather-414b6.firebaseio.com/y/2");
                    fbRef_x.setValue(mX);
                    fbRef_y.setValue(mY);
                }
                if (player==3) {
                    fbRef_x = new Firebase("https://crowdweather-414b6.firebaseio.com/x/3");
                    fbRef_y = new Firebase("https://crowdweather-414b6.firebaseio.com/y/3");
                    fbRef_x.setValue(mX);
                    fbRef_y.setValue(mY);
                }
                break;
            case MotionEvent.ACTION_UP:
                Xend = event.getX();
                Yend = event.getY();
                break;
        }
        return true;
    }

    @Override
    public void run() {
        Log.d("PaintSurface", "run()");
        while (mFlag) {
            // Check whether the object holds a valid surface
            if (!mHolder.getSurface().isValid())
                continue;
            Canvas canvas = mHolder.lockCanvas(); // Start editing the surface


            if (first == 1) {
                ScreenX = canvas.getWidth();
                ScreenY = canvas.getHeight();
                for (int i = 0; i < pucks.length; i++) {
                    pucks[i] = new Puck();
                    pucks[i].start(ScreenX, ScreenY, (int) (Math.random() * ScreenX / 20 + 2));
                    pucks[i].setColor((127 * i + 254) % 255, (86 * i + 250) % 255, (192 * i) % 255);
                    pucks[i].Vx = (float) (ScreenX / 250 * (2 * Math.random() - 1));
                    pucks[i].Vy = (float) (ScreenY / 240 * (2 * Math.random() - 1));
                    Log.d("First", "Pucks initiate");
                }
                for (int i = shared_x.size(); i <= 4; i++) {
                    shared_x.add(1.0/(i+2));
                    shared_y.add(1.0/(i+2));
                }
                pucks[pucks.length - 1].rad = 30;
                pucks[0].rad = 40;

                first = 0;
            }

            canvas.drawARGB(255 - glow, 0, 0, 0); // Background
            canvas.drawCircle((int) (mX*ScreenX), (int) (mY*ScreenY), 50, playerPaint1); // player paddle

            //for (int i = 0; i < shared_x.size(); i++) {
                canvas.drawCircle(shared_x.get(0).floatValue()*ScreenX, shared_y.get(0).floatValue()*ScreenY, 50, playerPaint1);
                canvas.drawCircle(shared_x.get(1).floatValue()*ScreenX, shared_y.get(1).floatValue()*ScreenY, 50, playerPaint2);
                canvas.drawCircle(shared_x.get(2).floatValue()*ScreenX, shared_y.get(2).floatValue()*ScreenY, 50, playerPaint3);
                canvas.drawCircle(shared_x.get(3).floatValue()*ScreenX, shared_y.get(3).floatValue()*ScreenY, 50, playerPaint4);
            //}

            for (int i = 0; i < pucks.length; i++) {
                pucks[i].addSpeed();
                pucks[i].stuckInCorner();
                pucks[i].edgeBounce(true);
                pucks[i].checkPaddleBounce((float)mX*ScreenX, (float)mY*ScreenY, Vxh, Vyh, (int) Rh); //Had issues
                //pucks[i].speedLimit(maxV, dragV); //Had issues
                canvas.drawCircle((int) pucks[i].x, (int) pucks[i].y, pucks[i].rad, pucks[i].Paint);
            }

            mHolder.unlockCanvasAndPost(canvas); // Finish editing the canvas and show to the user
        }
    }

    public static float[] bounce(float Xb, float Yb, float Xs, float Ys) {
        // System.out.println(" Xb=" + Xb + " Yb=" + Yb + " Xs=" + Xs + " Ys=" + Ys);
        float Norm, In;
        if (Xs > 0) {
            Norm = (float) (Math.atan(Ys / (Xs + 0.01)) + Pi / 2);
        } else {
            Norm = (float) (Math.atan(Ys / (Xs - 0.01)) + Pi * 3 / 2);
        }
        if (Xb > 0) {
            In = (float) Math.atan(Yb / (Xb + 0.01));
        } else {
            In = (float) (Math.atan(Yb / (Xb - 0.01)) + Pi);
        }
        float Bounce = Norm * 2 - In;

        float[] New = {0, 0};
        New[0] = (float) (Math.cos(Bounce) * scalar(Xb, Yb));
        New[1] = (float) (Math.sin(Bounce) * scalar(Xb, Yb));
        // System.out.println(" New Vx=" + New[0] + " New Vy=" + New[1] + " surf=" + Math.atan(Ys / Xs) + " Norm=" + Norm + " In=" + In + " B=" +
        // Bounce);
        return New;
    }



    public static float scalar(float X, float Y) {
        return (float) Math.sqrt(X * X + Y * Y);
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }
}
