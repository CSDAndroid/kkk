package com.AndroidStudio.kkk1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    private DrawView drawView;
    private MyDbHelper myDbHelper;
    private boolean addButton;
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
        myDbHelper = new MyDbHelper(MainActivity2.this,"model.db",null,1);
        initButton();

        drawView = findViewById(R.id.drawView);
        SeekBar sizeSeekBar = findViewById(R.id.size);
        SeekBar transparencySeekBar = findViewById(R.id.transparency);

        backActButton.setOnClickListener(view -> {
            //创建一个intent指向主页面mainActivity
            Intent backIntent = new Intent(MainActivity2.this,MainActivity.class);
            //  在Intent中添加回传的数据
            int itemId = getIntent().getIntExtra("id", -1);
            if (itemId != -1) {
                // 存在有效的项ID，执行更新操作
                myDbHelper.updateCell(String.valueOf(itemId), drawView);
            } else {
                // 无有效项ID，执行插入操作
                myDbHelper.insertCell(drawView);
            }
            //  设置回传结果为Activity.RESULT_OK
            setResult(Activity.RESULT_OK,backIntent);
            //   关闭当前Activity
            finish();
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
    public Boolean judgeAdd(boolean addButton){
        return addButton;
    }

}