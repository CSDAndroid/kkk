package com.AndroidStudio.kkk1;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    private DrawView mDrawingView;
    private Button mColorButton;
    private int mColor = Color.BLACK;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mDrawingView = findViewById(R.id.drawview);
        SeekBar sizeSeekBar = findViewById(R.id.size);
        SeekBar transparencySeekBar = findViewById(R.id.transparency);
        mColorButton = findViewById(R.id.color);


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

        Button backButton = findViewById(R.id.back);
        backButton.setOnClickListener((View v) -> {
            // 实现撤回功能的代码
        });


        Button nextButton = findViewById(R.id.next);
        nextButton.setOnClickListener((View v) -> {
            // 实现恢复功能的代码
        });



        //设置画笔粗细的代码
        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 根据SeekBar的进度设置画笔的粗细
                float strokeWidth = (float) progress;
                mDrawingView.setStrokeWidth(strokeWidth);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //    显示该seekBar更改配置为画笔粗细
                Toast.makeText(MainActivity2.this,"画笔粗细",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 当停止拖动SeekBar时，不需要执行任何操作
                }
        });


        //设置画笔透明度的代码
        transparencySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 根据SeekBar的进度设置画笔的透明度
                mDrawingView.setAlpha(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //   显示该seekBar配置为更改画笔透明度
                Toast.makeText(MainActivity2.this,"透明度",Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // 当停止拖动SeekBar时，不需要执行任何操作

            }
        });
    }


    //   画笔功能自定义函数
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