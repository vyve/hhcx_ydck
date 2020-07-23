package com.estar.hh.survey.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.constants.TimeFormat;
import com.estar.hh.survey.entity.entity.Note;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Date;

import static com.estar.hh.survey.utils.TextUtils.setEditTextNoEngChar;

/**
 * Created by Administrator on 2017/10/11 0011.
 * 记事本详细页
 */

public class NoteBookDetailActivity extends HuangheBaseActivity {

    @ViewInject(R.id.note_book_detail_back)
    private LinearLayout back;

    @ViewInject(R.id.note_book_detail_save)
    private TextView save;

    @ViewInject(R.id.note_book_detail_title)
    private EditText title;

    @ViewInject(R.id.note_book_detail_content)
    private EditText content;

    private Note note;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_book_detail_activity);
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

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleValue = title.getText().toString();
                String contentValue = content.getText().toString();
                if (StringUtils.isEmpty(titleValue)){
                    ToastUtils.showShort(NoteBookDetailActivity.this, "请输入标题");
                    return;
                }
                if (StringUtils.isEmpty(contentValue)){
                    ToastUtils.showShort(NoteBookDetailActivity.this, "请输入内容");
                    return;
                }

                boolean save;//保存标志

                if (note != null) {
                    note.setTitle(titleValue);
                    note.setContent(contentValue);
                    save = note.saveOrUpdate("timeFlag = ?", note.getTimeFlag());
                }else {
                    Note noteNew = new Note();
                    noteNew.setTitle(titleValue);
                    noteNew.setContent(contentValue);
                    noteNew.setTimeFlag(TimeFormat.FORMAT_YMDHMS.format(new Date()).replace("-","").replace(":","").replace(" ",""));
                    noteNew.setTime(TimeFormat.FORMAT_YMDHMS.format(new Date()));
                    save = noteNew.save();
                }

                if (save){
                    ToastUtils.showShort(NoteBookDetailActivity.this, "保存成功");
                    finish();
                }else {
                    ToastUtils.showShort(NoteBookDetailActivity.this, "保存失败");
                }
            }
        });

        setEditTextNoEngChar(title);
        setEditTextNoEngChar(content);
    }

    private void initData(){
        note = (Note) getIntent().getSerializableExtra("note");
        if (note != null) {
            title.setText(note.getTitle());
            //设置标题不可编辑
            title.setEnabled(false);
            title.setFocusable(false);
            title.setKeyListener(null);//重点
            content.setText(note.getContent());
        }
    }

}
