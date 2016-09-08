package com.example.administrator.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.administrator.bean.Weather;
import com.example.administrator.constants.Const;
import com.example.administrator.runforlife.R;
import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/9/2/002.
 */
public class FragmentWeather extends Fragment {

    private static final String TAG = "FragmentWeather";

    /**
     * UI控件
     */
    @InjectView(R.id.tv_weather_temperature)
    TextView mTvWeatherTemperature;

    @InjectView(R.id.tv_weather_type)
    TextView mTvWeatherType;

    @InjectView(R.id.tv_weather_winddir)
    TextView mTvWeatherWinddir;

    @InjectView(R.id.linearLayout_details)
    LinearLayout mLinearLayout;

    @InjectView(R.id.img_weatherfragment_now)
    ImageView mImgWeatherfragmentNow;

    @InjectView(R.id.tv_weather_sportsuggestion)
    TextView mTvWeatherSportsuggestion;

    /**
     * json数据
     */
    private Weather mWeatherModel;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        getWeatherFromServer(Const.WEATHER_API);

        View inflate = View.inflate(getActivity(), R.layout.fragment_weather, null);
        ButterKnife.inject(this, inflate);


        return inflate;
    }

    private void getWeatherFromServer(String weatherApi) {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send(HttpRequest.HttpMethod.GET, weatherApi, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> info) {

                Gson gson = new Gson();
                mWeatherModel = gson.fromJson(info.result, Weather.class);

                final Weather.HeWeatherBean.NowBean nowWeather
                        = mWeatherModel.getHeWeather().get(0).getNow();
                final String condCode = nowWeather.getCond().getCode();

                final String sportSuggestion = mWeatherModel.getHeWeather().get(0).getSuggestion().getSport().getTxt();


                Log.d(TAG, "onSuccess: 当前气温 = " + nowWeather.getTmp());
                mTvWeatherTemperature.setText(nowWeather.getTmp());

                Log.d(TAG, "onSuccess: 天气类型 = " + nowWeather.getCond().getTxt());
                mTvWeatherType.setText(nowWeather.getCond().getTxt());

                Log.d(TAG, "onSuccess: 图标 = " + condCode);
                BitmapUtils bitmapUtils = new BitmapUtils(getActivity());
                bitmapUtils.display(mImgWeatherfragmentNow,
                        "http://files.heweather.com/cond_icon/"+condCode+".png");

                Log.d(TAG, "onSuccess: 风向 = " + nowWeather.getWind().getDir());
                mTvWeatherWinddir.setText(nowWeather.getWind().getDir());

                Log.d(TAG, "onSuccess: 运动建议 = " + sportSuggestion);
                mTvWeatherSportsuggestion.setText(sportSuggestion);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Log.d(TAG, "onFailure: ");
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
