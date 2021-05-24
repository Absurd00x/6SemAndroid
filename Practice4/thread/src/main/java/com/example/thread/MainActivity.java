package com.example.thread;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;
import java.util.concurrent.atomic.AtomicReference;

public class MainActivity extends AppCompatActivity {
    int counter = 0;
    private EditText et;
    private TextView tw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tw = findViewById(R.id.textView);
        et = findViewById(R.id.editTextTextPersonName);
        Thread mainThread = Thread.currentThread();
        tw.setText("Текущий поток: " + mainThread.getName());
        mainThread.setName("MireaThread");
        tw.append("\n Новое имя потока: " + mainThread.getName());
    }

    public void onClick(View v) throws InterruptedException {
        AtomicReference<String> result = new AtomicReference<>("");
        Runnable runnable = () -> {
            int numberThread = counter++;
            Log.i("ThreadProject", "Запущен поток № " + numberThread);
            int classes = Integer.parseInt(et.getText().toString());
            Calendar calendar = Calendar.getInstance();
            int days = calendar.getActualMaximum(calendar.DAY_OF_MONTH);
            Log.i("Days in month", String.valueOf(days));
            result.set(String.valueOf((double) classes / days));
            Log.i("ThreadProject", "Выполнен поток № " + numberThread);
        };
        Thread thread = new Thread(runnable);
        thread.start();
        thread.join();
        tw.setText(result.get());
    }
}