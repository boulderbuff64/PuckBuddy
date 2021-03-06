package com.erickson.friendlytouch;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private Firebase mRef_x;
    private Firebase mRef_y;
    PaintSurface mPaintSurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        Log.d("onCreate()", "1");

        Toolbar toolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(toolbar);

        mPaintSurface = (PaintSurface) findViewById(R.id.paint_surface);
        mPaintSurface.setOnTouchListener(mPaintSurface);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mRef_x = new Firebase("https://crowdweather-414b6.firebaseio.com/x");
        mRef_y = new Firebase("https://crowdweather-414b6.firebaseio.com/y");
        Log.d("MainActivity", "Start");

        mRef_x.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPaintSurface.shared_x = (ArrayList<Double>) dataSnapshot.getValue();
                Log.d("firebase-change", String.valueOf(mPaintSurface.shared_x));
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                mPaintSurface.shared_x.set(0, 0.10d);
                mPaintSurface.shared_x.set(1, 0.20d);
                mPaintSurface.shared_x.set(2, 0.30d);
                mPaintSurface.shared_x.set(3, 0.40d);
                Log.d("firebase-cancel", String.valueOf(mPaintSurface.shared_x));
                Log.e("firebase-cancel", "", firebaseError.toException());
            }
        });
        mRef_y.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                mPaintSurface.shared_y = (ArrayList<Double>) dataSnapshot.getValue();
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                mPaintSurface.shared_y.set(0, 0d);
                mPaintSurface.shared_y.set(1, 0.10d);
                mPaintSurface.shared_y.set(2, 0.20d);
                mPaintSurface.shared_y.set(3, 0.30d);
            }
        });

    }

    protected void onResume() {
        super.onResume();
        mPaintSurface.resume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPaintSurface.pause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.player1) {
            PaintSurface.player = 0;
            return true;
        }
        if (id == R.id.player2) {
            PaintSurface.player = 1;
            return true;
        }
        if (id == R.id.player3) {
            PaintSurface.player = 2;
            return true;
        }
        if (id == R.id.player4) {
            PaintSurface.player = 3;
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
