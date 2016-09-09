// Generated code from Butter Knife. Do not modify!
package com.example.administrator.runforlife;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class MainActivity$$ViewInjector {
  public static void inject(Finder finder, final com.example.administrator.runforlife.MainActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492948, "field 'fragmentlayout'");
    target.fragmentlayout = (android.widget.FrameLayout) view;
    view = finder.findRequiredView(source, 2131492950, "field 'rbFragmentRun' and method 'onClick'");
    target.rbFragmentRun = (android.widget.RadioButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492951, "field 'rbFragmentWeather' and method 'onClick'");
    target.rbFragmentWeather = (android.widget.RadioButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492952, "field 'rbFragmentHealth' and method 'onClick'");
    target.rbFragmentHealth = (android.widget.RadioButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492953, "field 'rbFragmentDate' and method 'onClick'");
    target.rbFragmentDate = (android.widget.RadioButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492954, "field 'rbFragmentSetting' and method 'onClick'");
    target.rbFragmentSetting = (android.widget.RadioButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492949, "field 'rgFramgentcontentBottom'");
    target.rgFramgentcontentBottom = (android.widget.RadioGroup) view;
  }

  public static void reset(com.example.administrator.runforlife.MainActivity target) {
    target.fragmentlayout = null;
    target.rbFragmentRun = null;
    target.rbFragmentWeather = null;
    target.rbFragmentHealth = null;
    target.rbFragmentDate = null;
    target.rbFragmentSetting = null;
    target.rgFramgentcontentBottom = null;
  }
}
