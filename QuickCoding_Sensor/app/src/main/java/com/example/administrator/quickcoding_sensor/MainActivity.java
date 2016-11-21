package com.example.administrator.quickcoding_sensor;


import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView textGravity;
    private TextView textAccel;
    private TextView textLinear;
    private TextView textGyro;

    private SensorManager sm;
    private Sensor sensor_gravity;
    private Sensor sensor_accel;
    private Sensor sensor_linear;
    private Sensor sensor_gyro;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textGravity = (TextView)findViewById(R.id.textGravity);
        textAccel = (TextView)findViewById(R.id.textAccel);
        textLinear = (TextView)findViewById(R.id.textLinear);
        textGyro = (TextView)findViewById(R.id.textGyro);

        sm = (SensorManager)getSystemService(SENSOR_SERVICE);

        sensor_gravity = sm.getDefaultSensor(Sensor.TYPE_GRAVITY);
        sensor_accel = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensor_linear = sm.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        sensor_gyro = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    protected  void onResume(){
        super.onResume();

        //센서 리스너에 등록
        sm.registerListener(this,sensor_gravity,SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this,sensor_accel,SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this,sensor_linear,SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this,sensor_gyro,SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();

        sm.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor,int accuracy ){

    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        switch(event.sensor.getType()){
            case Sensor.TYPE_GRAVITY:
                textGravity.setText("Gravity :\nx : "+event.values[0]+"\ny : "+event.values[1]+"\nz : "+event.values[2]+"\n");
                break;
            case Sensor.TYPE_ACCELEROMETER:
                textAccel.setText("Acceleration :\nx : "+event.values[0]+"\ny : "+event.values[1]+"\nz : "+event.values[2]+"\n");
                break;
            case Sensor.TYPE_LINEAR_ACCELERATION:
                textLinear.setText("Linear acceleration :\nx : "+event.values[0]+"\ny : "+event.values[1]+"\nz : "+event.values[2]+"\n");
                break;
            case Sensor.TYPE_GYROSCOPE:
                textGyro.setText("Gyro :\nx : "+event.values[0]+"\ny : "+event.values[1]+"\nz : "+event.values[2]+"\n");
                break;
        }

    }
}
