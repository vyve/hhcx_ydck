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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.TimePickerView;
import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.MissionAdapter;
import com.estar.hh.survey.common.UserSharePrefrence;
import com.estar.hh.survey.component.StateSelectDialog;
import com.estar.hh.survey.constants.Constants;
import com.estar.hh.survey.entity.entity.KeyValue;
import com.estar.hh.survey.entity.entity.Mission;
import com.estar.hh.survey.entity.entity.User;
import com.estar.hh.survey.entity.request.MissionListRequest;
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
 * 待提交任务
 */

public class MissionWaitingFragment extends Fragment implements XListView.IXListViewListener{

    private EditText conditionInput;
    private LinearLayout caseStateLayout;
    private TextView caseState;
    private TextView startTime;
    private TextView endTime;
    private ImageView search;
    private XListView missionList;

    private MissionAdapter adapter;
    private List<Mission> missions = new ArrayList<>();

    private int MISSIONTYPE;//判断是哪个状态

    private TimePickerView pvTime;//时间选择器
    private Date nowTime;
    private int timeSelectType = 0;//时间选择判断

    private StateSelectDialog dialog;

    UserSharePrefrence userSharePrefrence;
    User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.mission_list_fragment, container, false);

        initView(contextView);
        initData();

        return contextView;
    }

    private void initView(View view){

        conditionInput = view.findViewById(R.id.mission_list_caseno_search);
        caseStateLayout = view.findViewById(R.id.mission_list_casestep_layout);
        caseState = view.findViewById(R.id.mission_list_casestep_text);
        startTime = view.findViewById(R.id.mission_list_time_start);
        endTime = view.findViewById(R.id.mission_list_time_end);
        search = view.findViewById(R.id.mission_list_search);
        missionList = view.findViewById(R.id.mission_list_list);

        missionList.setPullRefreshEnable(true);
        missionList.setPullLoadEnable(false);
        missionList.setXListViewListener(this);

    }

    private void initData(){

        userSharePrefrence = new UserSharePrefrence(getActivity());
        user = userSharePrefrence.getUserEntity();

        //获取Activity传递过来的参数
        Bundle mBundle = getArguments();
        MISSIONTYPE = mBundle.getInt("missiontype");//1新任务 2待提交 3已提交

        adapter = new MissionAdapter(getActivity(), missions, MISSIONTYPE);
        missionList.setAdapter(adapter);

        final List<KeyValue> dataset = new ArrayList<KeyValue>();
        dataset.add(new KeyValue("全部", "0"));
        dataset.add(new KeyValue("现场查勘", "1"));
        dataset.add(new KeyValue("车辆定损", "2"));
        dataset.add(new KeyValue("财产定损", "3"));

        dialog = new StateSelectDialog(getActivity(), dataset, caseState);

        caseState.setText(dataset.get(0).getKey());
        caseState.setTag(dataset.get(0).getValue());

        caseStateLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });


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

    }

    public void missionSearch(){

        final MyProgressDialog dialog = new MyProgressDialog(getActivity(), "正在获取任务...");

        MissionListRequest request = new MissionListRequest();
        if (!StringUtils.isEmpty(conditionInput.getText().toString())){
            request.getData().setConditions(conditionInput.getText().toString());
        }
        request.getData().setDisEndTime(endTime.getText().toString());
        request.getData().setDisStartTime(startTime.getText().toString());
        request.getData().setSrvyfaceCde(user.getEmpCde());
        request.getData().setSearchType(caseState.getTag().toString());
//        request.getData().setSearchStatus(MISSIONTYPE + "");
        request.getData().setSearchStatus("2");

        final Gson gson = new Gson();
        final String reqMsg = gson.toJson(request);

//        RequestParams params = HttpUtils.buildRequestParam(Constants.MISSIONLIST, reqMsg);
        RequestParams params = HttpClientUtil.getHttpRequestParam(Constants.MISSIONLIST, reqMsg);

        LogUtils.i("任务列表请求参数为:", "-------------------------------------------\n" + reqMsg);

        x.http().post(params, new Callback.CacheCallback<String>() {
            @Override
            public boolean onCache(String result) {
                return false;
            }

            @Override
            public void onSuccess(String result) {
                LogUtils.i("任务列表返回参数为:", "--------------------------------------------\n" + result);
                MissionListResponse response = gson.fromJson(result, MissionListResponse.class);
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


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}
