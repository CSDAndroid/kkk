package com.AndroidStudio.kkk1;

import android.media.Image;

public class Model {

    private String id;
    private Image myImage;
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public Image getImage() {
        return myImage;
    }

    public void setImage(Image image) {
        this.myImage = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
