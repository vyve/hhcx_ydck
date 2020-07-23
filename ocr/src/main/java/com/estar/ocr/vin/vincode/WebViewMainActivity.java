package com.estar.ocr.vin.vincode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.estar.ocr.R;
import com.estar.ocr.backcard.bankcode.NewBackOcrActivity;


/**
 * Created by Administrator on 2017/7/17.
 */

public class WebViewMainActivity extends Activity{

    private WebView webView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_main_activity);
        initWebView();
    }

    private void initWebView(){

        webView = findViewById(R.id.main_webview);

        webView.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings setting = webView.getSettings();
        setting.setJavaScriptEnabled(true);//支持js
        setting.setDefaultTextEncodingName("GBK");//设置字符编码
        setting.setSupportZoom(true);//支持缩放
        setting.setAllowFileAccess(false);
        setting.setBuiltInZoomControls(true); //显示放大缩小
        setting.setCacheMode(WebSettings.LOAD_NO_CACHE);//解决缓存问题
        setting.setLoadWithOverviewMode(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);//滚动条风格，为0指滚动条不占用空间，直接覆盖在网页上
        webView.addJavascriptInterface(this, "JavaScriptInterface");//添加js交互支持

        webView.loadUrl("file:///android_asset/page.html");//加载本地页面  //10.0.2.2
//        webView.loadUrl("file:///android_asset/index.html!#");//加载本地页面
//        webView.loadUrl("http://192.168.2.174:8081");//加载网络页面
//        webView.loadUrl("http://www.baidu.com");//加载网络页面
    }

    //重写返回键 使默认返回到网页上一页
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @JavascriptInterface
    public void toActivity() {
        //此处应该定义常量对应，同时提供给web页面编写者
        Intent intent = new Intent(WebViewMainActivity.this, NewBackOcrActivity.class);
        startActivity(intent);
    }

}
