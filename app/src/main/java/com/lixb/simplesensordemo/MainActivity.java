package com.lixb.simplesensordemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, IOnOrientationChangeLIstener {

    private FMSensorManager mFMSensorManager;
    private TextView mTvAzimuth;
    private TextView mTvPitch;
    private TextView mTvRoll;
    private DecimalFormat mFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.btn_sensor).setOnTouchListener(this);
        mTvAzimuth = (TextView) findViewById(R.id.tv_azimuth);
        mTvPitch = (TextView) findViewById(R.id.tv_pitch);
        mTvRoll = (TextView) findViewById(R.id.tv_roll);
        mFMSensorManager = new FMSensorManager(this);
        mFormat = new DecimalFormat("0.00");
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mFMSensorManager.registerOrientationListener(this);
                break;
            case MotionEvent.ACTION_UP:
                mFMSensorManager.unregisterOrientationListener();
                break;
        }
        return false;
    }

    @Override
    public void onOrientationChange(float azimuth, float pitch, float roll) {
        mTvAzimuth.setText("azimuth:" + Math.round(azimuth));
        mTvPitch.setText("pitch:" + Math.round(pitch));
        mTvRoll.setText("roll:"+ Math.round(roll));
    }
}
