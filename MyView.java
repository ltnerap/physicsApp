package com.example.leah.physicsapp;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Canvas;
import android.graphics.Color;
import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector;
import android.support.v4.view.GestureDetectorCompat;

/**
 * Created by Leah on 3/26/2015.
 */
public class MyView extends View implements View.OnTouchListener, GestureDetector.OnGestureListener {
    int startX;
    int startY;
    int stopX;
    int stopY;
    int startBall;
    int stopBall;
    boolean startFound = false;
    boolean stopFound = false;
    Bitmap table;
    private GestureDetector detect;
    public float s;
    boolean drawing = false;
    int draw = 0;

    public MyView(Context context) {
        super(context);
        setOnTouchListener(this);
        detect = new GestureDetector(context, this);
        table = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher),1000,1000,false);
    }

    public void onShowPress(MotionEvent e){

    }

    public boolean onSingleTapUp(MotionEvent e){
        return false;
    }

    public boolean onDown(MotionEvent e){
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float start, float end){
        return false;
    }

    public void onLongPress(MotionEvent e){

        InitActivity.isPaused = !InitActivity.isPaused;
        draw = 0;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float start, float end){
        return false;
    }

    public boolean onTouch(View v, MotionEvent e) {
        detect.onTouchEvent(e);
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
                System.out.println("made it here");
                if(InitActivity.isPaused) {
                    startX = (int)(e.getX()/s);
                    startY = (int)(e.getY()/s);
                    for(int i=0; i<InitActivity.BallCount; i++){
                        if(InitActivity.balls[i].contains((float)startX, (float)startY)){
                            startFound = true;
                            startBall = i;
                            break;
                        }
                    }
                }
                else {
                    break;
                }
                break;
            case MotionEvent.ACTION_UP:
                if(!InitActivity.isPaused) {
                    InitActivity.balls[InitActivity.BallCount] = new Ball(e.getX()/s, e.getY()/s);
                    this.postInvalidate();
                    InitActivity.BallCount++;
                }

                else if(startFound && InitActivity.isPaused){
                    stopX = (int)(e.getX()/s);
                    stopY = (int)(e.getY()/s);
                    for(int i=0; i<InitActivity.BallCount; i++){
                        if(InitActivity.balls[i].contains((float)stopX, (float)stopY) && startBall != i){
                            stopFound = true;
                            stopBall = i;
                            break;
                        }
                    }
                }
                if(startFound && stopFound && InitActivity.isPaused){
                    InitActivity.links[InitActivity.LinkCount] = new Link(startBall, stopBall, false);
                    startFound = false;
                    stopFound = false;
                    InitActivity.LinkCount++;
                    this.postInvalidate();
                }
                else if(InitActivity.isPaused && draw != 0){
                    stopX = (int)(e.getX()/s);
                    stopY = (int)(e.getY()/s);
                    System.out.println("startx = " + startX + ", starty = " + startY + ", stopx = " + stopX + ", stopy = " + stopY);
                    if(startX != stopX || startY != stopY) {
                        System.out.println("here");
                        InitActivity.walls[InitActivity.WallCount] = new Wall(startX, startY, stopX, stopY);
                        InitActivity.WallCount++;
                        this.postInvalidate();
                    }
                }
                if(InitActivity.isPaused){
                    draw = 1;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(InitActivity.isPaused && !startFound && !stopFound){
                    //stopX = (int)(e.getX()/s);
                    //stopY = (int)(e.getY()/s);
                    //System.out.println("startx = " + startX + ", starty = " + startY + ", stopx = " + stopX + ", stopy = " + stopY);
                    /*InitActivity.walls[InitActivity.WallCount] = new Wall(startX, startY, stopX, stopY);
                    InitActivity.WallCount++;*/
                    this.postInvalidate();
                    drawing = true;
                    break;
                }
                break;
        }
        return true;
    }

    public void onDraw(Canvas g){
        float w = g.getWidth();
        float h = g.getHeight();
        if(w/InitActivity.WIDTH < h/InitActivity.HEIGHT){
            s = w/InitActivity.WIDTH;
        }
        else {
            s = h/InitActivity.HEIGHT;
        }
        g.scale(s, s);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        g.drawRect(0,0, InitActivity.WIDTH, InitActivity.HEIGHT, paint);
        for(int i=0; i<InitActivity.BallCount; i++){
            InitActivity.balls[i].draw(g);
        }
        for(int i=0; i<InitActivity.LinkCount; i++){
            InitActivity.links[i].draw(g);
        }
        for(int i=0; i<InitActivity.WallCount; i++){
            InitActivity.walls[i].draw(g);
        }
    }
}
