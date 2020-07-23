package com.estar.hh.survey.view.component;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.MessageStateSelectAdapter;
import com.estar.hh.survey.entity.entity.KeyValue;

import java.util.List;


/**
 * Created by Administrator on 2017/8/17.
 * 选择框
 */

public class MessageStateSelectDialog extends Dialog{

    private ListView list;
    private MessageStateSelectAdapter adapter;


    public MessageStateSelectDialog(Context context, List<KeyValue> states, AdapterView.OnItemSelectedListener stateClickListener){
        super(context, R.style.state_select);
        adapter = new MessageStateSelectAdapter(context, states);
    }

    public MessageStateSelectDialog(Context context, List<KeyValue> states){
        super(context, R.style.state_select);
        adapter = new MessageStateSelectAdapter(context, states, this);
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
