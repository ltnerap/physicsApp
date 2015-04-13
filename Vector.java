package com.example.leah.physicsapp;

/**
 * Created by Leah on 3/26/2015.
 */
public class Vector {
    public float x;
    public float y;
    public Vector(float xx, float yy){
        x = xx;
        y = yy;
    }
    Vector add(Vector v){
        Vector vnew = new Vector(x, y);
        vnew.x += v.x;
        vnew.y += v.y;
        return vnew;
    }
    Vector sub(Vector v){
        Vector vnew = new Vector(x, y);
        vnew.x -= v.x;
        vnew.y -= v.y;
        return vnew;
    }
    Vector mul(float f){
        Vector vnew = new Vector(x, y);
        vnew.x *= f;
        vnew.y *= f;
        return vnew;
    }
    float dot(Vector v){
        return x * v.x + y * v.y;
    }
    Vector normalize(){
        Vector vnew = new Vector(x, y);
        vnew.x = (float) (x/Math.sqrt(x*x+y*y));
        vnew.y = (float)(y/Math.sqrt(x*x+y*y));
        return vnew;
    }
    float length(){
        return (float)Math.sqrt(x*x+y*y);
    }
    float lengthsquared(){
        return(x*x+y*y);
    }
    float angle(){
        return (float)Math.atan2((double)y, (double)x);
    }
}
