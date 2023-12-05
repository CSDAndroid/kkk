package com.AndroidStudio.kkk1;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    private DrawView drawView;
    private ImageButton backActButton,paintButton,eraserButton,clearButton,backButton,nextButton,saveButton;

    //获取控件对象
    private void  initButton(){
        backActButton = findViewById(R.id.backActivity);
        paintButton = findViewById(R.id.paint);
        eraserButton = findViewById(R.id.eraser);
        clearButton = findViewById(R.id.clear);
        saveButton = findViewById(R.id.save);
        backButton = findViewById(R.id.back);
        nextButton = findViewById(R.id.next);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initButton();

        drawView = findViewById(R.id.drawView);
        SeekBar sizeSeekBar = findViewById(R.id.size);
        SeekBar transparencySeekBar = findViewById(R.id.transparency);

        backActButton.setOnClickListener(view -> {
            //创建一个intent指向主页面mainActivity
            Intent backIntent = new Intent(MainActivity2.this, MainActivity.class);
            //启动主页面
            startActivity(backIntent);
        });

        paintButton.setOnClickListener((View v) -> {
            // 实现画笔功能的代码
            drawView.setEraser(false);
        });

        eraserButton.setOnClickListener((View v) -> {
            // 实现擦除功能的代码
            drawView.setEraser(true);
        });


        clearButton.setOnClickListener((View v) -> {
            //  实现清空功能的代码
            drawView.clearCanvas();
        });


        saveButton.setOnClickListener((View v) -> {
            // 实现保存功能的代码
        });


        backButton.setOnClickListener((View v) -> {
            // 实现撤回功能的代码
            drawView.undo();
        });


        nextButton.setOnClickListener((View v) -> {
            // 实现恢复功能的代码
            drawView.redo();
        });

        //设置画笔粗细的代码
        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 根据SeekBar的进度设置画笔的粗细
                float strokeWidth = (float) progress;
                drawView.setPathStrokeWidth(strokeWidth);

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
                int alpha = progress;
                drawView.setPathAlpha(alpha);
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

    //SQLite
    class myHelper extends SQLiteOpenHelper{

        //构造器，其中参数：上下文，数据库文件名称，结果集工厂，版本号，定义数据库
        public myHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        //数据库初始化的时候用于创建表或视图文件
        @Override
        public void onCreate(SQLiteDatabase db) {

        }

        //升级方法
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }
    }
}