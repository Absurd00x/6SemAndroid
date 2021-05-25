package com.example.mireaproject;

import android.content.Context;
import android.content.pm.FeatureInfo;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SensorsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SensorsFragment extends Fragment
                             implements SensorEventListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private FragmentActivity fa;
    private Sensor humiditySensor, temperatureSensor, pressureSensor, gyroscopeSensor;
    private TextView humidityTextView, temperatureTextView, pressureTextView;
    private TextView axisX, axisY, axisZ;
    SensorManager sm;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SensorsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SensorsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SensorsFragment newInstance(String param1, String param2) {
        SensorsFragment fragment = new SensorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        fa = getActivity();
        sm = (SensorManager) fa.getSystemService(Context.SENSOR_SERVICE);

        humiditySensor = sm.getDefaultSensor(Sensor.TYPE_RELATIVE_HUMIDITY);
        temperatureSensor = sm.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        pressureSensor = sm.getDefaultSensor(Sensor.TYPE_PRESSURE);
        gyroscopeSensor = sm.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
    }

    @Override
    public void onStart() {
        super.onStart();
        humidityTextView = fa.findViewById(R.id.humidityValue);
        temperatureTextView = fa.findViewById(R.id.temperatureValue);
        pressureTextView = fa.findViewById(R.id.pressureValue);
        axisX = fa.findViewById(R.id.valueX);
        axisY = fa.findViewById(R.id.valueY);
        axisZ = fa.findViewById(R.id.valueZ);

        PackageManager pm = fa.getPackageManager();
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_RELATIVE_HUMIDITY))
            humidityTextView.setText("Функция недоступна");
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_AMBIENT_TEMPERATURE))
            temperatureTextView.setText("Функция недоступна");
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_BAROMETER))
            pressureTextView.setText("Функция недоступна");
        if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_GYROSCOPE)) {
            axisX.setText("Функция недоступна");
            axisX.setText("Функция недоступна");
            axisX.setText("Функция недоступна");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sm.unregisterListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        sm.registerListener(this, humiditySensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL);
        sm.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensors, container, false);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        switch (event.sensor.getType()) {
            case Sensor.TYPE_RELATIVE_HUMIDITY:
                humidityTextView.setText(String.valueOf(event.values[0]) + " %");
                break;
            case Sensor.TYPE_AMBIENT_TEMPERATURE:
                temperatureTextView.setText(String.valueOf(event.values[0]) + " °C");
                break;
            case Sensor.TYPE_PRESSURE:
                pressureTextView.setText(String.valueOf(event.values[0]) + " hPa");
                break;
            case Sensor.TYPE_GYROSCOPE:
                axisX.setText(String.valueOf(event.values[0]));
                axisY.setText(String.valueOf(event.values[1]));
                axisZ.setText(String.valueOf(event.values[2]));
            default:
                break;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}