package com.AndroidStudio.kkk1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class MyDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "model.db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "model";
    private static final String ID = "id";
    private static final String COLUMN_IMAGE = "image";
    private final SQLiteDatabase db;
    //创建数据库和表
    public MyDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name, factory, 2);
        //super必须放第一列
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  创建表结构
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + COLUMN_IMAGE + " BLOB)";
        db.execSQL(createTableQuery);
    }

    //对model表的操作
    //添加数据
    public boolean insertCell(DrawView drawView){
        Bitmap imageBitmap = convertDrawViewToBitmap(drawView);
        byte[] imageBytes = convertBitmapToByteArray(imageBitmap);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IMAGE, imageBytes);

        long i = db.insert(TABLE_NAME,null,contentValues);
        db.close();
        return i > 0;
    }
    //删除数据,由id进行删除
    public Boolean deleteCell(String deleteId){
        int i = db.delete(TABLE_NAME,"id = ?",new String[]{deleteId});
        return i > 0;
    }

    //更新数据
    public Boolean updateCell(String updateId,DrawView updateDrawView){
        //将需要更新的内容存入contentValues
        ContentValues contentValues = new ContentValues();
        Bitmap imageBitmap = convertDrawViewToBitmap(updateDrawView);
        byte[] imageBytes = convertBitmapToByteArray(imageBitmap);
        contentValues.put(COLUMN_IMAGE, imageBytes);
        int i = db.update(TABLE_NAME,contentValues,"id = ?",new String[]{updateId});
        return i > 0;
    }

    //查询数据，查询表中所有内容，将查询的内容存储在集合中
    @SuppressLint("Range")
    public List<Model> query(){
        List<Model> list = new ArrayList<>();
        Cursor cursor = db.query(TABLE_NAME,null,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            Model model = new Model();
            model.setId(Integer.valueOf(cursor.getInt(cursor.getColumnIndex(ID))));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
            model.setImage(bitmap);
            list.add(model);
        }
        cursor.close();
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 判断旧版本和新版本，执行相应的升级逻辑
        if (oldVersion < 2) {
            // 如果旧版本小于2，执行添加 "image" 列的操作
            db.execSQL("ALTER TABLE model ADD COLUMN image BLOB");
        }
    }

    private Bitmap convertDrawViewToBitmap(DrawView drawView){
        Bitmap bitmap = Bitmap.createBitmap(drawView.getWidth(),drawView.getHeight(),Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawView.draw(canvas);
        return bitmap;
    }

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}
