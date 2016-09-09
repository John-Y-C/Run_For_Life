package com.example.administrator.healthpage;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.activity.ShowInfoDetailHealthActivity;
import com.example.administrator.bean.HealthInfoList;
import com.example.administrator.constants.Const;
import com.example.administrator.runforlife.R;
import com.example.administrator.utils.SharedPrefUtils;
import com.example.administrator.view.RefreshListView;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import java.util.List;


public class HealthInfoDetailPage {

    private static final String TAG = "HealthInfoDetailPage";
    private String url;
    int page =1;
    Activity mActivity;
    public View infoDetailView;
    private RefreshListView lvHealthdetailInfo;
    private List<HealthInfoList.ResultBean> resultList;
    private MyInfoListAdapter myInfoListAdapter;

    public HealthInfoDetailPage(Activity activity, String url) {

        mActivity = activity;
        this.url=url;

        infoDetailView = initView();
        initData();
    }


    private View initView() {

        View inflate = View.inflate(mActivity, R.layout.page_healthdetail_info, null);
        lvHealthdetailInfo = (RefreshListView) inflate.findViewById(R.id.lv_healthdetail_info);

        lvHealthdetailInfo.setMyRefreshListener(new RefreshListView.MyRefreshListener() {
            @Override
            public void onRefreshing() {
                //重新去加载该page对应的url，去获取服务器的最新数据
                getDataFromServer(url+1);
            }

            @Override
            public void onLoadMore() {

                page++;
                String urlRE=url+page;
                final String urlkey = urlRE;
                HttpUtils httpUtils = new HttpUtils();
                httpUtils.send(HttpRequest.HttpMethod.GET, urlkey, new RequestCallBack<String>() {
                    @Override
                    public void onSuccess(ResponseInfo<String> responseInfo) {

                        lvHealthdetailInfo.changeFooter();

                        Log.i(TAG,"url:"+urlkey);
                        Log.i(TAG, "onSuccess--" + responseInfo.result);

                        Gson gson = new Gson();
                        HealthInfoList infoDetailNew = gson.fromJson(responseInfo.result, HealthInfoList.class);

                        if (infoDetailNew.getResult().size()==0){

                            Toast.makeText(mActivity,"没有更多数据了，休息一会",Toast.LENGTH_SHORT).show();
                        }else {

                            resultList.addAll(infoDetailNew.getResult());
                            myInfoListAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onFailure(HttpException e, String s) {

                        lvHealthdetailInfo.onRefreshComplete();      //刷新完毕，回到初始状态
                        lvHealthdetailInfo.changeFooter();
                        Log.i(TAG, "onFailure--" + s);
                        Toast.makeText(mActivity, "加载失败，请稍后再试！", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        lvHealthdetailInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG,"onItemClick--position:"+position);
                int idForURL = resultList.get(position-1).getId();

                String urlContent = Const.healthDetailAddr +"?key="+Const.apiKey+"&id="+idForURL;

                Intent intent = new Intent(mActivity, ShowInfoDetailHealthActivity.class);
                intent.putExtra("url",urlContent);
                mActivity.startActivity(intent);

            }
        });

        return inflate;

    }


    private void initData() {

        String urlRE=url+page;
        String jsonFromCache = SharedPrefUtils.getJsonFromCache(urlRE, mActivity);

        if (jsonFromCache.isEmpty()){
            //从服务器上去拿数据
            Log.i(TAG,"缓存为空,从服务器上去拿数据");
            getDataFromServer(urlRE);
        }else {
            //缓存不为空，则直接用缓存去解析json
            Log.i(TAG,"缓存不为空,直接用缓存去解析json");
            parseJsonString(jsonFromCache);
        }
    }

    //从服务器获取数据
    private void getDataFromServer(String urlRE) {

        final String urlkey = urlRE;

        Log.i(TAG, "getDataFromServer="+urlRE);
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, urlkey, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                lvHealthdetailInfo.onRefreshComplete();      //刷新完毕，回到初始状态

                Log.i(TAG,"url:"+urlkey);
                Log.i(TAG, "onSuccess--" + responseInfo.result);

                //把从服务器上拿到的JsonString缓存起来
                SharedPrefUtils.saveJsonToCache(urlkey,responseInfo.result,mActivity);
                parseJsonString(responseInfo.result);

                Toast.makeText(mActivity, "刷新完毕", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(HttpException e, String s) {
                lvHealthdetailInfo.onRefreshComplete();   //刷新完毕，回到初始状态
                Log.i(TAG, "onFailure--" + s);
                Toast.makeText(mActivity, "加载失败，请稍后再试！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //解析数据
    private void parseJsonString(String result) {

        //用Gson解析
        Gson gson = new Gson();
        HealthInfoList infoDetail = gson.fromJson(result, HealthInfoList.class);

        Log.i(TAG,"infoDetail.getResult():"+infoDetail.getResult());

        if (infoDetail.getResult()!=null){
            resultList = infoDetail.getResult();
            myInfoListAdapter = new MyInfoListAdapter();
            lvHealthdetailInfo.setAdapter(myInfoListAdapter);
        }else {
            Toast.makeText(mActivity,"无法连接服器，请稍后再试！",Toast.LENGTH_SHORT).show();
        }

    }


    class MyInfoListAdapter extends BaseAdapter {

        BitmapUtils bitmapUtils;

        public MyInfoListAdapter() {
            bitmapUtils = new BitmapUtils(mActivity);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            View inflate = View.inflate(mActivity, R.layout.item_listview_infodetail, null);

            ImageView ivInfodetailImg = (ImageView) inflate.findViewById(R.id.iv_infodetail_img);
            ivInfodetailImg.setScaleType(ImageView.ScaleType.FIT_XY);

            TextView tvInfodetailTitle = (TextView) inflate.findViewById(R.id.tv_infodetail_title);
            TextView tvInfodetailDescription = (TextView) inflate.findViewById(R.id.tv_infodetail_keywords);

            //加载数据
            bitmapUtils.display(ivInfodetailImg,resultList.get(position).getImg());
            tvInfodetailTitle.setText(resultList.get(position).getTitle());
            tvInfodetailDescription.setText(resultList.get(position).getKeywords());

            return inflate;
        }


    }


}
