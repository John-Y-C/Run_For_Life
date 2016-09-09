// Generated code from Butter Knife. Do not modify!
package com.example.administrator.fragment;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class FragmentRun$$ViewInjector {
  public static void inject(Finder finder, final com.example.administrator.fragment.FragmentRun target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492968, "field 'lrFragmentrunImg'");
    target.lrFragmentrunImg = (android.support.v7.widget.RecyclerView) view;
    view = finder.findRequiredView(source, 2131492969, "field 'ivFragmentInitimg' and method 'onClick'");
    target.ivFragmentInitimg = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492970, "field 'ibFragmentRun' and method 'onClick'");
    target.ibFragmentRun = (android.widget.ImageButton) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492971, "field 'ivFragmentColumn' and method 'onClick'");
    target.ivFragmentColumn = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
    view = finder.findRequiredView(source, 2131492972, "field 'ivFragmentStag' and method 'onClick'");
    target.ivFragmentStag = (android.widget.ImageView) view;
    view.setOnClickListener(
      new android.view.View.OnClickListener() {
        @Override public void onClick(
          android.view.View p0
        ) {
          target.onClick(p0);
        }
      });
  }

  public static void reset(com.example.administrator.fragment.FragmentRun target) {
    target.lrFragmentrunImg = null;
    target.ivFragmentInitimg = null;
    target.ibFragmentRun = null;
    target.ivFragmentColumn = null;
    target.ivFragmentStag = null;
  }
}
