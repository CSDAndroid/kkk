package com.AndroidStudio.kkk1;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.media.MediaScannerConnection;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Stack;

public class DrawView extends FrameLayout implements Serializable {
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mPaint;
    private Path mPath;
    private float mStrokeWidth;
    private int mPaintColor = Color.BLACK;
    private int mAlpha = 255;
    private final ArrayList<Path> paths;
    private final ArrayList<Float> strokeWidths;
    private final ArrayList<Integer> alphas;
    private final ArrayList<Integer> paintColors;
    private final Stack<Path> undonePaths;
    private final Stack<Float> undoneStrokeWidths;
    private final Stack<Integer> undoneAlphas;
    private final Stack<Integer> undonePaintColors;
    private boolean isEraser = false;

    public DrawView(Context context) {
        this(context,null);

    }

    public DrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        //  初始化画笔，画布设置
        //  初始化路径
        paths = new ArrayList<>();
        strokeWidths = new ArrayList<>();
        alphas = new ArrayList<>();
        paintColors = new ArrayList<>();
        undonePaths = new Stack<>();
        undoneStrokeWidths = new Stack<>();
        undoneAlphas = new Stack<>();
        undonePaintColors = new Stack<>();
        mPaint = new Paint();
        mPath = new Path();
        mPaint.setColor(mPaintColor);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setAlpha(mAlpha);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }


    @Override
    //  重写dnDraw
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBitmap, 0, 0, null);
        for (int i = 0;i < paths.size();i ++){
            Path path = paths.get(i);
            float strokeWidth = strokeWidths.get(i);
            int alpha = alphas.get(i);
            int paintColor = paintColors.get(i);
            mPaint.setColor(paintColor);
            mPaint.setStrokeWidth(strokeWidth);
            mPaint.setAlpha(alpha);
            canvas.drawPath(path,mPaint);
        }
        canvas.drawPath(mPath,mPaint);
    }


    //  清空按钮功能
    public void clearCanvas() {
        // 清空路径
        paths.clear();
        strokeWidths.clear();
        alphas.clear();
        paintColors.clear();
        undonePaths.clear();
        undoneStrokeWidths.clear();
        undoneAlphas.clear();
        undonePaintColors.clear();
        mPath.reset();

        // 触发重绘
        invalidate();
    }

    //  画笔监听路径
    @SuppressLint("ClickableViewAccessibility")
    @Override
    //  监听路径
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时，移动路径起点
                undonePaths.clear();
                undoneStrokeWidths.clear();
                undoneAlphas.clear();
                undonePaintColors.clear();
                mPath.reset();
                mPath.moveTo(x,y);
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动时，连接路径
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起时，添加路径
                paths.add(mPath);
                strokeWidths.add(mStrokeWidth);
                alphas.add(mAlpha);
                paintColors.add(mPaintColor);
                mPath = new Path();
                break;
        }

        // 触发重绘
        if (event.getAction() != MotionEvent.ACTION_UP && event.getAction() != MotionEvent.ACTION_POINTER_UP) {
            invalidate();
        }
        return true;
    }

    protected void onSizeChanged(int w, int h, int oldw,int oldh){
        super.onSizeChanged(w,h,oldw,oldh);
        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }
    public Bitmap savePathsToBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(mBitmap, 0, 0, null);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        for (int i = 0; i < paths.size(); i++) {
            Path path = paths.get(i);
            float strokeWidth = strokeWidths.get(i);
            int alpha = alphas.get(i);
            int paintColor = paintColors.get(i);

            paint.setColor(paintColor);
            paint.setStrokeWidth(strokeWidth);
            paint.setAlpha(alpha);
            canvas.drawPath(path, paint);
        }

        return bitmap;
    }
    //  设置橡皮擦模式
    public void setEraser(boolean isEraser){
        this.isEraser = isEraser;
        if (isEraser) {
            mPaintColor = Color.WHITE;
        }
    }


    public void setPathColor(int selectedColor){
        mPaintColor = selectedColor;
    }

    //   设置画笔粗细功能
    public void setPathStrokeWidth(float strokeWidth){
        mStrokeWidth = strokeWidth;
    }

    //    设置画笔不透明度功能
    public void setPathAlpha(int alpha){
        mAlpha = alpha;
    }

    //   撤回功能
    public void undo(){
        if (!paths.isEmpty()){
            undonePaths.push(paths.remove(paths.size()-1));
            undoneStrokeWidths.push(strokeWidths.remove(strokeWidths.size()-1));
            undoneAlphas.push(alphas.remove(alphas.size()-1));
            undonePaintColors.push(paintColors.remove(paintColors.size()-1));
            invalidate();
        }
    }

    //  返回功能
    public void redo(){
        if (!undonePaths.isEmpty()){
            paths.add(undonePaths.pop());
            strokeWidths.add(undoneStrokeWidths.pop());
            alphas.add(undoneAlphas.pop());
            paintColors.add(undonePaintColors.pop());
            invalidate();
        }
    }

    public Bitmap getBitmap() {
        Bitmap bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        draw(canvas);
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        mCanvas = new Canvas(mBitmap);
        invalidate();
    }
    public void saveImage() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "Drawing_" + timeStamp + ".png";

        try {
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString();
            File directory = new File(path);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(directory, imageFileName);
            OutputStream outputStream = new FileOutputStream(file);
            Bitmap pathsBitmap = savePathsToBitmap();
            pathsBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();

            // Add the image to the media store
            ContentValues values = new ContentValues();
            values.put(MediaStore.Images.Media.TITLE, imageFileName);
            values.put(MediaStore.Images.Media.DISPLAY_NAME, imageFileName);
            values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
            values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
            getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            // Notify the media scanner to refresh
            MediaScannerConnection.scanFile(getContext(), new String[]{file.getAbsolutePath()}, null, null);
            Toast.makeText(getContext(), "保存成功", Toast.LENGTH_SHORT).show();

        } catch (IOException e) {
            e.printStackTrace();
            // Show save error message
            // You can customize this part to display a dialog or toast
            Toast.makeText(getContext(), "保存失败", Toast.LENGTH_SHORT).show();
        }
    }
}