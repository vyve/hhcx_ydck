package com.estar.hh.survey.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.estar.hh.survey.common.ConfigSharePrefrence;
import com.estar.hh.survey.utils.LogUtils;
import com.estar.utils.ToastUtils;

import java.util.Timer;
import java.util.TimerTask;


/**
 * Created by xueliang on 2017/6/27.
 */
public class LocationInfoUploadService extends Service {

    public static final String LOCATION_SERVICE_NAME = "com.estar.hh.survey.service.LocationInfoUploadService";
    private Timer timer = new Timer();
    private LocationClient mLocationClient = null;
    private BDLocation location = null;

    private int delay= 20;//每30秒执行一次推送

    private boolean signStatus;
    private ConfigSharePrefrence configSharePrefrence;

    private Mybinder mybinder = new Mybinder();

    public class Mybinder extends Binder {
        public LocationInfoUploadService getService(){
            return LocationInfoUploadService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        initData();
        initLocation();

        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if (!canUpload()){
                    LogUtils.i("locationUploadService","当前用户无位置上传权限");
                    return;
                }

                if (location==null) {
                    LogUtils.i("locationUploadService","定位信息为空");
                    return ;
                }

                /**
                 * 位置上传
                 */
                LogUtils.i("locationUploadService","位置上传成功");

            }
        },5*000 , delay*1000);//启动该服务3秒后开始上传位置，每隔delay秒执行一次位置上传操作

    }

    private void initData(){
        configSharePrefrence = new ConfigSharePrefrence(this);
    }

    @Override
    public IBinder onBind(Intent intent) {
        mLocationClient.start();
        mLocationClient.requestLocation();
        return mybinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLocationClient.start();
        mLocationClient.requestLocation();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationClient.stop();
        LogUtils.i("locationUploadService", "------------------位置获取服务已被关闭");
        timer.cancel();
        timer = null;
        LogUtils.i("locationUploadService", "------------------定位上传服务线程已被关闭");
    }

    private void initLocation(){
        mLocationClient = new LocationClient(getApplicationContext());
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=10*1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
//        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果(目前这种方式使setScanSpan有效)
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        mLocationClient.registerLocationListener(new MyLocationListener());
    }

    private class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation.getLocType()!= BDLocation.TypeServerError) {
                location = bdLocation;
                LogUtils.i("locationUploadService","定位成功");
                LogUtils.i("locationUploadService","--------------->latitude:"+location.getLatitude() + "    longitude:"+location.getLongitude());
            }else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                //当前缺少定位依据，可能是用户没有授权，建议弹出提示框让用户开启权限
                //可进一步参考onLocDiagnosticMessage中的错误返回码

            }
        }

        /**
         * 回调定位诊断信息，开发者可以根据相关信息解决定位遇到的一些问题
         * 继承BDAbstractLocationListener自动回调，相同的diagnosticType只会回调一次
         *
         * @param locType           当前定位类型
         * @param diagnosticType    诊断类型（1~9）
         * @param diagnosticMessage 具体的诊断信息释义
         */
        public void onLocDiagnosticMessage(int locType, int diagnosticType, String diagnosticMessage) {

            if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_GPS) {
                //建议打开GPS
                ToastUtils.showShort(LocationInfoUploadService.this, "请打开GPS");
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_BETTER_OPEN_WIFI) {
                //建议打开wifi，不必连接，这样有助于提高网络定位精度！
                ToastUtils.showShort(LocationInfoUploadService.this, "建议打开wifi，不必连接，这样有助于提高网络定位精度！");
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_LOC_PERMISSION) {
                //定位权限受限，建议提示用户授予APP定位权限！
                ToastUtils.showShort(LocationInfoUploadService.this, "定位权限受限，建议提示用户授予APP定位权限！");
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CHECK_NET) {
                //网络异常造成定位失败，建议用户确认网络状态是否异常！
                ToastUtils.showShort(LocationInfoUploadService.this, "网络异常造成定位失败，建议用户确认网络状态是否异常！");
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_CLOSE_FLYMODE) {
                //手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！
                ToastUtils.showShort(LocationInfoUploadService.this, "手机飞行模式造成定位失败，建议用户关闭飞行模式后再重试定位！");
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_INSERT_SIMCARD_OR_OPEN_WIFI) {
                //无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！
                ToastUtils.showShort(LocationInfoUploadService.this, "无法获取任何定位依据，建议用户打开wifi或者插入sim卡重试！");
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_NEED_OPEN_PHONE_LOC_SWITCH) {
                //无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！
                ToastUtils.showShort(LocationInfoUploadService.this, "无法获取有效定位依据，建议用户打开手机设置里的定位开关后重试！");
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_SERVER_FAIL) {
                //百度定位服务端定位失败
                //建议反馈location.getLocationID()和大体定位时间到loc-bugs@baidu.com
                ToastUtils.showShort(LocationInfoUploadService.this, "百度定位服务端定位失败！");
            } else if (diagnosticType == LocationClient.LOC_DIAGNOSTIC_TYPE_FAIL_UNKNOWN) {
                //无法获取有效定位依据，但无法确定具体原因
                //建议检查是否有安全软件屏蔽相关定位权限
                //或调用LocationClient.restart()重新启动后重试！
                ToastUtils.showShort(LocationInfoUploadService.this, "建议检查是否有安全软件屏蔽相关定位权限！");
            }
        }
    }

    /**
     * 判断是否可以上传
     *
     * @return
     */
    private boolean canUpload() {

//        signStatus = locPrefer.getString("signStatus", "");
//
//        if ("0".equals(dataSources)) {
//            if (authenState.equals("1") && !rolecode.equals("6")) {
//                return true;
//            }
//        } else if ("1".equals(dataSources)) {
//            if ("1".equals(signStatus)) {
//                return true;
//            }
//        }
//        return false;

        signStatus = configSharePrefrence.getBoolean(ConfigSharePrefrence.SIGNIN);
        return signStatus;
    }

}
