package com.lv.myadview;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.just.agentweb.AgentWeb;
import com.lv.myadview.service.LoopService;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {



    private NetBroadcastReceiver netBroadcastReceiver;

    //private String url ="http://baidu.com";
    //广州市白云区助农服务综合平台
    //private String url ="http://120.78.175.246/apps/#/?boxCode=LZ02-563524";
    //市社大院
    private String url ="http://120.78.175.246/apps/#/?boxCode=LZ02-563524";
    private AgentWeb go;
    private LinearLayout lin_web;
    private RelativeLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
        lin_web = findViewById(R.id.lin_web);
        ll = findViewById(R.id.ll);

        //getWindow().setBackgroundDrawableResource(R.color.colorPrimary);// app主题颜色



        go = AgentWeb.with(this)
                .setAgentWebParent(lin_web, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);



        //注册网络监听广播
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        netBroadcastReceiver = new NetBroadcastReceiver();
        registerReceiver(netBroadcastReceiver,intentFilter);


        Intent intentFour = new Intent(this, com.lv.myadview.service.LoopService.class);
        startService(intentFour);

    }


    @Subscriber(tag = "notify")
    private void setnotify(String nitify) {
            lin_web.setVisibility(View.VISIBLE);
            ll.setVisibility(View.GONE);

            WebView webView = go.getWebCreator().getWebView();
            webView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            WebSettings settings = webView.getSettings();
            settings.setJavaScriptEnabled(true);
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
            webView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    //使用WebView加载显示url
                    view.loadUrl(url);
                    //返回true
                    return true;
                }

                @Override
                public void onReceivedSslError(WebView view,
                                               SslErrorHandler handler, SslError error) {
                    // TODO Auto-generated method stub
                    // handler.cancel();// Android默认的处理方式
                    handler.proceed();// 接受所有网站的证书
                    // handleMessage(Message msg);// 进行其他处理
                }
            });
            webView.reload();
    }



    @Subscriber(tag = "showview")
    private void showView(String status) {
      if ("0".equals(status)){
          lin_web.setVisibility(View.VISIBLE);
          ll.setVisibility(View.GONE);
      }else{
          lin_web.setVisibility(View.GONE);
          ll.setVisibility(View.VISIBLE);
      }
    }





    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        unregisterReceiver(netBroadcastReceiver);
    }
}