// Generated code from Butter Knife. Do not modify!
package com.example.administrator.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FragmentWeather$$ViewInjector {
  public static void inject(Finder finder, final com.example.administrator.fragment.FragmentWeather target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492982, "field 'mTvWeatherTemperature'");
    target.mTvWeatherTemperature = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131492983, "field 'mTvWeatherType'");
    target.mTvWeatherType = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131492984, "field 'mTvWeatherWinddir'");
    target.mTvWeatherWinddir = (android.widget.TextView) view;
    view = finder.findRequiredView(source, 2131492981, "field 'mLinearLayout'");
    target.mLinearLayout = (android.widget.LinearLayout) view;
    view = finder.findRequiredView(source, 2131492985, "field 'mImgWeatherfragmentNow'");
    target.mImgWeatherfragmentNow = (android.widget.ImageView) view;
    view = finder.findRequiredView(source, 2131492986, "field 'mTvWeatherSportsuggestion'");
    target.mTvWeatherSportsuggestion = (android.widget.TextView) view;
  }

  public static void reset(com.example.administrator.fragment.FragmentWeather target) {
    target.mTvWeatherTemperature = null;
    target.mTvWeatherType = null;
    target.mTvWeatherWinddir = null;
    target.mLinearLayout = null;
    target.mImgWeatherfragmentNow = null;
    target.mTvWeatherSportsuggestion = null;
  }
}
