package com.example.resultactivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class DataActivity extends AppCompatActivity {
    private EditText et;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        et = findViewById(R.id.textField);
    }

    public void onClickSend(View v) {
        Intent intent = new Intent();
        intent.putExtra("uni", et.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}