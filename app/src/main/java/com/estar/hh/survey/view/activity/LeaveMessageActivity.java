package com.estar.hh.survey.view.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.LeaveWordAdapter;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.Word;
import com.estar.hh.survey.entity.request.LeavewordRequest;
import com.estar.hh.survey.entity.request.MessageListRequest;
import com.estar.hh.survey.entity.response.LeavewordResponse;
import com.estar.hh.survey.entity.response.MessageListResponse;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.DateUtil;
import com.estar.utils.StringUtils;
import com.google.gson.Gson;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.estar.hh.survey.utils.TextUtils.setEditTextNoEngChar;

/**
 * Created by Administrator on 2017/9/29 0029.
 * 留言板
 */

public class LeaveMessageActivity extends HuangheBaseActivity {

    @ViewInject(R.id.leave_message_back)
    private LinearLayout back;

    @ViewInject(R.id.leave_message_list)
    private ListView list;

    @ViewInject(R.id.leave_message_content)
    private EditText content;

    @ViewInject(R.id.leave_message_submit)
    private LinearLayout submit;

    private List<Word> words = new ArrayList<>();
    private LeaveWordAdapter adapter;

    private UserSharePrefrence userSharePrefrence = null;
    private Mission mission = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.leave_message_activity);
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

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mission == null){
                    showShortToast("未找到报案号");
                    return;
                }

                if (StringUtils.isEmpty(content.getText().toString())){
                    showShortToast("请先填写备注内容");
                    return;
                }

                submitWord();
            }
        });

        setEditTextNoEngChar(content);

    }

    private void initData(){

        mission = (Mission) getIntent().getSerializableExtra("mission");
        userSharePrefrence = new UserSharePrefrence(this);

        adapter = new LeaveWordAdapter(this, words);
        list.setAdapter(adapter);

        messageList();
    }


    /**
     * 备注信息获取
     */
    private void messageList(){

        final MyProgressDialog dialog = new MyProgressDialog(this, "正在初始化留言信息...");

        MessageListRequest request = new MessageListRequest();
        request.getData().setRemarkEmp(userSharePrefrence.getUserEntity().getEmpCde());
        request.getData().setReportNo(mission.getRptNo());
        request.getData().setTaskNo(mission.getTaskId());

        final Gson gson = new Gson();
        final String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.WORDLIST, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.WORDLIST, reqMsg);

        LogUtils.i("留言列表请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("留言列表返回参数为:", "--------------------------------------------\n" + result);
                MessageListResponse response = gson.fromJson(result, MessageListResponse.class);
                if (response.getSuccess()){
                    if (response.getObj() != null && response.getObj().size() > 0){
                        words.clear();
                        words.addAll(response.getObj());
                        adapter.notifyDataSetChanged();
                    }else {
                        words.clear();
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("留言列表请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                ex.printStackTrace();
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("留言列表请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                cex.printStackTrace();
                showShortToast(cex.getLocalizedMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }


    /**
     * 备注信息提交
     */
    private void submitWord(){


        final MyProgressDialog dialog = new MyProgressDialog(this, "备注提交中...");

        final String contentValue = content.getText().toString();
        final String empCde = userSharePrefrence.getUserEntity().getEmpCde();
        final String nowTime = DateUtil.getTime_YMDMS(new Date());

        LeavewordRequest request = new LeavewordRequest();
        request.getData().setReportNo(mission.getRptNo());
        request.getData().setTaskNo(mission.getTaskId());
        request.getData().setRemarkDate(nowTime);
        request.getData().setRemarkEmp(empCde);
        request.getData().setRemarkContent(contentValue);
        final Gson gson = new Gson();
        String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.LEAVEWORD, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.LEAVEWORD, reqMsg);

        LogUtils.i("留言请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("留言返回参数为:", "--------------------------------------------\n" + result);
                LeavewordResponse response = gson.fromJson(result, LeavewordResponse.class);
                if (response.getSuccess()){
                    Word word = new Word();
                    word.setReportNo(mission.getRptNo());
                    word.setTaskNo(mission.getTaskId());
                    word.setRemarkEmp(empCde);
                    word.setRemarkContent(contentValue);
                    word.setRemarkDate(nowTime);
                    words.add(word);
                    adapter.notifyDataSetChanged();
                    content.setText("");
                }else{
                    showShortToast(response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("留言请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                showShortToast(ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("留言请求取消为:", "--------------------------------------------\n" + cex.getMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
            }
        });

    }

}
