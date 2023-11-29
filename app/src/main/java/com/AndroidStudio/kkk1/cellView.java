package com.AndroidStudio.kkk1;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

public class cellView extends androidx.appcompat.widget.AppCompatTextView {
    public cellView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 设置自定义属性
        setTextSize(18); // 设置文本大小
        setBackgroundColor(Color.WHITE); // 设置背景颜色
    }
}
