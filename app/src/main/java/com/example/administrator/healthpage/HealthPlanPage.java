package com.example.administrator.healthpage;

import android.app.Activity;
import android.view.View;

import com.example.administrator.runforlife.R;


public class HealthPlanPage {

    protected Activity mActivity;
    public View planView;

    public HealthPlanPage(Activity activity) {
        this.mActivity = activity;
        initView();
    }

    private void initView() {
        planView = View.inflate(mActivity, R.layout.content_health_plan, null);

    }


}