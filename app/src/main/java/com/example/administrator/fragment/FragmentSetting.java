package com.example.administrator.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.runforlife.R;

import butterknife.ButterKnife;

/**
 * Created by Administrator on 2016/9/2/002.
 */
public class FragmentSetting extends Fragment {


    private static final String TAG = "Fragment";
    private Button bt_settingfragmeng_recomend;
    private Button bt_settingfragmeng_checkupdate;
    private ProgressBar pb_settingfragmeng_progressbar;
    private TextView tv_settingframgmeng_checkupdate;
    private Button bt_settingfragmeng_cleancache;


    private TextView tv_mainactivity_time;
    private int year,month,day,ym;
    Time t=new Time();
    private String date;
    int i = 1;
    private Button bt_mainactivity_sign;
    private String currentdate;
    private SharedPreferences config;
    private SharedPreferences.Editor editor;
    private boolean isFirstClick;
    private TextView tv_settingframgmeng_signday;
    private int signday;
    private int newsignday;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View inflate = View.inflate(getActivity(), R.layout.fragment_setting, null);

        tv_mainactivity_time = (TextView) inflate.findViewById(R.id.tv_mainactivity_time);
        bt_mainactivity_sign = (Button) inflate.findViewById(R.id.bt_mainactivity_sign);

        bt_settingfragmeng_cleancache = (Button) inflate.findViewById(R.id.bt_settingfragmeng_cleancache);

        bt_settingfragmeng_recomend = (Button) inflate.findViewById(R.id.bt_settingfragmeng_recomend);

        bt_settingfragmeng_checkupdate = (Button) inflate.findViewById(R.id.bt_settingfragmeng_checkupdate);

        pb_settingfragmeng_progressbar = (ProgressBar) inflate.findViewById(R.id.pb_settingfragmeng_progressbar);

        tv_settingframgmeng_checkupdate = (TextView) inflate.findViewById(R.id.tv_settingframgmeng_checkupdate);

        tv_settingframgmeng_signday = (TextView) inflate.findViewById(R.id.tv_settingframgmeng_signday);
        config = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = config.edit();


        //获取当前时间
        t.setToNow();
        year=t.year;
        month=t.month+1;
        day = t.monthDay+3;
        Log.i(TAG,"oncreate");
        date = "今天是"+year+"年"+month+"月"+day+"日";



        config = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
        editor = config.edit();
        //从sharepreference中拿到上次签到的时间
        String currentdate = config.getString("signdate","2016年9月1日");

        Log.i(TAG,"currentday="+currentdate);
        Log.i(TAG,"date="+date);
        //设置当前时间
        tv_mainactivity_time.setText(date);

        signday = config.getInt("signday",0);
        tv_settingframgmeng_signday.setText("已经签到了"+ signday +"天！");

        //对比当前时间与上次签到时间不相同才允许签到
        if(!(date).equals((currentdate))){
            editor.putBoolean("isFirstClick",true);
            editor.commit();
            tv_mainactivity_time.setTextColor(Color.GRAY);
            bt_mainactivity_sign.setClickable(true);
            boolean comptime = date.equals(currentdate);

            Log.i(TAG,"comptime="+comptime);
            //点击签到
            bt_mainactivity_sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    isFirstClick = config.getBoolean("isFirstClick",false);
                    signday = config.getInt("signday",0);

                    Log.i(TAG,"isFirstClick="+isFirstClick);
                    if(isFirstClick){
                        tv_mainactivity_time.setTextColor(Color.RED);
                        bt_mainactivity_sign.setText("已经签到");
                        Toast.makeText(getActivity(), "签到成功！", Toast.LENGTH_SHORT).show();
                        editor.putBoolean("isFirstClick",false);
                        editor.putBoolean("isAlreadySign",true);
                        editor.putInt("signday", signday +1);

                        editor.putString("signdate",tv_mainactivity_time.getText().toString());
                        Log.i(TAG,"isclick="+isFirstClick);
                        editor.commit();

                        newsignday = signday+1;

                        tv_settingframgmeng_signday.setText("已经签到了"+ newsignday +"天！");

                    }else {

                        while (i==1){
                            Toast.makeText(getActivity(), "已经签到，请勿重新签到", Toast.LENGTH_SHORT).show();
                            i++;
                        }


                    }

                }
            });
        } else {

            boolean isAlreadySign = config.getBoolean("isAlreadySign",false);
            if (isAlreadySign){
                bt_mainactivity_sign.setText("已经签到");
                tv_mainactivity_time.setTextColor(Color.RED);
                int n = config.getInt("signday",0);
                tv_settingframgmeng_signday.setText("已经签到了"+n+"天！");
                bt_mainactivity_sign.setClickable(false);
            }

        }
        //清除缓存
        bt_settingfragmeng_cleancache.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {




            }
        });





        //检查更新
        bt_settingfragmeng_checkupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                tv_settingframgmeng_checkupdate.setVisibility(View.VISIBLE);
                pb_settingfragmeng_progressbar.setVisibility(View.VISIBLE);

                AnimationSet as = new AnimationSet(false);
                RotateAnimation rotateAnimation = new RotateAnimation(0,360, Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f);
                as.addAnimation(rotateAnimation);
                rotateAnimation.setDuration(3000);
                pb_settingfragmeng_progressbar.setAnimation(as);

                as.start();

                as.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {

                        pb_settingfragmeng_progressbar.clearAnimation();
                        pb_settingfragmeng_progressbar.setVisibility(View.INVISIBLE);
                        tv_settingframgmeng_checkupdate.setVisibility(View.INVISIBLE);

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.setMessage("检查完毕，暂无更新");
                        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        alertDialog.show();
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

            }
        });

        //反馈建议
        bt_settingfragmeng_recomend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText inputServer = new EditText(getActivity());

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("请输入您的意见").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer)
                        .setNegativeButton("取消发送", null);
                builder.setPositiveButton("发送", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        inputServer.getText();
                        Toast.makeText(getActivity(), "发送成功！", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });


        return inflate;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
