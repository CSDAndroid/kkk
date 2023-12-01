package com.AndroidStudio.kkk1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class DrawView extends View {
    private Paint mPaint;
    private Path mPath;

    private float mStrokeWidth;
    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init(){
        //  初始化画笔，画布设置
        mPaint = new Paint();
        mPaint.setColor(0xFF000000);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(12);
        mStrokeWidth=10;
        //  初始化路径
        mPath = new Path();
    }

    public void setStrokeWidth(float strokeWidth) {
        mStrokeWidth = strokeWidth;
        mPaint.setStrokeWidth(mStrokeWidth);
    }

    @Override
    //  重写dnDraw
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(mPath, mPaint);
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
        mPath.reset();

        // 触发重绘
        invalidate();
    }

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
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                // 手指抬起时，重置路径
                mPath.reset();
                break;
        }

        // 触发重绘
        if (event.getAction() != MotionEvent.ACTION_UP) {
            invalidate();
        }
        return true;
    }


}
