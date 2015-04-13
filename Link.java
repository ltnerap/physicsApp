package com.example.leah.physicsapp;
import android.graphics.Color;
import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by Leah on 3/26/2015.
 */
public class Link {
    int ball1;
    int ball2;
    float length;
    boolean isRope;

    public Link(int b1, int b2, boolean isRpe){
        ball1 = b1;
        ball2 = b2;
        isRope = isRpe;

        Vector v = InitActivity.balls[ball1].position.sub(InitActivity.balls[ball2].position);
        length = v.length();
    }

    public void draw(Canvas g){
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        g.drawLine(InitActivity.balls[ball1].position.x,
                InitActivity.balls[ball1].position.y,
                InitActivity.balls[ball2].position.x,
                InitActivity.balls[ball2].position.y, paint);
    }
}
