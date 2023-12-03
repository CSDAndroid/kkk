package com.AndroidStudio.kkk1;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class CellAdapter extends BaseAdapter {
    private List<Model> list;
    //layoutInflater用于将某个布局转换为view对象
    private LayoutInflater layoutInflater;
    //当创建adapter对象的时候需要list数据
    public CellAdapter(List<Model> list, Context context){
        this.list = list;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        // 获取对象，对象对应的表中的某条记录
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.model,null,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder)convertView.getTag();
        }

        Model model = (Model) getItem(position);
        // 将数据库中的内容加载到相应控件上
        //    *****   viewHolder.t_image.setImage(model.getImage());
        viewHolder.t_text.setText(model.getText());

        return convertView;
    }

    //ViewHolder用于给item视图加载数据内容
    class ViewHolder{
        ImageView t_image;
        TextView t_text;
        public ViewHolder(View view){
            t_image = view.findViewById(R.id.itemImage);
            t_text = view.findViewById(R.id.itemText);
        }

    }
}
