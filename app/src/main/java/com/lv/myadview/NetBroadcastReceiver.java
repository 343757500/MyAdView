package com.lv.myadview;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import org.simple.eventbus.EventBus;

public class NetBroadcastReceiver extends BroadcastReceiver {


@Override
public void onReceive(Context context, Intent intent) {
    ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    @SuppressLint("MissingPermission") NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
    if(networkInfo!=null && networkInfo.getType()== ConnectivityManager.TYPE_WIFI){
      //  Toast.makeText(context,"now is wifi",Toast.LENGTH_SHORT).show();
      //  EventBus.getDefault().post("","notify");
       // EventBus.getDefault().post("0","showview");
    }
    else if(networkInfo!=null && networkInfo.getType()== ConnectivityManager.TYPE_MOBILE){
      //  Toast.makeText(context,"now is 移动数据",Toast.LENGTH_SHORT).show();
        //更新主页adapter
     //   EventBus.getDefault().post("","notify");
      //  EventBus.getDefault().post("0","showview");
    }
    else{
        //Toast.makeText(context,"没有网络",Toast.LENGTH_SHORT).show();
       // EventBus.getDefault().post("1","showview");
    }

}

}
