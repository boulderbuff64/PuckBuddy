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

//import android.support.v4.view.GestureDetectorCompat;

public class MainActivity extends Activity implements GestureDetector.OnGestureListener, OnClickListener {

    private Firebase mRef;
    private Firebase mRefTemp;
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
        //mRef = new Firebase("http://androidqs-firebaseio-demo.com/condition");
        mRef = new Firebase("https://burning-inferno-8506.firebaseio.com/condition");
        mRefTemp = new Firebase("https://burning-inferno-8506.firebaseio.com/temp");
        //mRef = new Firebase("https://project-7467768797914907035.firebaseio.com/temp");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String newCondition = (String) dataSnapshot.getValue();
                //mTextCondition.setText(newCondition);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

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
