package com.AndroidStudio.kkk1;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

public class myGridView extends GridView {

    public myGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 设置自定义属性
        setRowHeight(50); // 设置单元格高度
        setColumnWidth(100); // 设置单元格宽度
        setHorizontalSpacing(10); // 设置单元格水平间距
        setVerticalSpacing(10); // 设置单元格垂直间距

    }

    public void setRowHeight(int rowHeight) {
    }


}
