package com.zwf.broadcastdemo;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {
    private IntentFilter intentFilter;
    private NetWorkBroadCastReceiver netWorkBroadCastReceiver;

    private Button broadCastBtn;
    private Button orderCastBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //动态注册消息接听
        intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        netWorkBroadCastReceiver = new NetWorkBroadCastReceiver();
        registerReceiver(netWorkBroadCastReceiver, intentFilter);


        //发送标准广播（顺序播放）
        broadCastBtn = (Button) findViewById(R.id.brodcast_btn);
        broadCastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.zwf.broadcastdemo.MY_BROADCAST");
                sendBroadcast(intent);
            }
        });

        //发送有序广播（第一个接受者接收到之后，后面的接受者才能接收，根据优先级来）
        orderCastBtn = (Button) findViewById(R.id.orderedcast_btn);
        orderCastBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent("com.zwf.broadcastdemo.MY_ODERED_BROADCAST");
                sendOrderedBroadcast(intent, null);

            }
        });



    }

    class NetWorkBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

            if (networkInfo != null && networkInfo.isAvailable()){
                Toast.makeText(context, "network valible!", Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context, "network unvalible!", Toast.LENGTH_LONG).show();
            }

        }
    }


    public class OrderBroadCastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            //这样会终止这个广播
            abortBroadcast();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //动态注册的广播接收器要取消才行
        unregisterReceiver(netWorkBroadCastReceiver);
    }
}
