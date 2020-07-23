package com.estar.hh.survey.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.Message;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/12/19 0019.
 */

public class MessageDetailActivity extends HuangheBaseActivity {

    @ViewInject(R.id.message_detail_back)
    private LinearLayout back;

    @ViewInject(R.id.message_detail_title)
    private TextView title;

    @ViewInject(R.id.message_detail_title_show)
    private EditText titleShow;

    @ViewInject(R.id.message_detail_content)
    private EditText content;

    private Message message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_detail_activity);
        x.view().inject(this);

        initView();
        initData();

    }

    private void initView(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        titleShow.setEnabled(false);
        content.setEnabled(false);
    }

    private void initData(){
        message = (Message) getIntent().getSerializableExtra("message");

        if (message != null){
            title.setText(message.getNoticeTitle());
            titleShow.setText(message.getNoticeTitle());
            content.setText(message.getNoticeContent());
        }
    }

}
