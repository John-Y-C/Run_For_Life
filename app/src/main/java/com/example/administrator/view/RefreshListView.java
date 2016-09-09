package com.example.administrator.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.administrator.runforlife.R;


public class RefreshListView extends ListView implements AbsListView.OnScrollListener {
    private static final String TAG = "RefreshListView";
    private int measuredHeight;
    private View refreshListviewHeader;
    private ProgressBar pbRefreshlistviewheaderFreshing;
    private TextView tvRefreshfooterGetmoredata;
    private boolean isRefreshable;

    public RefreshListView(Context context) {
        super(context);

        initHeaderView(context);
        initFooterView(context);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);

        initHeaderView(context);
        initFooterView(context);
    }

    private void initHeaderView(Context context) {

        refreshListviewHeader = View.inflate(context, R.layout.refresh_listview_header, null);

        pbRefreshlistviewheaderFreshing = (ProgressBar) refreshListviewHeader.findViewById(R.id.pb_refreshlistviewheader_freshing);

        refreshListviewHeader.measure(0,0);
        measuredHeight = refreshListviewHeader.getMeasuredHeight();
        refreshListviewHeader.setPadding(0,-measuredHeight,0,0);

        // 将下拉刷新的布局加入ListView的顶部
        addHeaderView(refreshListviewHeader);

        // 设置滚动监听事件
        setOnScrollListener(this);

    }



    private void initFooterView(Context context) {

        View refreahListviewFooter = View.inflate(context, R.layout.refresh_listview_footer, null);

        addFooterView(refreahListviewFooter);

        tvRefreshfooterGetmoredata = (TextView) refreahListviewFooter.findViewById(R.id.tv_refreshfooter_getmoredata);

        tvRefreshfooterGetmoredata.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (listener!=null){
                    listener.onLoadMore();
                    tvRefreshfooterGetmoredata.setText("加载中");
                }
            }
        });
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

        Log.i(TAG,"firstVisibleItem:"+firstVisibleItem);
        //只有当listview滑到最上面时才能刷新
        if (firstVisibleItem==0){
            isRefreshable = true;
        }else {
            isRefreshable = false;
        }
    }

    float startX;
    float startY;
    float endX;
    float endY;

    private  final int INITSTATE=0;
    private  final int NEED_RELEASE=1;
    private  final int REFRESHING=2;

    private int current_state=INITSTATE;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (isRefreshable){

            switch (ev.getAction()){
                case MotionEvent.ACTION_DOWN:
                    startX=ev.getRawX();
                    startY=ev.getRawY();
                    Log.i(TAG,"startX="+startX+" startY="+startY);
                    break;

                case MotionEvent.ACTION_MOVE:
                    if (current_state == REFRESHING){
                        break;
                    }

                    if (startX==0) startX= ev.getRawX();
                    if (startY==0) startY= ev.getRawY();

                    Log.i(TAG,"startX="+startX+" startY="+startY);
                    endX= ev.getRawX();
                    endY =ev.getRawY();
                    Log.i(TAG,"endX="+endX+" endY="+endY);
                    float dx =Math.abs(startX-endX);
                    float dy =Math.abs(startY-endY);
                    Log.i(TAG,"dx="+dx+" dy="+dy);

                    if (dx<dy){
                        if (endY>startY){//下滑
                            Log.i(TAG,"下滑");
                            refreshListviewHeader.setPadding(0,-measuredHeight+(int)(dy),0,0);
                            if (-measuredHeight+(int)(dy)>0&&current_state!=NEED_RELEASE){
                                //就已经完全拉出来了。
                                current_state=NEED_RELEASE;
                                Log.i(TAG,"状态变为需要松手");
                            }
                        }
                    }
                    break;

                case  MotionEvent.ACTION_UP:

                    //如果已经完全拉出来了，就刷新
                    if (current_state==NEED_RELEASE){
                        current_state=REFRESHING;
                        Log.i(TAG,"状态变为正在刷新");
                        refreshListviewHeader.setPadding(0,0,0,0);

                        //刷新的代码
                        if (listener!=null){
                            listener.onRefreshing();
                        }
                    }

                    //如果只拉出来一点点，就让他弹回去，恢复到原位（隐藏）
                    if (current_state==INITSTATE){
                        Log.i(TAG,"状态变为初始状态，回到原位");
                        refreshListviewHeader.setPadding(0,-measuredHeight ,0,0);
                    }
                    break;
            }
        }
        return super.onTouchEvent(ev);
    }


    public void onRefreshComplete(){

        current_state=INITSTATE;
        Log.i(TAG,"状态变为初始状态");
        refreshListviewHeader.setPadding(0,-measuredHeight ,0,0);

    }

    public void changeFooter(){
        tvRefreshfooterGetmoredata.setText("加载更多");
    }

    public MyRefreshListener listener;

    public void setMyRefreshListener(MyRefreshListener l){
        this.listener=l;
    }

    public interface MyRefreshListener{

        public void onRefreshing();

        public void onLoadMore();
    }
}

