package com.estar.hh.survey.view.activity;

import android.os.Bundle;

import com.baidu.mapapi.map.MapView;
import com.estar.hh.survey.R;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

public class MainActivity extends HuangheBaseActivity {

    @ViewInject(R.id.mapview)
    private MapView mMapView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        mMapView.getMap();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
//        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
//        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        mMapView.onPause();
    }

}
