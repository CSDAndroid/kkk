package com.AndroidStudio.kkk1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.Image;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class MyDbHelper extends SQLiteOpenHelper {
    private static  final String DATABASE_NAME = "imageDb";
    private  static final int DATABASE_VERSION = 1;
    private SQLiteDatabase db;
    //创建数据库和表
    public MyDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        //super必须放第一列
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table model(id integer primary key autoincrement,image blob,text text)");
    }
    // 存储图像到数据库
    public long saveImage(byte[] imageBytes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("image", imageBytes);
        long rowId = db.insert("images", null, values);
        db.close();
        return rowId;
    }

    // 从数据库读取图像
    @SuppressLint("Range")
    public byte[] loadImage(long imageId) {
        SQLiteDatabase db = getReadableDatabase();
        String[] columns = {"image"};
        String selection = "id=?";
        String[] selectionArgs = {String.valueOf(imageId)};
        Cursor cursor = db.query("images", columns, selection, selectionArgs, null, null, null);
        byte[] imageBytes = null;
        if (cursor != null && cursor.moveToFirst()) {
            imageBytes = cursor.getBlob(cursor.getColumnIndex("image"));
            cursor.close();
        }
        db.close();
        return imageBytes;
    }
    //对model表的操作
    //添加数据
    public boolean insertCell(@NonNull Image image, Context text){

        ContentValues contentValues = new ContentValues();
        contentValues.put("image", image.toString());
        contentValues.put("text", String.valueOf(text));
        long i = db.insert("model",null,contentValues);
        return i > 0;
    }
    //删除数据,根据id进行删除
    public Boolean deleteCell(String deleteId){
        int i = db.delete("modelInfo","id = ?",new String[]{deleteId});
        return i > 0;
    }

    //更新数据
    public Boolean updateCell(String updateId,Image updateImage,String updateContent){
        //将需要更新的内容存入contentValues
        ContentValues contentValues = new ContentValues();
        contentValues.put("image", updateImage.getTimestamp());
        contentValues.put("content",updateContent);
        int i = db.update("modelInfo",contentValues,"id = ?",new String[]{updateId});
        return i > 0;
    }

    //查询数据，查询表中所有内容，将查询的内容存储在集合中
    public List<Model> query(){
        List<Model> list = new ArrayList<>();
        Cursor cursor = db.query("modelInfo",null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            Model model = new Model();
            model.setId(String.valueOf(cursor.getInt(0)));
            //*****************model.setImage(cursor.getBlob());
            model.setText(cursor.getString(2));
            list.add(model);
        }
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
