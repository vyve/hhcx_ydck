package com.estar.hh.survey.view.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.estar.hh.survey.R;
import com.estar.hh.survey.adapter.TaskPlyAdapter;
import com.estar.hh.survey.entity.entity.CopyMainInfoDto;
import com.estar.hh.survey.entity.entity.CopyPolicyDto;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/9/30 0030.
 * 保单信息
 */

public class TaskPlyFragment extends Fragment {

//    private TextView caseNo;
//    private TextView plyNo;
//    private TextView orgName;
//    private TextView className;
//    private TextView caseKind;
//    private TextView caseName;
    private ListView plyList;

    private TaskPlyAdapter adapter;
    private CopyMainInfoDto copyMainInfo = null;
    private List<CopyPolicyDto> plys = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View contextView = inflater.inflate(R.layout.task_ply_fragment_change, container, false);

        initView(contextView);
        initData();

        return contextView;
    }

    private void initView(View view){
        plyList = view.findViewById(R.id.task_ply_list);
//        caseNo = (TextView)view.findViewById(R.id.task_ply_caseno);
//        plyNo = (TextView)view.findViewById(R.id.task_ply_plyno);
//        orgName = (TextView)view.findViewById(R.id.task_ply_orgname);
//        className = (TextView)view.findViewById(R.id.task_ply_classname);
//        caseKind = (TextView)view.findViewById(R.id.task_ply_casekind);
//        caseName = (TextView)view.findViewById(R.id.task_ply_casename);
    }

    private void initData(){

        //获取Activity传递过来的参数
        Bundle mBundle = getArguments();
        copyMainInfo = (CopyMainInfoDto) mBundle.getSerializable("copyMainInfo");
        if (copyMainInfo != null) {
            plys = copyMainInfo.getListPolicy();
        }

        if (plys != null && plys.size() > 0){

            adapter = new TaskPlyAdapter(getActivity(), plys);
            plyList.setAdapter(adapter);

        }


    }
}
