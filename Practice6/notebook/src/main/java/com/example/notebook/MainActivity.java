package com.example.notebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private EditText title, content;
    private SharedPreferences preferences;
    private String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        title = findViewById(R.id.titleEditText);
        content = findViewById(R.id.contentEditText);
        preferences = getPreferences(MODE_PRIVATE);
    }

    @Override
    protected void onStart() {
        super.onStart();
        String filename = preferences.getString("filename", "file");
        title.setText(filename);
        try {
            FileInputStream fin = openFileInput(filename);
            byte[] bytes = new byte[fin.available()];
            fin.read(bytes);
            fin.close();
            String text = new String(bytes);
            content.setText(text);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "Paused");
        String filename = title.getText().toString();
        new Thread(() -> {
            try {
                FileOutputStream fout = openFileOutput(filename, MODE_PRIVATE);
                fout.write(content.getText().toString().getBytes());
                fout.close();
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("filename", filename);
                editor.apply();
                Log.i(TAG, "changes applied");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}