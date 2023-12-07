package com.AndroidStudio.kkk1;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

public class Model {

    private int id;
    private Bitmap image;
    public Model(){
        this.id = id;
        this.image = image;
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
    public Bitmap byteToBitmap(byte[] bytes){
        if (bytes != null && bytes.length > 0){
            return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        }else {
            return null;
        }
    }
    public byte[] getByteArray() {
        if (image != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            return stream.toByteArray();
        } else {
            return null;
        }
    }

}