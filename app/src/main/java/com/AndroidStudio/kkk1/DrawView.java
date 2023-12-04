package com.AndroidStudio.kkk1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class DrawView extends View {
    public Paint mPaint;
    public Path mPath;
    public float mStrokeWidth;
    private ArrayList<Path> paths;
    private  int paintColor;
    public int penAlpha = 255;
    public DrawView(Context context) {
        this(context,null);

    }

    public DrawView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init(){
        //  初始化画笔，画布设置
        //  初始化路径
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setAlpha(penAlpha);
        paths = new ArrayList<>();
    }

    @Override
    //  重写dnDraw
    protected void onDraw(@NonNull Canvas canvas) {
        for (Path path : paths){
            canvas.drawPath(path,mPaint);
        }
        mPaint.setAlpha(penAlpha);
        canvas.drawPath(mPath,mPaint);
    }

    public void setErasing(boolean isErasing) {
        //  橡皮擦按钮功能
        if (isErasing) {
            // 设置为橡皮擦模式
            mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        } else {
            // 设置为绘制模式
            mPaint.setXfermode(null);
        }
    }

    public void clearCanvas() {
        //  清空按钮功能
        // 清空路径
        paths.clear();
        mPath.reset();

        // 触发重绘
        invalidate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    //  监听路径
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 手指按下时，移动路径起点
                mPath.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                // 手指移动时，连接路径
                mPaint.setStrokeWidth(mStrokeWidth);
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起时，添加路径
                paths.add(mPath);
                mPath = new Path();
                mPaint.setAlpha(penAlpha);
                mPaint.setStrokeWidth(mStrokeWidth);
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
        Bitmap canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas drawCanvas = new Canvas(canvasBitmap);
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        mPaint.setStrokeWidth(mStrokeWidth);
    }


    public void setPenAlpha(int alpha) {
        penAlpha = alpha;
    }
}