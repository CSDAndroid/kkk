package com.AndroidStudio.kkk1;

public class Model {

    private String id;
    private byte[] myImage;
    private String text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    public byte[] getImage() {
        return myImage;
    }

    public void setImage(byte[] image) {
        this.myImage = image;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
