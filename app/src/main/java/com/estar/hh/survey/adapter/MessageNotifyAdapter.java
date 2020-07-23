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
import com.estar.hh.survey.entity.entity.Message;
import com.estar.hh.survey.view.activity.MessageDetailActivity;

import java.util.List;

/**
 * Created by Administrator on 2017/8/7.
 * 消息列表适配器
 */

public class MessageNotifyAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messages;

    public MessageNotifyAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int i) {
        return messages.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        view = LayoutInflater.from(context).inflate(R.layout.message_notify_item, null);
        holder = new ViewHolder();
        holder.layout = view.findViewById(R.id.message_notify_item_layout);
        holder.title = view.findViewById(R.id.message_notify_item_title);
        holder.content = view.findViewById(R.id.message_notify_item_content);
        view.setTag(holder);

        /**
         * 此处通过数据刷新页面
         */
        {
            final Message message = messages.get(i);
            holder.content.setText(message.getNoticeContent());
            holder.title.setText(message.getNoticeTitle());

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, MessageDetailActivity.class);
                    intent.putExtra("message", message);
                    context.startActivity(intent);
                }
            });
        }

        return view;
    }

    private class ViewHolder {
        private LinearLayout layout;
        private TextView title;
        private TextView content;
    }
}
