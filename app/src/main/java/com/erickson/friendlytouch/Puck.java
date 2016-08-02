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
    float theta = 0;
    float theta2 = (float) 1.57;
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
        theta = (float) (Math.random() * 2 * Pi);
        Paint.setStrokeWidth(5);
    }

    public void setColor(int R, int G, int B) {
        Paint.setColor(Color.rgb(R, G, B));
    }

    public void setRandomColor() {
        Paint.setColor(Color.rgb((int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255)));
    }

    public void setRandomVelo(float d) {
        Vx = (float) (d * (2 * Math.random() - 1));
        Vy = (float) (d * (2 * Math.random() - 1));
    }

    public void setRandomLocation() {
        x = (float) Math.random() * width;
        y = (float) Math.random() * height;
    }

    public void setRainbowColor(float A, float B) {
        setColor((int) (Math.sin(-1.5 + Pi * (A * x / width + B * y / height)) * 128 + 128),
                (int) (Math.sin(0.6 + Pi * (A * x / width + B * y / height)) * 128 + 128),
                (int) (Math.sin(2.7 + Pi * (A * x / width + B * y / height)) * 128 + 128));
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

    public boolean near(float X, float Y, int RAD) {
        boolean B = false;
        double dist = Math.sqrt((X - x) * (X - x) + (Y - y) * (Y - y));
        if (dist < (rad + RAD)) {
            B = true;
        }
        return B;
    }

    public void checkPaddleBounce(float Xh, float Yh, float Vxh, float Vyh, int Radius) {
        float dist = scalar((Xh - x), (Yh - y));
        if (dist < Radius + rad & Radius > 0) {
            // print(" Vx=",Vxp," Vy=",Vyp);
            float[] temp = bounce(Vx, Vy, (Xh - x), (Yh - y));
            Vx = temp[0] + Vxh / VeloScalar;
            Vy = temp[1] + Vyh / VeloScalar;
            //Log.d("Vh= ", Vxh / VeloScalar + "  " + Vyh / VeloScalar);
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
        // System.out.println(" New Vx=" + New[0] + " New Vy=" + New[1] + " surf=" + Math.atan(Ys / Xs) + " Norm=" + Norm + " In=" + In + " B=" +
        // Bounce);
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

    public void Gravity(float X, float Y, float Power) {
        //Log.d("Puck", "Gravity");
        float Dist;
        float accel;
        Dist = (float) (Math.sqrt((x - X) * (x - X) + (y - Y) * (y - Y)) + 0.001) / 2;
        if (Dist > width / 100) {
            accel = 1 / Dist / Dist; //accelerate to finger
        } else {
            accel = 0; // if close, don't accelerate
        }

        Vx += Power * accel * Math.cos(Math.atan((y - Y) / (x - X + 0.001))) * -(x - X) / Math.abs((x - X + 0.001));
        Vy += Power * accel * Math.sin(Math.atan((y - Y) / (x - X + 0.001))) * -(x - X) / Math.abs((x - X + 0.001));
    }

    public void Sink(float Y, float Power) {
        //Log.d("Puck", "Sink");
        if (y < Y) {
            Vy += Power / 100;
        } else {
            Vy -= Power / 100;
        }
    }

    public void Ellipse(float Fx, float Fy, float Gx, float Gy, float power, double dT) {
        //Log.d("Puck", "Ellipse");
        theta = (float) ((theta + dT) % (2 * Pi));
        float shiftX, shiftY;
        //if (Fx<Gx){ shiftX=Fx; } else {shiftX=Gx;}
        //if (Fy<Gy){ shiftY=Fy; } else {shiftY=Gy;}
        shiftX = (Fx + Gx) / 2;
        shiftY = (Fy + Gy) / 2;
        float X = (float) (Math.abs((Fx - Gx)) / 2 * Math.cos(theta)) + shiftX;
        float Y = (float) (Math.abs((Fy - Gy)) / 2 * Math.sin(theta)) + shiftY;

        Follow(X, Y, power);
    }

    public void ForceMove(float A, float B, float X, float Y, float Power) {
        //Log.d("Puck", "ForceMove");

        float Dist1, Dist2;
        float accel1, accel2;
        Dist1 = (float) (Math.sqrt((x - X) * (x - X) + (y - Y) * (y - Y)) + 1);
        Dist2 = (float) (Math.sqrt((x - A) * (x - A) + (y - B) * (y - B)) + 1);
        accel1 = 1 / Dist1;
        accel2 = 1 / Dist2;

        if (Dist1 > Math.sqrt(Power)) {
            accel1 = accel1 / Dist1; //Squared
        }
        if (Dist2 > Math.sqrt(Power)) {
            accel2 = accel2 / Dist2; //Squared
        }

        Vx += Power * accel2 * Math.cos(Math.atan((y - Y) / (x - X + 0.001))) * -(x - X) / Math.abs((x - X + 0.001));
        Vy += Power * accel2 * Math.sin(Math.atan((y - Y) / (x - X + 0.001))) * -(x - X) / Math.abs((x - X + 0.001));
        Vx += Power * accel1 * Math.cos(Math.atan((y - B) / (x - A + 0.001))) * -(x - A) / Math.abs((x - A + 0.001));
        Vy += Power * accel1 * Math.sin(Math.atan((y - B) / (x - A + 0.001))) * -(x - A) / Math.abs((x - A + 0.001));
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

    public void Follow(float X, float Y, float Power) {
        //Log.d("Puck", "Follow");

        double thta = 0;
        float dist = scalar(X - x, Y - y);
        float dX = X - x;
        float dY = Y - y;

        if (dist >= Power) {
            //Vx = (float) (Power * Math.sin((X - x) / dist));
            //Vy = (float) ((Y - y) / Math.abs(Y - y) * Power * Math.cos((Y - y) / dist));

            thta = Math.atan(dX / dY);
            Vx = (float) (sign(dY) * Power * Math.sin(thta) + Vx) / 2;
            Vy = (float) (sign(dY) * Power * Math.cos(thta) + Vy) / 2;
        } else {
            Vx = dX;
            Vy = dY;
        }

    }

    private float sign(float A) {
        return (float) (A / Math.abs(A));
    }

}