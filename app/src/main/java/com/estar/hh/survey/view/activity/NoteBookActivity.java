package com.estar.hh.survey.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.NoteBookAdapter;
import com.estar.hh.survey.entity.entity.Note;

import org.litepal.crud.DataSupport;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/10/11 0011.
 * 记事本页面
 */

public class NoteBookActivity extends HuangheBaseActivity {

    @ViewInject(R.id.note_book_back)
    private LinearLayout back;

    @ViewInject(R.id.note_book_list)
    private ListView list;

    @ViewInject(R.id.note_book_add)
    private LinearLayout add;

    public List<Note> notes = new ArrayList<>();
    public NoteBookAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_book_activity);
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

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NoteBookActivity.this, NoteBookDetailActivity.class));
            }
        });
    }

    private void initData(){
        notes = DataSupport.where("").find(Note.class);
        adapter = new NoteBookAdapter(this, notes);
        list.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Note> result = DataSupport.where("").find(Note.class);
        if (result != null && result.size() > 0){
            notes.clear();
            notes.addAll(result);
        }else {
            notes.clear();
        }
        adapter.notifyDataSetChanged();
    }
}
