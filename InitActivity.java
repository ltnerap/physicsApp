package com.example.leah.physicsapp;

import com.example.leah.physicsapp.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector;

public class InitActivity extends Activity implements Runnable {

    public static final int WIDTH = 500;
    public static final int HEIGHT = 500;
    public static boolean isPaused = false;
    public static final double DT = 0.5;
    public static final int MAXBALLS = 1000;
    public static final double Restitution = 0.9;
    public static final double WallRestitution = 0.6;

    public static final double GRAVITY = 0;
    public static int Bound = 50;
    public static int Radius = 20;
    public static double FRICTION = 40;

    public static int BallCount = 0;
    public static int WallCount = 0;
    public static int LinkCount = 0;

    public static Ball[] balls = new Ball[MAXBALLS];
    public static Wall[] walls = new Wall[MAXBALLS];
    public static Link[] links = new Link[MAXBALLS];

    public MyView appView;

    public void run() {

        while(true){

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            if(isPaused){
                continue;
            }
            //move balls
            for(int i=0; i<BallCount; i++){
                balls[i].move();
                appView.postInvalidate();
            }
            //apply gravity
            for(int i=0; i<BallCount; i++){
                balls[i].velocity.y += (GRAVITY * DT);
            }
            for(int i=0; i<BallCount; i++){
                for(int j=i+1; j<BallCount; j++){
                    if((balls[j].position.x - balls[i].position.x) *
                            (balls[j].position.x - balls[i].position.x)
                            + (balls[j].position.y - balls[i].position.y)
                            * (balls[j].position.y - balls[i].position.y)
                            <= (balls[j].radius + balls[i].radius) *
                            (balls[j].radius + balls[i].radius)){
                        handleCollision(balls[j], balls[i]);
                    }
                }
            }
            for(int i=0; i<BallCount; i++){
                for(int j=0; j<WallCount; j++){
                    handleWallCollision(balls[i], walls[j]);
                }
                //bottom wall collision
                if(balls[i].position.y + balls[i].radius >= HEIGHT){
                    balls[i].position.y = (HEIGHT) - balls[i].radius;
                    if(balls[i].velocity.y > 0){
                        balls[i].velocity.y = (float)-WallRestitution * balls[i].velocity.y;
                    }
                }
                //right wall collision
                if(balls[i].position.x + balls[i].radius >= WIDTH - 20){
                    balls[i].position.x = (WIDTH - 20) - balls[i].radius;
                    if(balls[i].velocity.x > 0){
                        balls[i].velocity.x = (float) -WallRestitution * balls[i].velocity.x;
                    }
                }
                //left wall collision
                if(balls[i].position.x - balls[i].radius <= 0){
                    balls[i].position.x = balls[i].radius;
                    if(balls[i].velocity.x < 0){
                        balls[i].velocity.x = (float) -WallRestitution * balls[i].velocity.x;
                    }
                }
                //top wall collision
                if(balls[i].position.y - balls[i].radius <= 0){
                    balls[i].position.y = balls[i].radius;
                    if(balls[i].velocity.y < 0){
                        balls[i].velocity.y = (float) -WallRestitution * balls[i].velocity.y;
                    }
                }
            }
            for(int i=0; i<LinkCount; i++){
                handleLink(i);
            }
        }
    }

    public static void handleCollision(Ball ball1, Ball ball2){
        //collision vector is difference of positions
        Vector collision = ball1.position.sub(ball2.position);
        float distance = collision.length();
        //balls overlap, move them apart
        Vector adjust = collision.mul((ball1.radius + ball2.radius - distance) / distance);

        float im1 = 1/ball1.mass;
        float im2 = 1/ball2.mass;

        //adjust positions according to mass
        Vector posAdjust1 = adjust.mul(im1/(im1+im2));
        Vector posAdjust2 = adjust.mul(im2/(im1+im2));
        //make new positions
        ball1.position = ball1.position.add(posAdjust1);
        ball2.position = ball2.position.sub(posAdjust2);

        //get collision angle
        collision = collision.normalize();
        //velocity difference vector
        Vector vdiff = ball1.velocity.sub(ball2.velocity);
        //change is based on how much velocity lies along collision angle
        float vchange = vdiff.dot(collision);
        //adjust for mass and restitution
        float vchange1 = (float)(1 + Restitution) * vchange * im1/(im1 + im2);
        float vchange2 = (float)(1 + Restitution) * vchange * im2/(im1 + im2);
        Vector velchange1 = collision.mul(vchange1);
        Vector velchange2 = collision.mul(vchange2);

        //make new velocities
        ball1.velocity = ball1.velocity.sub(velchange1);
        ball2.velocity = ball2.velocity.add(velchange2);
    }

    public static void handleWallCollision(Ball ball, Wall wall){
        float A = wall.y1 - wall.y2;
        float B = wall.x2 - wall.x1;
        float C = (wall.x1 * wall.y2) - (wall.x2 * wall.y1);
        float dist = (A * ball.position.x + B * ball.position.y + C) /
                (float)(Math.sqrt((double)(A * A + B * B)));

        if(Math.abs(dist) >= ball.radius){
            return;
        }

        float xi = (B * (B * ball.position.x - A * ball.position.y) - A * C) / (A * A + B * B);

        if(xi < wall.x1 || xi > wall.x2){
            return;
        }

        float distAdjust = ball.radius - Math.abs(dist);

        //normal vector for wall
        Vector wallNormal = new Vector(wall.y2 - wall.y1, wall.x1 - wall.x2);
        wallNormal = wallNormal.normalize();
        Vector adjust = wallNormal.mul(distAdjust);
        Vector newposition;
        if(dist < 0){
            newposition = ball.position.add(adjust);
        }
        else{
            newposition = ball.position.sub(adjust);
        }

        //change based on how much velocity lies on collision angle
        float vchange = ball.velocity.dot(wallNormal) * (float)(1 + WallRestitution);

        Vector velchange1 = wallNormal.mul(vchange);
        Vector newvelocity = ball.velocity.sub(velchange1);

        Vector wallvect = new Vector(wall.x2 - wall.x1, wall.y2 - wall.y1);
        Vector walln = wallvect.normalize();
        float vfriction = (float) (FRICTION * DT);
        float vforward = ball.velocity.dot(walln);

        Vector frictvect;
        if(vfriction >= Math.abs(vforward)){
            frictvect = walln.mul(vforward);
        }
        else{
            frictvect = walln.mul(vfriction);
        }

        Vector newvelocitywithfriction = newvelocity.sub(frictvect);

        ball.velocity = newvelocitywithfriction;
        ball.position = newposition;
    }

    public static void handleLink(int l){
        Ball ball1 = balls[links[l].ball1];
        Ball ball2 = balls[links[l].ball2];

        //collision vector is difference of positions
        Vector collision = ball1.position.sub(ball2.position);
        float distance = collision.length();

        if(links[l].isRope && links[l].length > distance){
            return;
        }

        //balls need to stay certain distance apart
        Vector adjust = collision.mul((links[l].length - distance) / distance);

        float im1 = 1/ball1.mass;
        float im2 = 1/ball2.mass;

        Vector adjust1 = adjust.mul(im1 / (im1 + im2));
        Vector adjust2 = adjust.mul(im2 / (im1 + im2));

        Vector newposition1 = ball1.position.add(adjust1);
        Vector newposition2 = ball2.position.sub(adjust2);

        Vector collision_n = collision.normalize();
        Vector vdiff = ball1.velocity.sub(ball2.velocity);

        float vchange = vdiff.dot(collision_n);
        float vchange1 = 2 * vchange * (im1 / (im1 + im2));
        float vchange2 = 2 * vchange * (im2 / (im1 + im2));

        Vector velchange1 = collision_n.mul(vchange1);
        Vector velchange2 = collision_n.mul(vchange2);
        Vector newvelocity1 = ball1.velocity.sub(velchange1);
        Vector newvelocity2 = ball2.velocity.add(velchange2);

        ball1.position = newposition1;
        ball2.position = newposition2;
        ball1.velocity = newvelocity1;
        ball2.velocity = newvelocity2;
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appView = new MyView(this);
        Ball firstBall = new Ball(50,50);
        balls[BallCount] = firstBall;
        BallCount ++;
        setContentView(appView);

        Thread physics = new Thread(this);
        physics.start();
    }
}
