package com.AndroidStudio.kkk1;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private Button add;
    private CellAdapter cellAdapter;
    private List<Model> resultGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gridView = findViewById(R.id.gridView);
        add=findViewById(R.id.add);

        MyDbHelper dbHelper = new MyDbHelper(MainActivity.this,"model.db",null,1);

        add.setOnClickListener(view -> {
            //add按钮被点击之后执行内容
            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            //   数据回传：跳转之后,期望第二个页面回传数据
            startActivityForResult(intent,1);
        });
        //  数据初始化
        init();
        // 设置gridView项的点击监听器
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // 当列表项被点击时对该项内容进行修改操作

            }
        });

    }
    //查询数据库内容，将表中数据显示在gridView上面
    private void init(){
        if (resultGrid != null){
            resultGrid.clear();
        }
        MyDbHelper myDbHelper = new MyDbHelper(MainActivity.this, "model.db", null, 1);
        resultGrid = myDbHelper.query();
        cellAdapter = new CellAdapter(MainActivity.this,resultGrid);
        gridView.setAdapter(cellAdapter);

    }

}
