package com.erickson.crowdweather;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

//import android.support.v4.view.GestureDetectorCompat;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener, OnClickListener {

    private Firebase mRef;
    private Firebase mRef_x;
    private Firebase mRef_y;
    PaintSurface mPaintSurface;
    Draw drawView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        Log.d("onCreate()", "1");

        mPaintSurface = (PaintSurface) findViewById(R.id.paint_surface); // Getting reference to the PaintView R.layout.activity_main
        mPaintSurface.setOnTouchListener(mPaintSurface);
        drawView = new Draw(this);
        drawView.setBackgroundColor(Color.BLACK);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mRef_x = new Firebase("https://crowdweather-414b6.firebaseio.com/x");
        mRef_y = new Firebase("https://crowdweather-414b6.firebaseio.com/y");

        mRef_x.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPaintSurface.shared_x = (ArrayList<Long>) dataSnapshot.getValue();
                Log.d("firebase-change",String.valueOf(mPaintSurface.shared_x));
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                mPaintSurface.shared_x.set(0,0l);
                mPaintSurface.shared_x.set(1,0l);
                mPaintSurface.shared_x.set(2,0l);
                mPaintSurface.shared_x.set(3,0l);
                Log.d("firebase-cancel",String.valueOf(mPaintSurface.shared_x));
                Log.e("firebase-cancel","", firebaseError.toException());
            }
        });
        mRef_y.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPaintSurface.shared_y = (ArrayList<Long>) dataSnapshot.getValue();
            }
            @Override
            public void onCancelled(FirebaseError firebaseError) {
                mPaintSurface.shared_y.set(0,0l);
                mPaintSurface.shared_y.set(1,0l);
                mPaintSurface.shared_y.set(2,0l);
                mPaintSurface.shared_y.set(3,0l);
            }
        });

    }

    protected void onResume() {
        super.onResume();
        mPaintSurface.resume();
        super.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }

    @Override
    public void onClick(View v) {

    }
}
