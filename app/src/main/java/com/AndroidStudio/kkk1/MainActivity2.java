package com.AndroidStudio.kkk1;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        DrawView mDrawingView = findViewById(R.id.drawingview);

        Button paintButton = findViewById(R.id.paint);
        paintButton.setOnClickListener((View v) -> {
            // 实现画笔功能的代码
            startDrawing();
        });
        @SuppressLint("WrongViewCast")
        Button eraserButton = findViewById(R.id.eraser);
        eraserButton.setOnClickListener((View v) -> {
            // 实现擦除功能的代码
            mDrawingView.setErasing(true);
        });

        @SuppressLint("WrongViewCast")
        Button clearButton = findViewById(R.id.clear);
        clearButton.setOnClickListener((View v) -> {
            //  实现清空功能的代码
            mDrawingView.clearCanvas();
        });

        @SuppressLint("WrongViewCast")
        Button saveButton = findViewById(R.id.save);
        saveButton.setOnClickListener((View v) -> {
            // 实现保存功能的代码
        });



    }

    private void startDrawing() {
            // 获取画布控件
            FrameLayout frameDrawView = findViewById(R.id.FrameDrawView);
            // 创建一个DrawingView，继承自View
            DrawView drawingView = new DrawView(this);
            // 将DrawingView添加到画布上
            frameDrawView.addView(drawingView);
            // 设置DrawingView的宽高
            drawingView.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
    }


}