package com.erickson.crowdweather;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Handler;
import android.support.v4.view.VelocityTrackerCompat;
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

    public ArrayList<Long> shared_x = new ArrayList<Long>();
    public ArrayList<Long> shared_y = new ArrayList<Long>();

    SurfaceHolder mHolder; // Surface holder allows to control and monitor the surface
    Thread mThread; // A thread where the painting activities are taking place
    boolean mFlag = false; // A flag which controls the start and stop of the
    static Canvas canvas; // create the canvas to edit

    Paint playerPaint;

    float Xbegin; // X coord of 1st touch point
    float Ybegin; // Y coord of 1st touch point
    float mX; // X coord while touching
    float mY; // Y coord while touching
    float Xend; // X coord of last touch point
    float Yend; // Y coord of last touch point
    static int ScreenX = 800;
    static int ScreenY = 1200;

    float maxV = 15; // Speed Limit
    float dragV = 5; // Puck Feel drag above this spped
    int VeloScalar = 30;

    float Vxp = 1;
    float Vyp = 1;
    float Xh = ScreenX / 2;
    float Yh = (float) (ScreenY * 0.9);
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


    Puck[] pucks = new Puck[3];

    static int[] touchCount = new int[12];
    static int fingerCount = 0;

    int glow = 50;

    float[] x = new float[20];
    float[] y = new float[20];

    private VelocityTracker mVelocityTracker = null;

    public PaintSurface(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d("PaintSurface", "Start");

        mHolder = getHolder(); // Getting the holder
        mX = 1; // Initializing the X position
        mY = 1; // Initializing the Y position

        // Initializing the paint objects
        playerPaint = new Paint();

        // Setting the color for the paint object
        // Best Colors- Blue=0099CC/33B5E5, Purple=9933CC/AA6633, Green = 669900/99CC00,Orange=FF8800/FFBB33, Red=CC0000/FF4444
        playerPaint.setColor(0xFF33B5E5); // blue
    }

    public void resume() {
        mThread = new Thread(this); // Initializing the thread
        mFlag = true; // setting the mFlag to true for start repainting
        mThread.start(); // Start repaint the SurfaceView
        Log.d("PaintSurface", "resume");

    }

    public void pause() {
        mFlag = false;
    }

    // Counter for long press
    final Handler _handler = new Handler();
    Runnable _longPressed = new Runnable() {
        public void run() {
            // moveSun = true;
        }
    };

    //public boolean onTouch(View v, MotionEvent event) {

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Xbegin = event.getX();
                mX = event.getX();
                Ybegin = event.getY();
                mY = event.getY();
                Xh = mX;
                Yh = mY;
                x[0] = mX;
                y[0] = mY;
                fingerCount = 1;
                //touchCount[0]++;

                if (mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                // Add a user's movement to the tracker.
                mVelocityTracker.addMovement(event);
                if (mVelocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    mVelocityTracker = VelocityTracker.obtain();
                } else {
                    // Reset the velocity tracker back to its initial state.
                    mVelocityTracker.clear();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                _handler.removeCallbacks(_longPressed);
                mX = event.getX();
                mY = event.getY();
                Xh = mX;
                Yh = mY;
                fingerCount = event.getPointerCount();
                for (int i = 0; i < fingerCount; i++) {
                    //Log.d("onTouch()", String.valueOf(i));
                    int mActivePointerId = event.getPointerId(i); // Get the pointer ID
                    //Log.d("onTouch()", "mActivePointerId " + String.valueOf(mActivePointerId));
                    int pointerIndex = event.findPointerIndex(mActivePointerId); // Use the pointer ID to find the index of the active pointer and fetch its position
                    //Log.d("onTouch()", "pointerIndex " + String.valueOf(pointerIndex));
                    // Get the pointer's current position
                    x[i] = event.getX(pointerIndex);
                    y[i] = event.getY(pointerIndex);
                    //Log.d("onTouch()", "pointerIndex " + String.valueOf(pointerIndex) + "  x " + String.valueOf(x[i]));
                    //touchCount[i]++;
                }
                touchCount[fingerCount]++; //track touching

                mVelocityTracker.addMovement(event);
                mVelocityTracker.computeCurrentVelocity(1000);
                Vxh = VelocityTrackerCompat.getXVelocity(mVelocityTracker, 0);
                Vyh = VelocityTrackerCompat.getYVelocity(mVelocityTracker, 0);

                break;
            case MotionEvent.ACTION_UP:
                _handler.removeCallbacks(_longPressed);
                Xend = event.getX();
                Yend = event.getY();
                Xh = Xend;
                Yh = Yend;
                Vxh = 0;
                Vyh = 0;
                fingerCount = 0;
                break;
        }
        return true;
    }

    @Override
    public void run() {
        Log.d("PaintSurface", "run()");
        while (mFlag) {
            // Check whether the object holds a valid surface

            if (first == 1) {
                for (int i = 0; i < pucks.length; i++) {
                    pucks[i] = new Puck();
                    pucks[i].start(ScreenX, ScreenY, (int) (Math.random() * ScreenX / 50 + 2));
                    pucks[i].setColor((127 * i + 254) % 255, (86 * i + 250) % 255, (192 * i) % 255);
                    pucks[i].Vx = (float) (ScreenX / 250 * (2 * Math.random() - 1));
                    pucks[i].Vy = (float) (ScreenY / 240 * (2 * Math.random() - 1));
                    Log.d("First", "Pucks initiate");
                }
                for (int i=shared_x.size(); i<=4; i++){
                    shared_x.add(0l);
                    shared_y.add(0l);
                }
                pucks[pucks.length - 1].rad = 20;
                pucks[0].rad = 20;


                first = 0;
            }

            if (!mHolder.getSurface().isValid())
                continue;

            Canvas canvas = mHolder.lockCanvas(); // Start editing the surface
            canvas.drawARGB(255 - glow, 0, 0, 0); // Background
            canvas.drawCircle((int) Xh, (int) Yh, 100, playerPaint); // player paddle


            for (int i = 0; i<shared_x.size(); i++) {
                canvas.drawCircle(shared_x.get(i), shared_y.get(i), 50, playerPaint);
            }
            //shared_x.set(0, (long) Xh);
            //shared_y.set(0, (long) Yh);


            for (int i = 0; i < pucks.length; i++) {
                pucks[i].addSpeed();
                pucks[i].stuckInCorner();
                pucks[i].edgeBounce(true);
                pucks[i].checkPaddleBounce(Xh, Yh, Vxh, Vyh, (int) Rh); //Had issues
                pucks[i].speedLimit(maxV, dragV); //Had issues
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
