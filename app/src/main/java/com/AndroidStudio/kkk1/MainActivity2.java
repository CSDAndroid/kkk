package com.AndroidStudio.kkk1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity2 extends AppCompatActivity {
    private DrawView drawView;
    private MyDbHelper myDbHelper;
    private int itemId;
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
        // 获取传递的子项 ID
        itemId = getIntent().getIntExtra("id",-1);
        initButton();

        drawView = findViewById(R.id.drawView);
        SeekBar sizeSeekBar = findViewById(R.id.size);
        SeekBar transparencySeekBar = findViewById(R.id.transparency);

        backActButton.setOnClickListener(view -> {
            //创建一个intent指向主页面mainActivity
            Intent backIntent = new Intent(MainActivity2.this,MainActivity.class);
            //  在Intent中添加回传的数据
            itemId = getIntent().getIntExtra("id", -1);
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
            Bitmap bitmap = drawView.getBitmap(); // 获取DrawView的位图
            // 保存位图到设备的存储中
            String filename = "draw_image.png";
            File file = new File(getExternalFilesDir(null), filename);
            FileOutputStream outputStream;
            try {
                outputStream = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.flush();
                outputStream.close();

                // 将图片添加到相册中
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DISPLAY_NAME, filename);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_DCIM);

                ContentResolver contentResolver = getContentResolver();
                Uri imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

                // 通知相册刷新
                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                mediaScanIntent.setData(imageUri);
                sendBroadcast(mediaScanIntent);

                Toast.makeText(MainActivity2.this, "图像保存成功", Toast.LENGTH_SHORT).show();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(MainActivity2.this, "图像保存失败", Toast.LENGTH_SHORT).show();
            }

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
            builder.setTitle("选择颜色");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
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
                        drawView.setColor(selectedColors[position]);
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
}