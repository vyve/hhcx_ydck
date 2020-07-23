package com.estar.hh.survey.view.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.estar.hh.survey.R;
import com.estar.hh.survey.view.fragment.MissionCompleteFragment;
import com.estar.hh.survey.view.fragment.MissionFragment;
import com.estar.hh.survey.view.fragment.MissionWaitingFragment;
import com.estar.utils.ToastUtils;
import com.viewpagerindicator.TabPageIndicator;
import com.viewpagerindicator.UnderlinePageIndicator;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Administrator on 2017/9/28 0028.
 * 任务列表
 */

public class MissionListActivity extends FragmentActivity {

    @ViewInject(R.id.mission_list_back)
    private LinearLayout back;

    @ViewInject(R.id.mission_list_indicator)
    private TabPageIndicator indicator;

    @ViewInject(R.id.mission_list_indicator_underline)
    private UnderlinePageIndicator indicatorUnderline;

    @ViewInject(R.id.mission_list_pager)
    private ViewPager pager;

    private TabPageIndicatorAdapter adapter;

    /**
     * Tab标题
     */
    private static final String[] INDICATORTITLE = new String[] { "新任务", "待提交", "已提交" };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mission_list);
        x.view().inject(this);

        initView();

    }

    private void initView(){

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //ViewPager的adapter
        adapter = new TabPageIndicatorAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);

        indicatorUnderline.setViewPager(pager);
        indicatorUnderline.setFades(false);

        //实例化TabPageIndicator然后设置ViewPager与之关联
        indicator.setViewPager(pager);
        indicator.setOnPageChangeListener(indicatorUnderline);


        //如果我们要对ViewPager设置监听，用indicator设置就行了
        indicatorUnderline.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                if (arg0 == 0){// 若为新任务列表
                    ((MissionFragment)adapter.getMissionNew()).missionSearch();
                }else if (arg0 == 1){// 若为待提交任务列表
                    ((MissionFragment)adapter.getMissionWating()).missionSearch();
                }else if (arg0 == 2){// 若为已完成任务列表
                    ((MissionFragment)adapter.getMissionComplete()).missionSearch();
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });

    }




    /**
     * ViewPager适配器
     * @author len
     *
     */
    private class TabPageIndicatorAdapter extends FragmentPagerAdapter {

        private Fragment missionNew;
        private Fragment missionWating;
        private Fragment missionComplete;

        public Fragment getMissionNew() {
            return missionNew;
        }

        public Fragment getMissionComplete() {
            return missionComplete;
        }

        public Fragment getMissionWating() {
            return missionWating;
        }

        public TabPageIndicatorAdapter(FragmentManager fm) {
            super(fm);
            missionNew = new MissionFragment();
            missionWating = new MissionFragment();
            missionComplete = new MissionFragment();
        }

        @Override
        public Fragment getItem(int position) {

            Fragment fragment = null;

            switch (position){
                case 0:{
                    fragment = missionNew;
                }break;
                case 1:{
                    fragment = missionWating;
                }break;
                case 2:{
                    fragment = missionComplete;
                }break;
                default:break;
            }

            Bundle args = new Bundle();
            args.putInt("missiontype", position + 1);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return INDICATORTITLE[position % INDICATORTITLE.length];
        }

        @Override
        public int getCount() {
            return INDICATORTITLE.length;
        }
    }

}
