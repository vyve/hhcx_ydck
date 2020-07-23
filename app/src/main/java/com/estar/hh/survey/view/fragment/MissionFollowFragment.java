package com.estar.hh.survey.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.MissionFollowAdapter;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.CaseSearchDto;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.MissionFollowListRequest;
import com.estar.hh.survey.entity.request.MissionListRequest;
import com.estar.hh.survey.entity.response.MissionFollowListResponse;
import com.estar.hh.survey.entity.response.MissionListResponse;
import com.estar.hh.survey.utils.HttpClientUtil;
import com.estar.hh.survey.utils.HttpUtils;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.hh.survey.view.component.MyProgressDialog;
import com.estar.utils.DateUtil;
import com.estar.utils.StringUtils;
import com.estar.utils.ToastUtils;
import com.google.gson.Gson;


import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import view.XListView;

/**
 * Created by Administrator on 2017/9/28 0028.
 */

public class MissionFollowFragment extends Fragment implements XListView.IXListViewListener{

    private EditText conditionInput;
    private TextView startTime;
    private TextView endTime;
    private ImageView search;
    private XListView missionList;

    private MissionFollowAdapter adapter;
    private List<CaseSearchDto> missions = new ArrayList<>();

    private int MISSIONTYPE;//判断是哪个状态

    private TimePickerView pvTime;//时间选择器
    private Date nowTime;
    private int timeSelectType = 0;//时间选择判断

    private User user;

    private boolean firstRequest = true;// 新任务首次请求标志

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.mission_follow_fragment, container, false);

        initView(contextView);
        initData();

        return contextView;
    }

    private void initView(View view){
        conditionInput = view.findViewById(R.id.mission_follow_caseno_input);
        startTime = view.findViewById(R.id.mission_follow_time_start);
        endTime = view.findViewById(R.id.mission_follow_time_end);
        search = view.findViewById(R.id.mission_follow_search);
        missionList = view.findViewById(R.id.mission_follow_list);

        missionList.setPullRefreshEnable(true);
        missionList.setPullLoadEnable(false);
        missionList.setXListViewListener(this);

    }

    private void initData(){

        UserSharePrefrence userSharePrefrence = new UserSharePrefrence(getActivity());
        user = userSharePrefrence.getUserEntity();


        //获取Activity传递过来的参数
        Bundle mBundle = getArguments();
        MISSIONTYPE = mBundle.getInt("missiontype");//0案件查询 1已完成

        adapter = new MissionFollowAdapter(getActivity(), missions, MISSIONTYPE);
        missionList.setAdapter(adapter);

        nowTime=new Date();
        startTime.setText(DateUtil.getTime(nowTime));
        endTime.setText(DateUtil.getTime(nowTime));
        //时间选择器
        pvTime = new TimePickerView(getActivity(), TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                if (date.after(nowTime)){
                    ToastUtils.showShort(getActivity(), "选择日期不能晚于今天！");
                    return;
                }
                if (timeSelectType==0){
                    startTime.setText(DateUtil.getTime(date));
                }else if(timeSelectType==1){
                    endTime.setText(DateUtil.getTime(date));
                }
            }
        });

        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelectType=0;
                pvTime.setTime(TimePickerView.Type.YEAR_MONTH_DAY,DateUtil.getDate(startTime.getText().toString()));
                pvTime.show();
            }
        });

        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timeSelectType=1;
                pvTime.setTime(TimePickerView.Type.YEAR_MONTH_DAY,DateUtil.getDate(endTime.getText().toString()));
                pvTime.show();
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                missionSearch();
            }
        });

        /**
         * 新任务首次进入列表界面自动请求一次任务列表
         */
        if (MISSIONTYPE == 0 && firstRequest){
            missionSearch();
            firstRequest = false;
        }

    }

    @Override
    public void onRefresh() {
        missionSearch();
    }

    @Override
    public void onLoadMore() {

    }

    /**
     * 加载完成
     */
    private void onLoadComplete(){
        missionList.stopRefresh();
        missionList.stopLoadMore();
        missionList.setRefreshTime(DateUtil.getTime_YMDMS(new Date()));
    }

    public void missionSearch(){

        final MyProgressDialog dialog = new MyProgressDialog(getActivity(), "正在获取案件...");

        MissionFollowListRequest request = new MissionFollowListRequest();
        if (!StringUtils.isEmpty(conditionInput.getText().toString())){
            request.getData().setConditions(conditionInput.getText().toString());
        }
        request.getData().setDisEndTime(endTime.getText().toString());
        request.getData().setDisStartTime(startTime.getText().toString());
        request.getData().setSrvyfaceCde(user.getEmpCde());

        final Gson gson = new Gson();
        final String reqMsg = gson.toJson(request);

        RequestParams params = null;
        if (MISSIONTYPE == 0){
//            params = HttpUtils.buildRequestParam(Constants.CASESEARCH, reqMsg);
            params = HttpClientUtil.getHttpRequestParam(Constants.CASESEARCH, reqMsg);
        }else if (MISSIONTYPE == 1){
//            params = HttpUtils.buildRequestParam(Constants.FINISHTASK, reqMsg);
            params = HttpClientUtil.getHttpRequestParam(Constants.FINISHTASK, reqMsg);
        }

        LogUtils.i("任务列表请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("任务列表返回参数为:", "--------------------------------------------\n" + result);
                MissionFollowListResponse response = gson.fromJson(result, MissionFollowListResponse.class);
                if (response.getSuccess()){
                    if (response.getObj() != null && response.getObj().size() > 0){
                        missions.clear();
                        missions.addAll(response.getObj());
                        adapter.notifyDataSetChanged();
                    }else {
                        missions.clear();
                        adapter.notifyDataSetChanged();
                    }
                }else{
                    ToastUtils.showShort(getActivity(), response.getMsg());
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                LogUtils.i("任务列表请求错误为:", "--------------------------------------------\n" + ex.getMessage());
                ToastUtils.showShort(getActivity(), ex.getLocalizedMessage());
            }

            @Override
            public void onCancelled(CancelledException cex) {
                LogUtils.i("任务列表请求取消为:", "--------------------------------------------\n" + cex.getMessage());
                ToastUtils.showShort(getActivity(), cex.getLocalizedMessage());
            }

            @Override
            public void onFinished() {
                dialog.stopDialog();
                onLoadComplete();
            }
        });

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
