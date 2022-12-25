package com.example.game3lanes;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
public class Tilt_Detector {
    private TiltCallback tiltCallback;
    private SensorManager sensorManager;
    private Sensor sensor;

    private final int TILT_MULT = 1;
    private int stepCountX = 0;
    private int stepCountY = 0;
    private long timestemp = 0;

    private SensorEventListener sensorEventListener;

    public Tilt_Detector(Context context, TiltCallback _tiltCallback) {
        sensorManager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        this.tiltCallback = _tiltCallback;
        initEventListener();
    }

    private void initEventListener() {
        sensorEventListener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float x = event.values[0];
                float y = event.values[1];

                calculateStep(x, y);
            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
        };
    }

    private void calculateStep(float x, float y) {
        if (System.currentTimeMillis() - timestemp > 500) {
            timestemp = System.currentTimeMillis();
            if (x > TILT_MULT) {
                if (tiltCallback != null)
                    tiltCallback.stepXNegativeOne();
            }

            if(x < -TILT_MULT)
                if (tiltCallback != null)
                    tiltCallback.stepXPlusOne();


            if (y > TILT_MULT) {
                if (tiltCallback != null)
                    tiltCallback.stepYPos();
            }

            if (y < -TILT_MULT) {
                if (tiltCallback != null)
                    tiltCallback.stepYNeg();
            }


        }
    }


    public void start() {
        sensorManager.registerListener(
                sensorEventListener,
                sensor,
                SensorManager.SENSOR_DELAY_NORMAL
        );
    }

    public void stop() {
        sensorManager.unregisterListener(
                sensorEventListener,
                sensor
        );
    }
}
