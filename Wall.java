package com.example.leah.physicsapp;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;

/**
 * Created by Leah on 3/26/2015.
 */
public class Wall {
    float x1;
    float x2;
    float y1;
    float y2;

    public Wall(float p1, float p2, float p3, float p4){
        if(p1 > p3){
            x1 = p3;
            y1 = p4;
            x2 = p1;
            y2 = p2;
        }
        else{
            //set beginning x and y of wall
            x1 = p1;
            y1 = p2;
            //set ending x and y of wall
            x2 = p3;
            y2 = p4;
        }
    }

    public void draw(Canvas g){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        g.drawLine(x1, y1, x2, y2, paint);
    }
}
