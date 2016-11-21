package  com.example.administrator.quickcoding_pedometer;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private int count = 0;
    private TextView textCount=null;

    private SensorManager sensorManager;
    private Sensor accelSensor;
    private Sensor gravitySensor;

    double acceleration = 0.0f;
    double gravity = 0.0f;
    int dir_UP = 0;
    int dir_DOWN = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        gravitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY);

        textCount = (TextView)findViewById(R.id.textCount);
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelSensor, SensorManager.SENSOR_DELAY_GAME);
        sensorManager.registerListener(this, gravitySensor, SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            acceleration = Math.sqrt(x * x + y * y + z * z);
        }
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            gravity = Math.sqrt(x * x + y * y + z * z);
        }

        if((acceleration - gravity) > 5){
            dir_UP = 1;
        }
        if((dir_UP == 1) && (( gravity-acceleration) > 5)){
            dir_DOWN=1;
        }
        if(dir_DOWN==1){
            count++;
            textCount.setText(count+" 걸음");

            dir_UP=0;
            dir_DOWN=0;
        }
    }
}