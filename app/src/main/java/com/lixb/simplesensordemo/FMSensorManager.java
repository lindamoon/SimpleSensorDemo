package com.lixb.simplesensordemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.annotation.NonNull;
import android.util.Log;

/**
 * @author lixb
 * @version 1.0
 * @date 2016/10/21 11:08
 */
public class FMSensorManager implements SensorEventListener {

    private final String TAG = getClass().getSimpleName();
    private SensorManager mSm;
    private Sensor aSensor;
    private Sensor mSensor;

    //加速度传感器数据
    float accValues[]=new float[3];
    //地磁传感器数据
    float magValues[]=new float[3];
    //旋转矩阵，用来保存磁场和加速度的数据
    float r[]=new float[9];
    //模拟方向传感器的数据（原始数据为弧度）
    float values[]=new float[3];
    private IOnOrientationChangeLIstener mListener;


    public FMSensorManager(Context context) {
        mSm = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        aSensor = mSm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensor = mSm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

    }

    public void registerOrientationListener(@NonNull IOnOrientationChangeLIstener listener) {
        mSm.registerListener(this, aSensor, SensorManager.SENSOR_DELAY_GAME);
        mSm.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_GAME);
        mListener = listener;
    }

    public void unregisterOrientationListener() {
        mSm.unregisterListener(this);
        mListener = null;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor.getType()== Sensor.TYPE_ACCELEROMETER){
            accValues =event.values.clone();//这里是对象，需要克隆一份，否则共用一份数据
        }
        else if(event.sensor.getType()== Sensor.TYPE_MAGNETIC_FIELD){
            magValues =event.values.clone();//这里是对象，需要克隆一份，否则共用一份数据
        }
        /**public static boolean getRotationMatrix (float[] R, float[] I, float[] gravity, float[] geomagnetic)
         * 填充旋转数组r
         * r：要填充的旋转数组
         * I:将磁场数据转换进实际的重力坐标中 一般默认情况下可以设置为null
         * gravity:加速度传感器数据
         * geomagnetic：地磁传感器数据
         */
        SensorManager.getRotationMatrix(r, null, accValues, magValues);
        /**
         * public static float[] getOrientation (float[] R, float[] values)
         * R：旋转数组
         * values ：模拟方向传感器的数据
         */

        SensorManager.getOrientation(r, values);

        if (null != mListener) {
            mListener.onOrientationChange((float) Math.toDegrees(values[0]),(float) Math.toDegrees(values[1]),(float) Math.toDegrees(values[2]));
        }
        //将弧度转化为角度后输出
        StringBuffer buff=new StringBuffer();

        for(float value:values){
            value=(float) Math.toDegrees(value);
            buff.append(value+"  ");
        }

        Log.e(TAG, "sensor result=" + buff.toString());


    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
