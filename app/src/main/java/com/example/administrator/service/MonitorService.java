package com.example.administrator.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.administrator.runforlife.RunActivity;

import java.util.List;

/**
 * Created by quexiaming on 2016/9/7.
 */
public class MonitorService extends Service {

    private static final String SERVICE_NAME="com.baidu.trace.LBSTraceService";

    public static boolean isRunning=false;

    /**
     * 设置监控服务中线程是否走完的标记
     */
    public static boolean isCheck = false;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("MonitorService onCreate",".");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(" onStartCommand",".");
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (isCheck){
                    try {
                        Thread.sleep(5*1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        Log.i("thread sleep failed","是的");
                    }
                }
                if (!isServiceWork(getApplicationContext(),SERVICE_NAME)){
                    //TrackApplication.showMessage("轨迹服务已停止，重启轨迹服务");
                   // Toast.makeText(getApplicationContext(),"轨迹服务已停止，重启轨迹服务",Toast.LENGTH_SHORT).show();
                    if (null != RunActivity.client && null != RunActivity.trace) {
                        RunActivity.client.startTrace(RunActivity.trace);
                    }
                }else {
                  // TrackApplication.showMessage("轨迹服务正在运行");
                   // Toast.makeText(getApplicationContext(),"轨迹服务正在运行",Toast.LENGTH_SHORT).show();
                }


            }
        }).start();



        return super.onStartCommand(intent, flags, startId);
    }


    /**
     * 判断某个服务是否正在运行的方法
     * @param context
     * @param serviceName 是包名+服务的类名（例如：com.baidu.trace.LBSTraceService）
     * @return  true代表正在运行，false代表服务没有正在运行
     */
    public boolean isServiceWork(Context context, String serviceName){
        boolean isWork=false;
        ActivityManager am = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = am.getRunningServices(80);
        if (runningServices.size()<0){
            return  false;
        }
        for (ActivityManager.RunningServiceInfo serInfo:runningServices
                ) {
            if (serInfo.service.getClassName().equals(serviceName)){
                isWork=true;
                break;
            }
        }
        return isWork;
    }

}
