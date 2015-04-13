package com.example.leah.physicsapp;
import java.util.Random;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by Leah on 3/26/2015.
 */
public class Ball {
    Vector position;
    Vector velocity;
    public float mass;
    public float radius;

    //generates random integer between max and min
    public static int randInt(int max, int min){
        Random newRand = new Random();
        return newRand.nextInt((max - min) + 1) + min;
    }

    //ball constructor
    public Ball(float x, float y){
        //create position vector
        position = new Vector(x, y);
        //mass is random between 1 and 10
        mass = randInt(10, 1);
        //speed is random between 1 and 5
        float speed = randInt(5, 1) + 1;
        //angle is random between 0 and 360
        float angle = (float) (randInt(360, 0) * 2 * 3.14 / 360);
        //create velocity vector
        velocity = new Vector((float)(speed * Math.cos(angle)), (float)(speed * Math.sin(angle)));
        //set radius
        radius = InitActivity.Radius;
    }

    //moves ball
    void move(){
        //add to position vector based on velocity and passage of time
        position.x += velocity.x*InitActivity.DT;
        position.y += velocity.y*InitActivity.DT;
    }

    //checks if ball contains point (x,y)
    boolean contains(float x, float y){
        return (position.x - x)*(position.x - x) + (position.y - y) * (position.y - y)
                <= radius * radius;
    }

    void draw(Canvas g){
        //System.out.println("draw called");
        Paint paint = new Paint();
        int R = (255 - (int)(25 * mass));
        int G = (255 - (int)(25 * mass));
        int B = (255 - (int)(25 * mass));
        paint.setARGB(255, R, G, B);
        //paint.setColor(Color.RED);
        //g.drawOval((int)position.x - InitActivity.Radius, (int)position.y - InitActivity.Radius, (int)position.x + InitActivity.Radius, (int)position.y + InitActivity.Radius, paint);
        g.drawCircle(position.x, position.y, InitActivity.Radius, paint);
    }
}
