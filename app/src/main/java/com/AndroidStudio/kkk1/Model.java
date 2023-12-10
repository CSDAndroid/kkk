package com.AndroidStudio.kkk1;

import android.graphics.Bitmap;

public class Model {

    private int id;
    private Bitmap image;
    private int color;
    private float strokeWidth;
    private int alpha;
    public Model(){
        this.id = id;
        this.image = image;
        this.color = color;
        this.strokeWidth = strokeWidth;
        this.alpha = alpha;
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap image) {
        this.image = image;
    }
    public int getColor(){
        return color;
    }
    public void setColor(int color){
        this.color = color;
    }
    public float getStrokeWidth(){
        return strokeWidth;
    }
    public void setStrokeWidth(float strokeWidth){
        this.strokeWidth = strokeWidth;
    }
    public int getAlpha(){
        return alpha;
    }
    public void setAlpha(int alpha){
        this.alpha = alpha;
    }
}