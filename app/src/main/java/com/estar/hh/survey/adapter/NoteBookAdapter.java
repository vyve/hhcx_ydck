package com.estar.hh.survey.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.entity.entity.Note;
import com.estar.hh.survey.view.activity.NoteBookActivity;
import com.estar.hh.survey.view.activity.NoteBookDetailActivity;
import com.estar.hh.survey.view.activity.ThingLossDetailActivity;
import com.estar.utils.ToastUtils;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 记事本列表适配器
 */

public class NoteBookAdapter extends BaseAdapter{

    private Context context;
    private List<Note> notes;

    public NoteBookAdapter(Context context, List<Note> notes){
        this.context = context;
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int i) {
        return notes.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder ;
        if (view == null){
            view = LayoutInflater.from(context).inflate(R.layout.note_book_item, null);
            holder = new ViewHolder();
            holder.title = view.findViewById(R.id.note_book_title);
            holder.time = view.findViewById(R.id.note_book_time);
            holder.delete = view.findViewById(R.id.note_book_delete);
            holder.detail = view.findViewById(R.id.note_book_detail);
            view.setTag(holder);
        }else {
            holder = (ViewHolder)view.getTag();
        }

        /**
         * 此处通过数据刷新页面
         */
        {
            final Note note = notes.get(i);
            if (note == null){
                return view;
            }

            holder.title.setText(note.getTitle());
            holder.time.setText(note.getTime());
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    note.delete();
                    List<Note> result = DataSupport.where("").find(Note.class);
                    if (result != null && result.size() > 0){
                        ((NoteBookActivity)context).notes.clear();
                        ((NoteBookActivity)context).notes.addAll(result);
                    }else {
                        ((NoteBookActivity)context).notes.clear();
                    }
                    notifyDataSetChanged();
                    ToastUtils.showShort(context, "删除成功");
                }
            });

            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, NoteBookDetailActivity.class);
                    intent.putExtra("note", note);
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    private class ViewHolder{
        private TextView title;
        private TextView time;
        private LinearLayout delete;
        private LinearLayout detail;
    }

}
