package com.estar.hh.survey.view.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.estar.hh.survey.R;


/**
 * Created by Administrator on 2017/7/17.
 */

public class WebViewShowActivity extends HuangheBaseActivity{

    private WebView webView;
    private LinearLayout ll_back;
    private TextView tv_title;
    private String title;
    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_activity);
        initWebView();
    }


    @SuppressLint("JavascriptInterface")
    private void initWebView(){
        url = getIntent().getStringExtra("url");
        title = getIntent().getStringExtra("title");
        webView = findViewById(R.id.main_webview);
        tv_title = findViewById(R.id.tv_title);
        ll_back = findViewById(R.id.ll_back);
        ll_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings setting = webView.getSettings();
        setting.setAllowFileAccess(false);
        setting.setJavaScriptEnabled(true);//支持js
        setting.setDefaultTextEncodingName("GBK");//设置字符编码
        setting.setSupportZoom(true);//支持缩放
        setting.setBuiltInZoomControls(true); //显示放大缩小
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);//解决缓存问题
        setting.setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        webView.addJavascriptInterface(this, "JavaScriptInterface");//添加js交互支持

//        webView.loadUrl("file:///android_asset/page.html");//加载本地页面  //10.0.2.2
//        webView.loadUrl("file:///android_asset/index.html!#");//加载本地页面
//        webView.loadUrl("http://192.168.2.174:8081");//加载网络页面
        webView.loadUrl(url);//加载网络页面
    }

    //重写返回键 使默认返回到网页上一页
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
