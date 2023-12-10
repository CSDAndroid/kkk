package com.AndroidStudio.kkk1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private GridView gridView;
    private CellAdapter cellAdapter;
    private MyDbHelper myDbHelper;
    private List<Model> resultGrid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        gridView = findViewById(R.id.gridView);
        resultGrid = new ArrayList<>();
        Button add = findViewById(R.id.add);

        myDbHelper = new MyDbHelper(MainActivity.this,"model.db",null,1);


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
                // 获取点击的Model对象
                Model clickedModel = resultGrid.get(position);
                Intent fixIntent = new Intent(MainActivity.this,MainActivity2.class);
                int clickedItemID = clickedModel.getId();
                int clickedColor = clickedModel.getColor();
                float clickedStrokeWidth = clickedModel.getStrokeWidth();
                int clickedAlpha = clickedModel.getAlpha();
                fixIntent.putExtra("id",clickedItemID);
                fixIntent.putExtra("color",clickedColor);
                fixIntent.putExtra("strokeWidth", clickedStrokeWidth);
                fixIntent.putExtra("alpha", clickedAlpha);
                startActivityForResult(fixIntent, 1);
            }
        });

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // 获取长按的Model对象
                Model longClickedModel = resultGrid.get(position);
                long clickedItemId = longClickedModel.getId();

                // 弹出确认删除的对话框
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("删除")
                        .setMessage("是否删除该项作品？")
                        .setPositiveButton("删除", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 执行删除操作
                                myDbHelper.deleteCell(String.valueOf(clickedItemId));
                                // 更新主页列表
                                initCell();
                            }
                        })
                        .setNegativeButton("取消", null)
                        .show();

                return true;
            }
        });

    }
    //查询数据库内容，将表中数据显示在gridView上面
    private void init(){
        if (resultGrid != null){
            resultGrid.clear();
        }
        resultGrid = myDbHelper.query();
        cellAdapter = new CellAdapter(MainActivity.this,resultGrid);
        gridView.setAdapter(cellAdapter);

    }
private void initCell(){
    resultGrid.clear();
    resultGrid.addAll(myDbHelper.query());
    cellAdapter.notifyDataSetChanged();
}
    //  接收回传数据
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK){
            if (data != null){
                DrawView returnedData = (DrawView) data.getSerializableExtra("image");
                Float strokeWidth = data.getFloatExtra("strokeWidth",0);
                int color = data.getIntExtra("color",0xFF000000);
                int alpha = data.getIntExtra("alpha",255);
                //  处理回传数据,将返回的图像数据插入数据库
                myDbHelper.insertCell(returnedData,strokeWidth,color,alpha);
                // 更新gridView
                init();

            }
        }
    }

}
