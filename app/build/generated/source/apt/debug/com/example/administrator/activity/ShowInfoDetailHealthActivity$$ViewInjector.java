// Generated code from Butter Knife. Do not modify!
package com.example.administrator.activity;

import android.view.View;
import butterknife.ButterKnife.Finder;

public class ShowInfoDetailHealthActivity$$ViewInjector {
  public static void inject(Finder finder, final com.example.administrator.activity.ShowInfoDetailHealthActivity target, Object source) {
    View view;
    view = finder.findRequiredView(source, 2131492955, "field 'ibShowinfodetailBack'");
    target.ibShowinfodetailBack = (android.widget.ImageButton) view;
    view = finder.findRequiredView(source, 2131492956, "field 'wvShowinfodetailContent'");
    target.wvShowinfodetailContent = (android.webkit.WebView) view;
    view = finder.findRequiredView(source, 2131492957, "field 'pbShowinfodetailLoading'");
    target.pbShowinfodetailLoading = (android.widget.ProgressBar) view;
  }

  public static void reset(com.example.administrator.activity.ShowInfoDetailHealthActivity target) {
    target.ibShowinfodetailBack = null;
    target.wvShowinfodetailContent = null;
    target.pbShowinfodetailLoading = null;
  }
}
