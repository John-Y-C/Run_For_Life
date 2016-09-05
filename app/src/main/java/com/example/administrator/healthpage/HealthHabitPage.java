package com.example.administrator.healthpage;

import android.app.Activity;
import android.view.View;

import com.example.administrator.runforlife.R;

/**
 * Created by GTR on 2016/9/3.
 */
public class HealthHabitPage {

    protected Activity mActivity;
    public View habitView;

    public HealthHabitPage(Activity activity){
        this.mActivity = activity;
        initView();
    }

    private void initView() {
        habitView = View.inflate(mActivity, R.layout.content_habit,null);
    }


}