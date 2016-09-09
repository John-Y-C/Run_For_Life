package com.example.administrator.runforlife;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.location.Poi;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.CoordinateConverter;
import com.baidu.trace.LBSTraceClient;
import com.baidu.trace.LocationMode;
import com.baidu.trace.OnEntityListener;
import com.baidu.trace.OnStartTraceListener;
import com.baidu.trace.OnStopTraceListener;
import com.baidu.trace.OnTrackListener;
import com.baidu.trace.Trace;
import com.baidu.trace.TraceLocation;
import com.example.administrator.service.MonitorService;
import com.example.administrator.utils.ConstantValue;
import com.example.administrator.utils.SpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import at.markushi.ui.CircleButton;

public class RunActivity extends AppCompatActivity implements View.OnClickListener{


    //fragment里面的参数
    /**
     * 采集周期（单位 : 秒）
     */
    private int gatherInterval = 1;

    /**
     * 打包周期（单位 : 秒）
     */
    private int packInterval = 1;
    private RefreshThread refreshThread;
    private ArrayList<LatLng> pointList=new ArrayList<>();
    private MapStatusUpdate msUpdate;
    private BitmapDescriptor realtimeBitmap;
    private MarkerOptions overlayOptions;
    private PolylineOptions polylineOptions;
    private Intent serviceIntent;




    public OnTrackListener trackListener = null;
    public Context mContext;
    public static LBSTraceClient client;
    public  String entityName;
    private OnStartTraceListener onStartTraceListener;
    private OnStopTraceListener onStopTraceListener;
    /**
     * 鹰眼服务ID，开发者创建的鹰眼服务对应的服务ID
     */
    public static long serviceId = 124824;
    /**
     * 轨迹服务类型（0 : 不建立socket长连接， 1 : 建立socket长连接但不上传位置数据，2 : 建立socket长连接并上传位置数据）
     */
    private int traceType = 2;
    public static Trace trace;
    private CircleButton btn_startRun;
    private CircleButton btn_stopRun;


    private MapView bmapView;
    public BaiduMap mapController;
    public OnEntityListener onEntityListener;//Entity监听器
    private int startTime;
    private int endTime;
    private TextView tv_distance;
    private TextView tv_speed;
    private Double distanceSum=0d;
    private TextView tv_latLon;
    private TextView tv_latLon2;
    private TextView btn_time;
    private int measuredHeight;
    private int sec;
    private int min;
    private int hour;
    private Timer timer;
    private TimerTask timerTask;
    private TextView tv_direction;
    private boolean isTimingOut;


    public class MyLocationListener implements BDLocationListener {

        /**
         * 在这个方法里面接收定位结果
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
          /*  if (location!=null){
                MyLocationData.Builder builder = new MyLocationData.Builder();
                builder.accuracy(location.getRadius());//设置精度
                builder.direction(location.getDirection());//设置方向
                builder.latitude(location.getLatitude());//设置纬度
                builder.longitude(location.getLongitude());//设置进度
                MyLocationData locationData = builder.build();
                mapController.setMyLocationData(locationData);//把定位数据显示到地图上
            }*/
            Log.i("错误码",location.getLocType()+"");

            //Receive Location
            StringBuffer sb = new StringBuffer(256);
            sb.append("time : ");
            sb.append(location.getTime());
            sb.append("\nerror code : ");
            sb.append(location.getLocType());
            sb.append("\nlatitude : ");
            sb.append(location.getLatitude());
            sb.append("\nlontitude : ");
            sb.append(location.getLongitude());
            sb.append("\nradius : ");
            sb.append(location.getRadius());
            if (location.getLocType() == BDLocation.TypeGpsLocation) {// GPS定位结果
                sb.append("\nspeed : ");
                sb.append(location.getSpeed());// 单位：公里每小时
                sb.append("\nsatellite : ");
                sb.append(location.getSatelliteNumber());
                sb.append("\nheight : ");
                sb.append(location.getAltitude());// 单位：米
                sb.append("\ndirection : ");
                sb.append(location.getDirection());// 单位度
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                sb.append("\ndescribe : ");
                sb.append("gps定位成功");

            } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {// 网络定位结果
                sb.append("\naddr : ");
                sb.append(location.getAddrStr());
                //运营商信息
                sb.append("\noperationers : ");
                sb.append(location.getOperators());
                sb.append("\ndescribe : ");
                sb.append("网络定位成功");
            } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {// 离线定位结果
                sb.append("\ndescribe : ");
                sb.append("离线定位成功，离线定位结果也是有效的");
            } else if (location.getLocType() == BDLocation.TypeServerError) {
                sb.append("\ndescribe : ");
                sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");
            } else if (location.getLocType() == BDLocation.TypeNetWorkException) {
                sb.append("\ndescribe : ");
                sb.append("网络不同导致定位失败，请检查网络是否通畅");
            } else if (location.getLocType() == BDLocation.TypeCriteriaException) {
                sb.append("\ndescribe : ");
                sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");
            }
            sb.append("\nlocationdescribe : ");
            sb.append(location.getLocationDescribe());// 位置语义化信息
            List<Poi> list = location.getPoiList();// POI数据
            if (list != null) {
                sb.append("\npoilist size = : ");
                sb.append(list.size());
                for (Poi p : list) {
                    sb.append("\npoi= : ");
                    sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
                }
            }
            Log.i("BaiduLocationApiDem", sb.toString());
            tv_title_bar.setText(location.getCity()+location.getDistrict()+location.getStreet()+location.getLocationDescribe());
            DecimalFormat df = new DecimalFormat("#.0");
            tv_speed.setText("速度："+df.format(location.getSpeed())+"km/h");
            tv_direction.setText("方位角："+location.getDirection()+"度");
        }
    }

    public BDLocationListener myListener = new MyLocationListener();
    public LocationClient mLocationClient = null;
    private TextView tv_title_bar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        SDKInitializer.initialize(getApplicationContext());
        //requestWindowFeature(Window.FEATURE_NO_TITLE);//去除标题
        setContentView(R.layout.activity_run);
        getSupportActionBar().hide();
        mContext = getApplicationContext();

        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener( myListener );    //注册监听函数
        initLocation();
        mLocationClient.start();//开始定位



        //初始化轨迹服务客户端
        client = new LBSTraceClient(mContext);
        client.setLocationMode(LocationMode.High_Accuracy);//设置定位模式
        entityName = "myTrace";
        //初始化轨迹服务
        trace = new Trace(mContext, serviceId, entityName, traceType);

        //初始化组件
        initComponent();
        // 初始化EntityListener
        initOnEntityListener();
        // 初始化OnTrackListener
        initOnTrackListener();
        //初始化轨迹监听器
        initTraceListener();
        // 设置采集周期
        setInterval();
        // 设置http请求协议类型
        setRequestType();

    }

    private void initLocation() {
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy
        );//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span=1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
    }

    private void initOnTrackListener() {

        trackListener = new OnTrackListener() {

            // 请求失败回调接口
            @Override
            public void onRequestFailedCallback(String arg0) {
                // TODO Auto-generated method stub
                Toast.makeText(mContext,"track请求失败回调接口消息 : " + arg0,Toast.LENGTH_SHORT).show();
            }

            // 查询历史轨迹回调接口
            @Override
            public void onQueryHistoryTrackCallback(String arg0) {
                // TODO Auto-generated method stub
                super.onQueryHistoryTrackCallback(arg0);
            }

            @Override
            public void onQueryDistanceCallback(String arg0) {
                // TODO Auto-generated method stub
                try {
                    JSONObject dataJson = new JSONObject(arg0);
                    if (null != dataJson && dataJson.has("status") && dataJson.getInt("status") == 0) {
                        double distance = dataJson.getDouble("distance");
                        DecimalFormat df = new DecimalFormat("0.0");
                        final String licheng = df.format(distance);
                        //   Toast.makeText(mContext,"里程 : " + df.format(distance) + "米",Toast.LENGTH_SHORT).show();
                        Log.i("里程","里程 : " +licheng+ "m");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tv_distance.setText("里程："+licheng+"米");
                            }
                        });
                    }
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    //   Toast.makeText(mContext,"queryDistance回调消息 : " + arg0,Toast.LENGTH_SHORT).show();
                    Log.i("里程","queryDistance回调消息 : ");
                }

                System.out.print("queryDistance回调消息 : " + arg0);
            }

            @Override
            public Map<String, String> onTrackAttrCallback() {
                // TODO Auto-generated method stub
                System.out.println("onTrackAttrCallback");
                System.out.println("startTime:"+startTime+",endTime:"+endTime);
                return null;
            }

        };
    }



    private void setRequestType() {
        client.setInterval(gatherInterval, packInterval);
    }

    private void setInterval() {
        int type = 0;
        client.setProtocolType(type);
    }

    private void startRefreshThread(boolean isStart) {
        if (null == refreshThread) {
            refreshThread = new RefreshThread();
        }
        refreshThread.refresh = isStart;//控制线程走没走完的
        if (isStart) {
            if (!refreshThread.isAlive()) {
                refreshThread.start();
            }
        } else {
            refreshThread = null;
        }


    }
    public class RefreshThread extends Thread{
        public boolean refresh=true;//控制线程走没走完的

        @Override
        public void run() {
            Looper.prepare();//这个到底有什么用
            while (refresh){
                //查询实时位置
                queryRealTimeLoc();
                //每隔一个采集周期查询一次？
                //queryEntityList();
                endTime= (int) (System.currentTimeMillis()/1000);
                queryDistance(0,null);
                queryEntityList();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
            Looper.loop();
        }
    }


    // 查询里程
    private void queryDistance(int processed, String processOption) {

        // entity标识
        String entityName =this.entityName;

        // 是否返回纠偏后轨迹（0 : 否，1 : 是）
        int isProcessed = processed;

        // 里程补充
        String supplementMode = "walking";
        client.queryDistance(serviceId, entityName, isProcessed, processOption,
                supplementMode, startTime, endTime, trackListener);
    }
    //查询实时位置
    private void queryRealTimeLoc() {
        //  该方法用于单独发起一次即时定位，返回为当前终端所在位置，类似定位SDK的使用方法，与轨迹采集上传完全独立，互不关联
        client.queryRealtimeLoc(serviceId, onEntityListener);
    }


    private void initTraceListener() {
        // 初始化开启轨迹服务监听器
        initOnStartTraceListener();

        // 初始化停止轨迹服务监听器
        initOnStopTraceListener();
    }

    private void initOnStopTraceListener() {
        onStopTraceListener = new OnStopTraceListener() {
            @Override
            public void onStopTraceSuccess() {
            }

            @Override
            public void onStopTraceFailed(int i, String s) {

            }
        };
    }

    private void initOnStartTraceListener() {
        // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
// 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
        // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
// 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
        onStartTraceListener = new OnStartTraceListener() {

            // 开启轨迹服务回调接口（arg0 : 消息编码，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTraceCallback(int i, String s) {

            }

            // 轨迹服务推送接口（用于接收服务端推送消息，arg0 : 消息类型，arg1 : 消息内容，详情查看类参考）
            @Override
            public void onTracePushCallback(byte b, String s) {

            }
        };
    }









    private void initComponent() {
        tv_direction = (TextView) findViewById(R.id.tv_direction);
        tv_title_bar = (TextView) findViewById(R.id.tv_title_bar);


        btn_time = (TextView) findViewById(R.id.btn_time);
        //测量下
        btn_time.measure(0,0);
        measuredHeight = btn_time.getMeasuredHeight();
        btn_time.setPadding(0,0,0,-measuredHeight);


        tv_latLon2 = (TextView) findViewById(R.id.tv_LatLon2);
        tv_latLon = (TextView) findViewById(R.id.tv_LatLon);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_speed = (TextView) findViewById(R.id.tv_speed);

        btn_startRun = (CircleButton) findViewById(R.id.btn_startRun);
        btn_stopRun = (CircleButton) findViewById(R.id.btn_stopRun);
        btn_startRun.setOnClickListener(this);
        btn_stopRun.setOnClickListener(this);

        bmapView = (MapView) findViewById(R.id.bmapView);

        mapController = bmapView.getMap();//获取地图控制器
        bmapView.showZoomControls(false);
        //显示指南针
        UiSettings uiSettings = mapController.getUiSettings();
        uiSettings.setCompassEnabled(true);
    }

    private void initOnEntityListener() {
        // 查询entity列表回调接口
// 添加entity回调接口
        onEntityListener = new OnEntityListener() {
            @Override
            public void onRequestFailedCallback(String s) {

            }

            @Override
            public void onReceiveLocation(final TraceLocation traceLocation) {

                showRealtimeTrack(traceLocation);
                Log.i("位置:" ,traceLocation.toString());

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        tv_latLon.setText("纬度："+traceLocation.getLatitude()+"，经度："+traceLocation.getLongitude());
                        /*   DecimalFormat df = new DecimalFormat("#.0");//0 一个数字# 一个数字，不包括 0
                           String format = df.format(DistanceUtil.getDistance(new LatLng(22.671638, 114.046226), new LatLng(22.669828, 114.043019)));
                           btn_time.setText(format);
                           Log.i("位置:" ,"format=:"+format);*/
                    }
                });
                Log.i("位置:" ,"经纬度:"+traceLocation.getLatitude()+","+traceLocation.getLongitude()+"速度："+ traceLocation.getSpeed());




            }


            // 查询entity列表回调接口
            @Override
            public void onQueryEntityListCallback(String s) {
                super.onQueryEntityListCallback(s);

                // Toast.makeText(mContext,"onQueryEntityListCallback"+s,Toast.LENGTH_SHORT).show();
                Log.i("QueryEntityListCallback",s);
            }

            @Override
            public void onUpdateEntityCallback(String s) {

                super.onUpdateEntityCallback(s);
                // Toast.makeText(mContext,"onUpdateEntityCallback"+s,Toast.LENGTH_SHORT).show();
            }

            // 添加entity回调接口
            @Override
            public void onAddEntityCallback(String s) {
                super.onAddEntityCallback(s);
            }
        };
    }

    private void showRealtimeTrack(TraceLocation location) {

        if (null == refreshThread || !refreshThread.refresh) {
            return;
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

        if (Math.abs(latitude - 0.0) < 0.000001 && Math.abs(longitude - 0.0) < 0.000001) {
            // Toast.makeText(mContext,"当前查询无轨迹点",Toast.LENGTH_SHORT).show();
        }else {
            LatLng latLng = new LatLng(latitude, longitude);
            //这个坐标需要一些处理吧
            if (location.getCoordType()==1){
                LatLng sourceLatLng = latLng;
                CoordinateConverter converter = new
                        CoordinateConverter();
                converter.from(CoordinateConverter.CoordType.GPS);
                converter.coord(sourceLatLng);
                latLng = converter.convert();
            }


            pointList.add(latLng);

            // 绘制实时点
            drawRealtimePoint(latLng);

        }
    }

    private void drawRealtimePoint(LatLng point) {

        MapStatus mMapStatus=new MapStatus.Builder().target(point).zoom(19).build();
        msUpdate = MapStatusUpdateFactory.newMapStatus(mMapStatus);

        //初始化图片数据
        if (null == realtimeBitmap) {
            realtimeBitmap = BitmapDescriptorFactory
                    .fromResource(R.mipmap.icon_geo);
        }
        overlayOptions = new MarkerOptions().draggable(true).zIndex(9).position(point).icon(realtimeBitmap);


        //int size = pointList.size();
        if (pointList.size()>=2&&pointList.size()<=10000){
            polylineOptions = new PolylineOptions().width(10).color(Color.RED).points(pointList);
    /*        //计算里程
         for (int i=0;i<size;i++){
             if (i!=0){
                 double distance = DistanceUtil.getDistance(pointList.get(i - 1), pointList.get(i));
                 distanceSum+=distance;
             }
         }*/
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv_latLon2.setText("采集坐标点个数："+pointList.size());
            }
        });
        //tv_latLon2.setText("前一个点经纬度:"+pointList.get(size-2).latitude+","+pointList.get(size-2).longitude+",点的个数为"+pointList.size());
//        DecimalFormat df = new DecimalFormat("#.0");
//        tv_distance.setText("里程:"+df.format(distanceSum));
        addMarker();
    }

    private void addMarker() {

        if (msUpdate!=null){
            mapController.setMapStatus(msUpdate);

        }
        if (polylineOptions!=null){
            mapController.addOverlay(polylineOptions);
        }
        if (overlayOptions!=null){

            mapController.addOverlay(overlayOptions);
        }
    }

    @Override
    public void onClick(View v) {
        handleButtonCliick(v.getId());
    }

    private void handleButtonCliick(int id) {
        switch (id){
            case R.id.btn_startRun:
                if (isTimingOut==false){
                    startTimeAnimation();
                }
                Toast.makeText(mContext, "正在开启轨迹服务，请稍候", Toast.LENGTH_LONG).show();
                startTime= (int) (System.currentTimeMillis()/1000-10);
                startTrace();
                //startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
                startRefreshThread(true);
                break;

                case R.id.btn_stopRun:
                if (isTimingOut==true){
                    stopTimeAnimation();
                }
                endTiming();

                stopTrace();
                startRefreshThread(false);

               //存储总锻炼时间并展示
               showTotalTime();


                //存储并展示总里程
                showTotalDistance();
                Toast.makeText(mContext, "轨迹服务已停止", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void showTotalDistance() {
        float preTotalDistance = SpUtils.getFloat(mContext, ConstantValue.TOTAL_DISTANCE, 0.0f);
        String distanceText = tv_distance.getText().toString();
        String substring = distanceText.substring(3, distanceText.length()-1);
        Log.i("distanceText",substring);
        float v = preTotalDistance + Float.parseFloat(substring);
        SpUtils.putIFloat(mContext,ConstantValue.TOTAL_DISTANCE,v);
        Toast.makeText(mContext,"您已累计跑步"+v+"米",Toast.LENGTH_SHORT).show();
    }

    private void showTotalTime() {
        int preTotalTime = SpUtils.getInt(mContext, ConstantValue.TOTAL_TIME, 0);
        String text = btn_time.getText().toString();
        String[] time = text.split("：");
        String[] times = time[1].split(":");
        int secs = preTotalTime+Integer.parseInt(times[0]) * 3600 + Integer.parseInt(times[1]) * 60 + Integer.parseInt(times[2]);
        SpUtils.putInt(mContext,ConstantValue.TOTAL_TIME,secs);
        int i = secs / 3600;
        int i1 = secs % 3600 / 60;
        int i2 = secs % 3600 % 60;
        Toast.makeText(mContext,"您已累计跑步"+i+"小时"+i1+"分"+i2+"秒",Toast.LENGTH_SHORT).show();
    }

    //结束计时
    private void endTiming() {
        if (timer==null){
            timer.cancel();
            timer=null;
        }
    }

    private void stopTimeAnimation() {

        isTimingOut=false;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(0,-measuredHeight);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int  animatedValue = (int) animation.getAnimatedValue();
                btn_time.setPadding(0,0,0,animatedValue);
            }
        });
        valueAnimator.start();
    }

    private void startTimeAnimation() {
        //用于表示跑步时长控件是否上浮的标记
        isTimingOut = true;
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setIntValues(-measuredHeight,0);
        valueAnimator.setDuration(1000);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int  animatedValue = (int) animation.getAnimatedValue();
                btn_time.setPadding(0,0,0,animatedValue);
            }
        });
        valueAnimator.start();
        //设置监听器，动画结束开启秒表
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                beginTiming();
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

    }

    //开始计时
    private void beginTiming() {
        sec = 0;
        min = 0;
        hour = 0;
        if (timerTask==null){
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    sec = sec +1;
                    if (sec==60){
                        sec=0;
                        min=min+1;
                        if (min==60){
                            min=0;
                            hour=hour+1;
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            btn_time.setText("跑步时长："+j(hour)+":"+j(min)+":"+j(sec));
                        }
                    });
                }
            };
        }

        if (timer==null){
            timer = new Timer();
            timer.schedule(timerTask,1000,1000);
        }
    }

    private String j(int time) {
        if (time>=10){
            return time+"";
        }else {
            return "0"+time;
        }
    }


    private void stopTrace() {
        // 停止监听service
        MonitorService.isCheck = false;
        MonitorService.isRunning = false;

        client.stopTrace(trace, onStopTraceListener);

        if (null != serviceIntent) {
            stopService(serviceIntent);
        }
    }

    private void startTrace() {
        // 通过轨迹服务客户端client开启轨迹服务
        client.startTrace(trace, onStartTraceListener);

        if (!MonitorService.isRunning) {
            // 开启监听service
            MonitorService.isCheck = true;
            MonitorService.isRunning = true;
            startMonitorService();
        }
    }

    private void startMonitorService() {
        serviceIntent = new Intent(mContext,
                com.example.administrator.service.MonitorService.class);
        startService(serviceIntent);
    }


    //查询服务器端的实时位置
    private void queryEntityList() {
        // // entity标识列表（多个entityName，以英文逗号"," 分割）
        String entityNames = this.entityName;
        // 属性名称（格式为 : "key1=value1,key2=value2,....."）
        String columnKey = "key1=value1,key2=value2";
        // 返回结果的类型（0 : 返回全部结果，1 : 只返回entityName的列表）
        int returnType = 0;
        // 活跃时间（指定该字段时，返回从该时间点之后仍有位置变动的entity的实时点集合）
        // int activeTime = (int) (System.currentTimeMillis() / 1000 - 30);
        int activeTime = 0;
        // 分页大小
        int pageSize = 10;
        // 分页索引
        int pageIndex = 1;
        client.queryEntityList(serviceId, entityNames, columnKey, returnType, activeTime,
                pageSize,
                pageIndex, onEntityListener);
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        client.onDestroy();
        mLocationClient.stop();//停止定位
        //android.os.Process.killProcess(android.os.Process.myPid());
        stopService(new Intent(mContext, com.example.administrator.service.MonitorService.class));
        stopService(new Intent(mContext,com.baidu.trace.LBSTraceService.class));
        stopService(new Intent(mContext,com.baidu.location.f.class));
    }
}
