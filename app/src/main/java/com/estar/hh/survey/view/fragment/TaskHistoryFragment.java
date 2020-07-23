package com.estar.hh.survey.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.TaskHistoryAdapter;
import com.estar.hh.survey.adapter.TaskPlyAdapter;
import com.estar.hh.survey.adapter.TaskRiskAdapter;
import com.estar.hh.survey.entity.entity.CopyHisDangerDto;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/30 0030.
 * 历史赔案
 */

public class TaskHistoryFragment extends Fragment {

    private ListView historyList;

    private TaskHistoryAdapter adapter;
    private CopyMainInfoDto copyMainInfo = null;
    private List<CopyHisDangerDto> risks = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.task_history_fragment, container, false);

        initView(contextView);
        initData();

        return contextView;
    }

    private void initView(View view){
        historyList = view.findViewById(R.id.task_history_list);
    }

    private void initData(){

        //获取Activity传递过来的参数
        Bundle mBundle = getArguments();
        copyMainInfo = (CopyMainInfoDto) mBundle.getSerializable("copyMainInfo");
        if (copyMainInfo != null) {
            risks = copyMainInfo.getListHisDanger();
        }

        if (risks != null && risks.size() > 0){

            adapter = new TaskHistoryAdapter(getActivity(), risks);
            historyList.setAdapter(adapter);

        }

    }

}
