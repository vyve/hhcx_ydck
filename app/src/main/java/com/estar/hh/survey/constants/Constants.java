package com.estar.hh.survey.constants;

import java.text.SimpleDateFormat;

/**
 * Created by Administrator on 2017/8/1.
 * 配置信息
 */

public class Constants {
    public static final boolean LOGWITCH = true;//日志功能控制开关
    public static final boolean LOGWITCHTS = false;//特殊日志功能控制开关
    public static final boolean LOGWITCHTSS = false;//拍照日志功能控制开关

    /**
     * 任务类型
     */
    public static final String SURVEY_TYPE = "survey";//查勘任务
    public static final String CARLOSS_TYPE = "carloss";//车辆定损
    public static final String GOODLOSS_TYPE = "goodloss";//物损定损

    /**
     * 网络地址信息
     */
//    public static final String VERSION_DIF = "TEST_";// TEST版本标志
    public static final String VERSION_DIF = "UAT_";// UAT版本标志
//    public static final String VERSION_DIF = "PREP_";// PREP版本标志
//    public static final String VERSION_DIF = "PROD_";// 生产版本标志

        public static final String BASE_IP = "http://www.51clm.com/hhcx/uat/estar-pic-dev";//新增测试地址
//        public static final String BASE_IP = "http://223.71.98.124:10087/estar-pic-dev";//新增测试地址
//        public static final String BASE_IP = "http://223.71.98.124:10087/estar-pic-yr";//UAT地址
//    public static final String BASE_IP = "http://mobilecla.ypic.cn";//生产地址
//    public static final String BASE_IP = "http://111.203.168.212:10087";//预生产地址
//    public static final String BASE_IP = "http://10.1.26.43:8087";//预生产内网

    public static final String BASE_URL = "/survey/claimDataController.do?method=esData&esType=";
    public static final String BASE_IMG_URL = "/survey/imageDataController.do?method=esData&esType=";//图片上传接口
    public static final String REQUEST_URL = BASE_IP + BASE_URL;



    public static final String REQUEST_URL2 = "http://223.71.98.124:10087/estar-pic-yr/sales/priceDataController.do?method=esData&esType=";//UAT版本更新地址
//    public static final String REQUEST_URL2 = "http://10.1.26.44:8086/sales/priceDataController.do?method=esData&esType=";//预生产版本更新地址
//    public static final String REQUEST_URL2 = "http://mobilesal.ypic.cn/sales/priceDataController.do?method=esData&esType=";//生产环境版本更新地址

    /**
     * 精友下载地址
     */
    public static final String JY_DOWNLOAD = "https://rtv.lexiugo.com/app-upgrade/downloadFile/downloadMobile.do?id=79dc8969e8cc4469994a645a0e1bafc6";//UAT+预生产环境
//    public static final String JY_DOWNLOAD = "https://rtv.lexiugo.com/app-upgrade/downloadFile/downloadMobile.do?id=2d2d62439b77493a90f380f073db5db8";//生产环境


    /**
     * 视频相关地址
     */
    public static final String VIDEO_IP = "223.71.98.124";//UAT视频IP
    public static final int VIDEO_PORT = 8000;// UAT测试视频端口
//    public static final String VIDEO_IP = "mobilecla.ypic.cn";//生产视频IP
//    public static final int VIDEO_PORT = 7998;// 生产视频端口
//    public static final String VIDEO_IP = "111.203.168.212" ;//预生产视频IP
//    public static final int VIDEO_PORT = 10087;// 预生产视频端口
    public static final String NetworkSpeedActivity_Url = "http://124.172.245.189:8068/QHMobile/apk/VNC.jar";//网速测试


    /**
     * 接口信息
     */
    public static final String VERSION_UPDATE = REQUEST_URL2 + "appVersion";//版本更新
    public static final String LOGIN = REQUEST_URL + "mobileUserLogin";//登录
    public static final String REGIST = REQUEST_URL + "mobileUserBind";//用户绑定
    public static final String MODIFYPASS = REQUEST_URL + "updateSurvyPwd";//修改密码
    public static final String CHANGGEPHONE = REQUEST_URL + "updateSurvyTel";//改绑手机号
    public static final String SAVEMYDATA = BASE_IP + BASE_IMG_URL + "selfUpload";//修改个人资料

    public static final String SIGN = REQUEST_URL + "mobileUserSign";//考勤
    public static final String MISSIONLIST = REQUEST_URL + "mobileNewTask";//任务列表
    public static final String COPYINFO = REQUEST_URL + "getCopyMain";//获取抄单
    public static final String TASKREAD = REQUEST_URL + "taskStepOpe";//阅读任务
    public static final String CASESEARCH = REQUEST_URL + "caseSearch";//案件查询
    public static final String FINISHTASK = REQUEST_URL + "finishTask";//已完成
    public static final String SYNCHSURVEY = REQUEST_URL + "synchSurvey";//查勘案件暂存
    public static final String SYNCHCARLOSS = REQUEST_URL + "synchCarLoss";//车损案件暂存
    public static final String SYNCHPROPLOSS = REQUEST_URL + "synchPropLoss";//物损案件暂存
    public static final String SUBMITSURVEY = REQUEST_URL + "submitSurvey";//查勘案件提交
    public static final String SUBMITCARLOSS = REQUEST_URL + "submitCarLoss";//车损案件提交
    public static final String SUBMITPROPLOSS = REQUEST_URL + "submitPropLoss";//物损案件提交

    public static final String IMAGEUPLOAD = BASE_IP + BASE_IMG_URL + "uploadImg";//图片上传
    public static final String MESSAGESEARCH = REQUEST_URL + "noticeList";//消息列表

    public static final String LEAVEWORD = REQUEST_URL + "reportRemark";//备注提交
    public static final String WORDLIST = REQUEST_URL + "reportRemarkList";//备注列表
    public static final String JYLOSSNO = REQUEST_URL + "getLossItemNo";//获取精友定损单号

    public static final String VIDEOCREATE = REQUEST_URL + "toCreateTask";//视频连接
    public static final String LOGUPLOAD = BASE_IP + BASE_IMG_URL + "uploadLog";//日志上传接口

    public static final String SERVICE_ANDPROVISION = BASE_IP + "/agreements/app/serviceAndprovision.html";//隐私协议

}
