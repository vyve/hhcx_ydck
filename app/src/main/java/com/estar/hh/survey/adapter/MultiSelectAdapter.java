package com.estar.hh.survey.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.view.component.MultiSelectDialog;
import com.estar.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 记事本列表适配器
 */

public class MultiSelectAdapter extends BaseAdapter {

    private Context context;
    private List<KeyValue> states;

    private TextView textView;

    private List<String> textKeys = null;//多选控件值

    public MultiSelectAdapter(Context context, List<KeyValue> states) {
        this.context = context;
        this.states = states;
    }

    public MultiSelectAdapter(Context context, List<KeyValue> states, TextView textView) {
        this.context = context;
        this.states = states;
        this.textView = textView;
        this.textKeys = getTextKey(textView);
        initKeyValueTag(this.states, this.textKeys);
    }

    @Override
    public int getCount() {
        return states.size();
    }

    @Override
    public KeyValue getItem(int i) {
        return states.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        final ViewHolder holder;

        view = LayoutInflater.from(context).inflate(R.layout.multi_select_item, null);
        holder = new ViewHolder();
        holder.name = view.findViewById(R.id.multi_select_item_name);
        holder.box = view.findViewById(R.id.multi_select_item_box);
        holder.layout = view.findViewById(R.id.multi_select_item_layout);
        view.setTag(holder);

        {
            final KeyValue keyValue = states.get(i);

            if (!StringUtils.isEmpty(keyValue.getTag()) && keyValue.getTag().equals("true")){
                Glide.with(context)
                        .load(R.mipmap.box_select)
                        .centerCrop()
                        .into(holder.box);
            }else {
                Glide.with(context)
                        .load(R.mipmap.box_unselect)
                        .centerCrop()
                        .into(holder.box);
            }

            holder.name.setText(keyValue.getKey());

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!StringUtils.isEmpty(keyValue.getTag()) && keyValue.getTag().equals("true")){//如果已选择被点击
                        keyValue.setTag("false");
                        Glide.with(context)
                                .load(R.mipmap.box_unselect)
                                .centerCrop()
                                .into(holder.box);
                    }else {//如果未选择被点击
                        keyValue.setTag("true");
                        Glide.with(context)
                                .load(R.mipmap.box_select)
                                .centerCrop()
                                .into(holder.box);
                    }
                    notifyDataSetChanged();
                }
            });

        }

        return view;
    }


    private void initKeyValueTag(List<KeyValue> keyValues, List<String> textKeys){

        if (textKeys == null || textKeys.size() == 0){
            for (KeyValue keyValue : keyValues){
                keyValue.setTag("false");
            }
            return;
        }

        for (KeyValue keyValue : keyValues){
            if (textKeys.contains(keyValue.getValue())){
                keyValue.setTag("true");
            }
        }

    }


    /**
     * 获取文本的信息内容
     * @param textView
     * @return
     */
    public List<String> getTextKey(TextView textView){

        List<String> keys = null;

        if (textView.getTag() != null){
            keys = new ArrayList<>();
            String[] textKeys = textView.getTag().toString().split(",");
            if (textKeys != null && textKeys.length > 0){
                for (int i =0 ; i < textKeys.length; i++){
                    keys.add(textKeys[i]);
                }
            }
        }

        return keys;
    }

    private class ViewHolder {
//        private boolean isCheckedFlag = false;//被选择状态
        private ImageView box;
        private TextView name;
        private LinearLayout layout;
    }

}
