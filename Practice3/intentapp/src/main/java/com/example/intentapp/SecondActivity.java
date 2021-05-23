package com.example.intentapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SecondActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
//        setContentView(R.layout.second_activity);
    }

    @Override
    protected void onStart() {
        super.onStart();
//        TextView tw = (TextView) findViewById(R.id.otherTV);
//        Intent intent = getIntent();
//        String text = intent.getStringExtra("date");
//        Log.i("got", text);
//        tw.setText(text);
    }
}
