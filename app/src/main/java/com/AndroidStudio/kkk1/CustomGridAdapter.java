package com.AndroidStudio.kkk1;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;

import java.util.List;

public class CustomGridAdapter extends ArrayAdapter<cellView> {
    public CustomGridAdapter(Context context, List<cellView> cells) {
        super(context, R.layout.drawing, cells);
    }
    @NonNull
    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        cellView cell = getItem(position);
        // 设置单元格数据
        if (cell != null) {
            cell.setText("Cell " + position);
        }
        assert cell != null;
        return cell;
    }

    public cellView getItem(int position) {
        return null;
    }
}
