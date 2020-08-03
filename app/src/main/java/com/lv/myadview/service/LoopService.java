package com.lv.myadview.service;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.IBinder;

import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.Callback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.simple.eventbus.EventBus;

import java.io.IOException;


public class LoopService extends Service {
  public  static Handler handlerAlive=new Handler();
   public static Runnable runnableAlive=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            permissionAlive();
            handlerAlive.postDelayed(this, 600000);
        }
    };

    public  static Handler handlerAliveOk=new Handler();
    public static Runnable runnableAliveOk=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            OkAlive();
            handlerAliveOk.postDelayed(this, 20000);
        }
    };


    @Override
    public void onCreate() {

    }

    @Nullable
    public IBinder onBind(Intent intent) {
        return null;

    }

    @SuppressLint("WrongConstant")
    public int onStartCommand(Intent intent, int flags, int startId) {
        handlerAlive.postDelayed(runnableAlive, 600000);//每两秒执行一次runnable.
        handlerAliveOk.postDelayed(runnableAliveOk,20000);

        return START_REDELIVER_INTENT;
        /*    1 改动Service的onStartCommand方法中的返回值。onStartCommand方法有三种返回值，依次是
        START_NOT_STICK:
        当Service被异常杀死时。系统不会再去尝试再次启动这个Service
                START_STICKY
        当Service被异常杀死时。系统会再去尝试再次启动这个Service，可是之前的Intent会丢失，也就是在onStartCommand中接收到的Intent会是null
                START_REDELIVER_INTENT
        当Service被异常杀死时，系统会再去尝试再次启动这个Service。而且之前的Intent也会又一次被传给onStartCommand方法
        通过改动onStartCommand方法的返回值这一方法足以解决上面我们提到Service被关闭的第一种情况。 可是对于用户主动强制关闭和三方管理器还是没有效果的
        //return super.onStartCommand(intent, flags, startId);*/
    }


    //是否保活权限
    private static final void permissionAlive() {
        Log.e("LoopService","LoopService");
        EventBus.getDefault().post("","notify");
    }


    //是否保活权限
    private static final void OkAlive() {
        Log.e("LoopService","OkAlive");
        try {
            OkGo.<String>get("https://tcc.taobao.com/cc/json/mobile_tel_segment.htm?tel=123456789")

                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.i("onSuccess", "requestDeviceToken::" + response.body());
                            EventBus.getDefault().post("0","showview");
                        }

                        @Override
                        public void onError(Response<String> response) {
                            Log.i("onError", "requestDeviceToken::" + response.body());
                            EventBus.getDefault().post("1","showview");
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDestroy() {
        Log.e("LoopService","LoopService  --OnDestory");
        stopForeground(true);
        handlerAlive.removeCallbacks(runnableAlive);
        handlerAliveOk.removeCallbacks(runnableAliveOk);
        Intent intent = new Intent("restartService");
        sendBroadcast(intent);
    }






    }

