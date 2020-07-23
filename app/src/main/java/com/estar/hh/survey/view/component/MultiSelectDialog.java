package com.estar.hh.survey.view.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.MultiSelectAdapter;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 * 多选
 */

public class MultiSelectDialog extends Dialog{

    private ListView list;
    private TextView cancel;
    private TextView comfirm;

    private MultiSelectAdapter adapter;
    private Context context;
    private TextView textView;

    private List<KeyValue> states = null;

    public MultiSelectDialog(Context context, List<KeyValue> states){
        super(context, R.style.state_select);
        this.context = context;
        this.states = states;
        adapter = new MultiSelectAdapter(context, states);
    }

    public MultiSelectDialog(Context context, List<KeyValue> states, TextView textView){
        super(context, R.style.state_select);
        this.context = context;
        this.textView = textView;
        this.states = states;
        adapter = new MultiSelectAdapter(context, states, textView);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.multi_select_dialog);

        setCanceledOnTouchOutside(true);

        list = findViewById(R.id.multi_select_list);
        cancel = findViewById(R.id.multi_select_cancel);
        comfirm = findViewById(R.id.multi_select_comfirm);

        list.setAdapter(adapter);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MultiSelectDialog.this.dismiss();
            }
        });

        comfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(getKeys(states));
                textView.setTag(getValues(states));
                MultiSelectDialog.this.dismiss();
            }
        });

    }

    /**
     * 获取勾选状态值
     * @param keyValues
     * @return
     */
    private String getKeys(List<KeyValue> keyValues){

        String keys = "";

        for (int i = 0; i < keyValues.size(); i++){
            if (!StringUtils.isEmpty(keyValues.get(i).getTag()) && keyValues.get(i).getTag().equals("true")) {
                if (StringUtils.isEmpty(keys)) {
                    keys = keyValues.get(i).getKey();
                } else {
                    keys = keys + "," + keyValues.get(i).getKey();
                }
            }
        }

        return keys;
    }

    /**
     * 获取勾选状态键
     * @param keyValues
     * @return
     */
    private String getValues(List<KeyValue> keyValues){

        String values = "";

        for (int i = 0; i < keyValues.size(); i++){
            if (!StringUtils.isEmpty(keyValues.get(i).getTag()) && keyValues.get(i).getTag().equals("true")) {
                if (StringUtils.isEmpty(values)) {
                    values = keyValues.get(i).getValue();
                } else {
                    values = values + "," + keyValues.get(i).getValue();
                }
            }
        }

        return values;
    }


}
