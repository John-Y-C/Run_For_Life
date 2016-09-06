package com.example.administrator.healthpage;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.activity.ShowInfoDetailHealthActivity;
import com.example.administrator.bean.HealthInfoList;
import com.example.administrator.constants.Const;
import com.example.administrator.runforlife.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

import java.util.List;


public class HealthInfoDetailPage {

    private static final String TAG = "HealthInfoDetailPage";
    Activity mActivity;
    public View infoDetailView;
    private ListView lvHealthdetailInfo;
    private List<HealthInfoList.ResultBean> resultList;

    public HealthInfoDetailPage(Activity activity, int infoId) {

        mActivity = activity;

        infoDetailView = initView();
        initData(infoId);
    }


    private View initView() {

        View inflate = View.inflate(mActivity, R.layout.page_healthdetail_info, null);
        lvHealthdetailInfo = (ListView) inflate.findViewById(R.id.lv_healthdetail_info);
        return inflate;

    }


    private void initData(int infoId) {

        //生成url  ,筛选关键字为 运动 或 健康 的内容
        String url = Const.healthServerAddr +"?key="+Const.apiKey+"&keyword=运动u007C健康&classify="+infoId+"&page=1&rows=10";

        getDataFromServer(url);

    }

    //从服务器获取数据
    private void getDataFromServer(String url) {

        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(com.lidroid.xutils.http.client.HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {

                Log.i(TAG, "onSuccess--" + responseInfo.result);
                parseJsonString(responseInfo.result);
            }

            @Override
            public void onFailure(com.lidroid.xutils.exception.HttpException e, String s) {

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

        resultList = infoDetail.getResult();
        lvHealthdetailInfo.setAdapter(new MyInfoListAdapter());

        lvHealthdetailInfo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.i(TAG,"onItemClick--position:"+position);
                int idForURL = resultList.get(position).getId();

                String url = Const.healthServerAddr +"?key="+Const.apiKey+"&id="+idForURL;

                Intent intent = new Intent(mActivity, ShowInfoDetailHealthActivity.class);
                intent.putExtra("url",url);
                mActivity.startActivity(intent);

            }
        });


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
