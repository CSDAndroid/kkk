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
    private static final String TABLE_NAME = "model";
    private static final String ID = "id";
    private static final String COLUMN_IMAGE = "image";
    private static final String COLUMN_COLOR = "color";
    private static final String COLUMN_STROKE_WIDTH = "strokeWidth";
    private static final String COLUMN_ALPHA = "alpha";
    private final SQLiteDatabase db;
    //创建数据库和表
    public MyDbHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context,name, factory, 6);
        //super必须放第一列
        db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //  创建表结构
        String createTableQuery = "CREATE TABLE " + TABLE_NAME + "(" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_IMAGE + " BLOB, " +
                COLUMN_COLOR + " INTEGER, " +
                COLUMN_ALPHA + " INTEGER, " +
                COLUMN_STROKE_WIDTH + " REAL)";
        db.execSQL(createTableQuery);
    }

    //对model表的操作
    //添加数据
    public boolean insertCell(DrawView drawView, Float strokeWidth, int color, int alpha){
        Bitmap imageBitmap = convertDrawViewToBitmap(drawView);
        byte[] imageBytes = convertBitmapToByteArray(imageBitmap);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_IMAGE, imageBytes);
        contentValues.put(COLUMN_COLOR,color);
        contentValues.put(COLUMN_ALPHA,alpha);
        contentValues.put(COLUMN_STROKE_WIDTH,strokeWidth);
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
    public Boolean updateCell(String updateId,DrawView updateDrawView, Float strokeWidth, int color, int alpha){
        //将需要更新的内容存入contentValues
        ContentValues contentValues = new ContentValues();
        Bitmap imageBitmap = convertDrawViewToBitmap(updateDrawView);
        byte[] imageBytes = convertBitmapToByteArray(imageBitmap);
        contentValues.put(COLUMN_IMAGE, imageBytes);
        contentValues.put(COLUMN_COLOR,color);
        contentValues.put(COLUMN_ALPHA,alpha);
        contentValues.put(COLUMN_STROKE_WIDTH,strokeWidth);
        int i = db.update(TABLE_NAME,contentValues,"id = ?",new String[]{updateId});
        return i > 0;
    }

    //查询数据，查询表中所有内容，将查询的内容存储在集合中
    @SuppressLint("Range")
    public List<Model> query(){
        List<Model> list = new ArrayList<>();
        String[] columns = {ID, COLUMN_IMAGE, COLUMN_COLOR, COLUMN_ALPHA, COLUMN_STROKE_WIDTH};
        Cursor cursor = db.query(TABLE_NAME,columns,null,null,null,null,null,null);
        while (cursor.moveToNext()){
            Model model = new Model();
            model.setId(cursor.getInt(cursor.getColumnIndex(ID)));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndex(COLUMN_IMAGE));
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
            model.setImage(bitmap);
            model.setColor(cursor.getInt(cursor.getColumnIndex(COLUMN_COLOR)));
            model.setAlpha(cursor.getInt(cursor.getColumnIndex(COLUMN_ALPHA)));
            model.setStrokeWidth(cursor.getFloat(cursor.getColumnIndex(COLUMN_STROKE_WIDTH)));
            list.add(model);
        }
        cursor.close();
        return list;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

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
