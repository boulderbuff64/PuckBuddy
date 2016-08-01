package com.erickson.crowdweather;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

@SuppressLint("ClickableViewAccessibility") public class Draw extends View implements OnTouchListener {
    Paint paint = new Paint();
    float mX;
    float mY;
    float Xend;
    float Yend;


    public Draw(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        mX = 100;
        mY = 100;
        paint.setColor(Color.BLACK); //set paint color
        paint.setStrokeWidth(3); // set line width

        int height = canvas.getHeight(); // get height of canvas on wich you want to draw
        int width = canvas.getWidth(); // get width of canvas on wich you want to draw

        int cx = width / 2; // center x (width)
        int cy = height / 2; // center y (height)

        canvas.drawCircle(cx, cy, 10, paint);
        canvas.drawCircle(mX, mY, 20, paint);

    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mX = event.getX();
                mY = event.getY();
                v.performClick();
                break;
            case MotionEvent.ACTION_MOVE:
                mX = event.getX();
                mY = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                Xend = event.getX();
                Yend = event.getY();
                break;
        }
        return true;
    }
}

