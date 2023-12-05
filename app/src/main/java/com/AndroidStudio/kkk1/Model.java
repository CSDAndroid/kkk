package com.AndroidStudio.kkk1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Model {

    private String id;
    private byte[] myImage;
    private String text;
    public Bitmap bitmap;

    public String getId() {
        return id;
    }

    public void setId(String id) {

        this.id = id;
    }

    public byte[] getImage() {
        return myImage;
    }

    public Bitmap setImage(byte[] image) {
        bitmap = BitmapFactory.decodeByteArray(image,0, image.length);
        return bitmap;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMyImage(byte[] myImage) {
        this.myImage = myImage;
    }
}
