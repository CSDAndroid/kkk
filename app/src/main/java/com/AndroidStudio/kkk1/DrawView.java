package com.AndroidStudio.kkk1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Stack;

public class DrawView extends FrameLayout {
    private GLSurfaceView glSurfaceView;
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
        Bitmap canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas drawCanvas = new Canvas(canvasBitmap);
    }

    //  设置橡皮擦模式
    public void setEraser(boolean isEraser){
        this.isEraser = isEraser;
        if (isEraser) {
            mPaintColor = Color.WHITE;
        }
    }

    //   设置画笔粗细功能
    public void setPathStrokeWidth(float strokeWidth){
        mStrokeWidth = strokeWidth;
    }

    //    设置画笔不透明度功能
    public void setPathAlpha(int alpha){
        mAlpha = alpha;
    }

    public void setPathPaintColor(int paintColor){
        mPaintColor = paintColor;
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

}