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
    public int s;

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
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float start, float end){
        return false;
    }

    public boolean onTouch(View v, MotionEvent e) {
        detect.onTouchEvent(e);
        switch(e.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                if(!InitActivity.isPaused) {
                    InitActivity.balls[InitActivity.BallCount] = new Ball(e.getX()*(s/InitActivity.WIDTH), e.getY()/(s/InitActivity.HEIGHT));
                    this.postInvalidate();
                    InitActivity.BallCount++;
                }
                else if(InitActivity.isPaused){
                    startX = (int)e.getX()*(s/InitActivity.WIDTH);
                    startY = (int)e.getY()/(s/InitActivity.HEIGHT);
                    for(int i=0; i<InitActivity.BallCount; i++){
                        if(InitActivity.balls[i].contains((float)startX, (float)startY)){
                            startFound = true;
                            startBall = i;
                            break;
                        }
                    }
                }
                else if(startFound && InitActivity.isPaused){
                    stopX = (int)e.getX()*(s/InitActivity.WIDTH);
                    stopY = (int)e.getY()*(s/InitActivity.HEIGHT);
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
                    this.invalidate();
                }
                else if(InitActivity.isPaused && !startFound && !stopFound){
                    InitActivity.walls[InitActivity.WallCount] = new Wall(startX, startY, stopX, stopY);
                    InitActivity.WallCount++;
                    this.invalidate();
                }
        }
        return true;
    }

    public void onDraw(Canvas g){
        int w = g.getWidth();
        int h = g.getHeight();
        if(w < h){
            s = w;
        }
        else {
            s = h;
        }
        g.scale((float)s/InitActivity.WIDTH, (float)s/InitActivity.HEIGHT);
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        g.drawRect(0,0, (float)s, (float)s, paint);
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
