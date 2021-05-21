package com.example.intentfilter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    protected void onClickBowser(View view) {
        Uri address = Uri.parse("https://www.mirea.ru/");
        Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, address);
        if (openLinkIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(openLinkIntent);
        } else {
            Log.d("Intent", "Не получается обработать намерение");
        }
    }

    protected void onClickSMS(View view) {
        Uri sms = Uri.parse("smsto:88005553535");
        Intent sendMessageIntent = new Intent(Intent.ACTION_SENDTO, sms);
        sendMessageIntent.putExtra("sms_body", "Проще позвонить, чем у кого-то занимать");
        startActivity(sendMessageIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Button bowserButton = findViewById(R.id.button);
        bowserButton.setOnClickListener(v -> onClickBowser(bowserButton));

        Button smsButton = findViewById(R.id.button2);
        smsButton.setOnClickListener(v -> onClickSMS(smsButton));
    }
}