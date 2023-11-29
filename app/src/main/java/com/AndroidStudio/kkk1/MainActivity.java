package com.AndroidStudio.kkk1;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button add=findViewById(R.id.add);
        Button delete=findViewById(R.id.delete);

        add.setOnClickListener(view -> {
            //add按钮被点击之后执行内容

        });

        delete.setOnClickListener(view -> {
            //delete按钮被点击之后执行内容

        });

    }

}
