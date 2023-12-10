package com.AndroidStudio.kkk1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class MainActivity2 extends AppCompatActivity {
    private DrawView drawView;
    private MyDbHelper myDbHelper;
    private int itemId;
    private int alpha;
    private int color;
    private float strokeWidth;
    private ImageButton backActButton,eraserButton,clearButton,backButton,nextButton,saveButton,colorButton;

    //获取控件对象
    private void  initButton(){
        backActButton = findViewById(R.id.backActivity);
        eraserButton = findViewById(R.id.eraser);
        clearButton = findViewById(R.id.clear);
        saveButton = findViewById(R.id.save);
        backButton = findViewById(R.id.back);
        nextButton = findViewById(R.id.next);
        colorButton = findViewById(R.id.color);
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

        Intent intent = this.getIntent();
        // 获取传递的子项 ID
        itemId = getIntent().getIntExtra("id",-1);
        //如若点击项存在则将数据传递给DrawView和SeekBar
        byte[] imageBlobData = intent.getByteArrayExtra("image");
        if (imageBlobData != null) {
            // 将字节数组转换为Bitmap
            Bitmap imageBitmap = BitmapFactory.decodeByteArray(imageBlobData, 0, imageBlobData.length);
            // 将位图数据适配回DrawView
            if (imageBitmap != null) {
                drawView.setBackground(new BitmapDrawable(getResources(), imageBitmap));
            }
        }
        strokeWidth = intent.getFloatExtra("strokeWidth", 0);
        alpha = intent.getIntExtra("alpha", 255);
        color = intent.getIntExtra("color",0xFF000000);
        transparencySeekBar.setProgress(alpha);
        drawView.setPathAlpha(alpha);
        sizeSeekBar.setProgress((int) strokeWidth);
        drawView.setPathStrokeWidth(strokeWidth);
        drawView.setPathColor(color);

        backActButton.setOnClickListener(view -> {
            //  在Intent中添加回传的数据
            itemId = getIntent().getIntExtra("id", -1);
            if (itemId != -1) {
                // 存在有效的项ID，执行更新操作
                myDbHelper.updateCell(String.valueOf(itemId), drawView,strokeWidth,color,alpha);
            } else {
                // 无有效项ID，执行插入操作
                myDbHelper.insertCell(drawView,strokeWidth,color,alpha);
            }
            //创建一个intent指向主页面mainActivity
            Intent backIntent = new Intent(MainActivity2.this,MainActivity.class);
            // 将绘图数据转换为Bitmap
            Bitmap bitmap = drawView.savePathsToBitmap();
            // 将Bitmap转换为字节数组
            byte[] imageBytes = convertBitmapToByteArray(bitmap);
            backIntent.putExtra("image",imageBytes);
            backIntent.putExtra("strokeWidth", strokeWidth);
            backIntent.putExtra("alpha", alpha);
            backIntent.putExtra("color", color);
            //  设置回传结果为Activity.RESULT_OK
            setResult(Activity.RESULT_OK,backIntent);
            //   关闭当前Activity
            finish();
            //启动主页面
            startActivity(backIntent);
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
            drawView.saveImage();

        });


        backButton.setOnClickListener((View v) -> {
            // 实现撤回功能的代码
            drawView.undo();
        });


        nextButton.setOnClickListener((View v) -> {
            // 实现恢复功能的代码
            drawView.redo();
        });

        colorButton.setOnClickListener((View v) -> {
            // 实现更改画笔颜色功能的代码
            String[] colors = {"black", "brown", "red", "orange", "yellow", "green", "white", "skyBlue", "blue", "Prussia", "purple", "pink"};
            int[] selectedColors = {Color.BLACK,Color.rgb(99,37,37),Color.rgb(225,40,26),Color.rgb(246,113,11),Color.rgb(246,197,21),Color.rgb(75,204,28),
                    Color.rgb(13,225,200),Color.rgb(33,117,243),Color.rgb(5,16,244),Color.rgb(118,19,230),Color.rgb(255,76,189)};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("颜色");
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.color_picker, null);
            // 设置自定义视图的高度
            int dialogHeight = 300; // 设置为所需的高度，单位为像素
            dialogView.setMinimumHeight(dialogHeight);
            builder.setView(dialogView);
            for (int i = 0; i < colors.length; i++) {
                final int position = i;
                @SuppressLint("DiscouragedApi") ImageButton button = dialogView.findViewById(getResources().getIdentifier(colors[i], "id", getPackageName()));
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // 执行其他操作，如根据选择的颜色进行处理
                        color = selectedColors[position];
                        drawView.setPathColor(color);
                    }
                });
            }
            AlertDialog dialog = builder.create();
            dialog.show();

        });

        //设置画笔粗细的代码
        sizeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // 根据SeekBar的进度设置画笔的粗细
                strokeWidth = (float) progress;
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
                alpha = progress;
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

    private byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }
}