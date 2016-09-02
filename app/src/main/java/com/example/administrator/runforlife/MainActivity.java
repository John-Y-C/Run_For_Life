package com.example.administrator.runforlife;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.administrator.fragment.FragmentDate;
import com.example.administrator.fragment.FragmentHealth;
import com.example.administrator.fragment.FragmentRun;
import com.example.administrator.fragment.FragmentSetting;
import com.example.administrator.fragment.FragmentWeather;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.fragmentlayout)
    FrameLayout fragmentlayout;
    @InjectView(R.id.rb_fragment_run)
    RadioButton rbFragmentRun;
    @InjectView(R.id.rb_fragment_weather)
    RadioButton rbFragmentWeather;
    @InjectView(R.id.rb_fragment_health)
    RadioButton rbFragmentHealth;
    @InjectView(R.id.rb_fragment_date)
    RadioButton rbFragmentDate;
    @InjectView(R.id.rb_fragment_setting)
    RadioButton rbFragmentSetting;
    @InjectView(R.id.rg_framgentcontent_bottom)
    RadioGroup rgFramgentcontentBottom;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        getSupportActionBar().hide();
        fragmentManager = getFragmentManager();
    }

    @OnClick({R.id.rb_fragment_run, R.id.rb_fragment_weather, R.id.rb_fragment_health, R.id.rb_fragment_date, R.id.rb_fragment_setting})
    public void onClick(View view) {
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        switch (view.getId()) {
            case R.id.rb_fragment_run:
                fragmentTransaction.replace(R.id.fragmentlayout,new FragmentRun());
                fragmentTransaction.commit();
                break;
            case R.id.rb_fragment_weather:
                fragmentTransaction.replace(R.id.fragmentlayout,new FragmentWeather());
                fragmentTransaction.commit();
                break;
            case R.id.rb_fragment_health:
                fragmentTransaction.replace(R.id.fragmentlayout,new FragmentHealth());
                fragmentTransaction.commit();
                break;
            case R.id.rb_fragment_date:
                fragmentTransaction.replace(R.id.fragmentlayout,new FragmentDate());
                fragmentTransaction.commit();
                break;
            case R.id.rb_fragment_setting:
                fragmentTransaction.replace(R.id.fragmentlayout,new FragmentSetting());
                fragmentTransaction.commit();
                break;
        }
    }
}
