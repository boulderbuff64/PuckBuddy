package com.erickson.friendlytouch;
/**
 * Created by ASErickson on 5/26/16.
 */

import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

public class Puck {
    float x;
    float y;
    float Vx;
    float Vy;
    int width;
    int height;
    int edge;
    int rad;
    int VeloScalar = 30;
    static float Pi = (float) 3.14159;
    android.graphics.Paint Paint = new Paint();

    public void start(int Width, int Height, int Radius) {
        Log.d("Puck", "start");
        width = Width;
        height = Height;
        edge = 0; //(int) (Width * 0.02);
        rad = Radius;
        x = (float) (Math.random() * Width);
        y = (float) (Math.random() * Height);
        Vx = 0;
        Vy = 0;
        Paint.setStrokeWidth(5);
    }

    public void setColor(int R, int G, int B) {
        Paint.setColor(Color.rgb(R, G, B));
    }

    public void addSpeed() {
        //Log.d("Puck", "addSpeed");
        x = x + Vx; //add X velocity
        y = y + Vy; //add Y velocity
    }

    public void edgeBounce(boolean bounce) {
        //Log.d("Puck", "edgeBounce");
        if (bounce) {
            if (x > width - edge - rad) {
                Vx = Vx * -1;
                x = width - edge - rad;
            }
            if (x < 0 + edge + rad) {
                Vx = Vx * -1;
                x = edge + rad;
            }
            if (y > height - edge - rad) {
                Vy = Vy * -1;
                y = height - edge - rad;
            }
            if (y < edge + rad) {
                Vy = Vy * -1;
                y = edge + rad;
            }
        } else {
            if (x >= width) {
                x = x - width;
            }
            if (x <= 0) {
                x = x + width;
            }
            if (y >= height) {
                y = y - height;
            }
            if (y <= 0) {
                y = y + height;
            }
        }
    }

    public void checkPaddleBounce(float Xh, float Yh, float Vxh, float Vyh, int Radius) {
        float dist = scalar((Xh - x), (Yh - y));
        if (dist < Radius + rad & Radius > 0) {
            // print(" Vx=",Vxp," Vy=",Vyp);
            float[] temp = bounce(Vx, Vy, (Xh - x), (Yh - y));
            Vx = temp[0] + Vxh / VeloScalar;
            Vy = temp[1] + Vyh / VeloScalar;
            Log.d("Vh= ", Vxh / VeloScalar + "  " + Vyh / VeloScalar);
            //canvas.drawLine(Xp, Yp, Xp + Vxp * 100, Yp + Vyp * 100, playerPaint); // line(Xp,Yp,Xp+Vxp*40,Yp+Vyp*40);

            x = x + Vx;
            y = y + Vy;
            dist = scalar((Xh - x), (Yh - y));
            while (dist < Radius + rad) {
                dist = scalar((Xh - x), (Yh - y));
                x = x + Vx;
                y = y + Vy;
            }
        }
    }

    private static float scalar(float A, float B) {
        return (float) Math.sqrt(A * A + B * B);
    }

    public static float[] bounce(float Xb, float Yb, float Xs, float Ys) {
        float Norm, In;
        float Pi = (float) 3.141529;
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
        return New;
    }

    public void speedLimit(float maxV, float dragV) {
        float VeloMag = scalar(Vx, Vy);
        if (VeloMag > maxV) {
            Vx = Vx / VeloMag * maxV;
            Vy = Vy / VeloMag * maxV;
        }
        if (VeloMag > dragV) {
            Vx = (float) (Vx * 0.996);
            Vy = (float) (Vy * 0.996);
            // System.out.println(" Vxp=" + Vxp + " Vyp=" + Vyp);
        }
    }

    public void stuckInCorner() {
        if (x < 5 & y < 5) {
            x = width / 2;
            y = height / 2;
        }
        if (x > width & y > height) {
            x = width / 2;
            y = height / 2;
        }
    }

    private float sign(float A) {
        return (float) (A / Math.abs(A));
    }

}