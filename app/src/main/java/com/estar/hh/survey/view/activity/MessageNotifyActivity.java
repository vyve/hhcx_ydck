package com.estar.hh.survey.view.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.MessageNotifyAdapter;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.entity.entity.Message;
import com.estar.hh.survey.entity.request.MessageRequest;
import com.estar.hh.survey.entity.response.MessageResponse;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.component.MessageStateSelectDialog;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.DateUtil;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import view.XListView;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 通知消息
 */

public class MessageNotifyActivity extends HuangheBaseActivity implements XListView.IXListViewListener{

    @ViewInject(R.id.message_notify_back)
    private LinearLayout back;

    @ViewInject(R.id.message_select)
    private TextView select;

    @ViewInject(R.id.message_notify_list)
    private XListView list;

    private MessageStateSelectDialog dialog = null;
    private List<KeyValue> states = new ArrayList<>();

    private List<Message> messages = new ArrayList<>();
    private MessageNotifyAdapter adapter;

    private UserSharePrefrence userSharePrefrence;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_notify_activity);
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

        states.add(new KeyValue("通知", "1"));
        states.add(new KeyValue("公告", "2"));
        dialog = new MessageStateSelectDialog(this, states);

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


        list.setPullLoadEnable(false);
        list.setPullRefreshEnable(false);
        list.setXListViewListener(this);

    }

    private void initData(){

        userSharePrefrence = new UserSharePrefrence(this);

        adapter = new MessageNotifyAdapter(this, messages);
        list.setAdapter(adapter);

        caseList(0);

    }


    /**
     * 消息列表
     */
    public void caseList(int state){

        final MyProgressDialog dialog = new MyProgressDialog(this, "获取消息列表...");

        MessageRequest request = new MessageRequest();
        request.getData().setUserName(userSharePrefrence.getUserEntity().getUserName());
        request.getData().setNoticeType(state + "");

        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.MESSAGESEARCH, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.MESSAGESEARCH, reqMsg);

        LogUtils.i("消息列表请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("消息列表返回参数为:", "--------------------------------------------\n" + result);
                MessageResponse response = gson.fromJson(result, MessageResponse.class);
                if (response.getSuccess()){

                    if (response.getObj() != null && response.getObj().size() > 0){
                        messages.clear();
                        messages.addAll(response.getObj());
                        adapter.notifyDataSetChanged();
                    }else {
                        showShortToast(response.getMsg());
                        messages.clear();
                        adapter.notifyDataSetChanged();
                    }

                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("消息列表请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("消息列表请求取消为:", "--------------------------------------------\n" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
                loadComplete();
            }
        });
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }

    private void loadComplete(){
        list.stopRefresh();
        list.stopLoadMore();
        list.setRefreshTime(DateUtil.getTime_YMDMS(new Date()));
    }

}
