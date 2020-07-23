package com.estar.hh.survey.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.StateSelectAdapter;
import com.estar.hh.survey.entity.entity.KeyValue;

import java.util.List;

/**
 * Created by Administrator on 2017/8/17.
 * 业务助手
 */

public class StateSelectDialog extends Dialog{

    private ListView list;
    private StateSelectAdapter adapter;
    private List<KeyValue> states;
    private Context context;

    private AdapterView.OnItemSelectedListener stateClickListener;//事件点击回调
    private TextView textView;

    public StateSelectDialog(Context context, List<KeyValue> states, AdapterView.OnItemSelectedListener stateClickListener){
        super(context, R.style.state_select);
        this.context = context;
        this.states = states;
        adapter = new StateSelectAdapter(context, states);
        this.stateClickListener = stateClickListener;
    }

    public StateSelectDialog(Context context, List<KeyValue> states, TextView textView){
        super(context, R.style.state_select);
        this.context = context;
        this.states = states;
        this.textView = textView;
        adapter = new StateSelectAdapter(context, states, textView, this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.state_select_dialog);

        setCanceledOnTouchOutside(true);

        list = findViewById(R.id.state_select_list);
        list.setAdapter(adapter);
    }


}
