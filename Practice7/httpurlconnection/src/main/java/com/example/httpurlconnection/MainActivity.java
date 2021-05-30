package com.example.httpurlconnection;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private TextView tv;
    private final String url = "https://filesamples.com/samples/code/json/sample2.json";

    public class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            tv.setText("Загружаем...");
        }
        @Override
        protected String doInBackground(String... urls) {
            for (String url : urls)
                Log.i("urls", url);
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                e.printStackTrace();
                return "error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            tv.setText(result);
            Log.d(MainActivity.class.getSimpleName(), result);
            try {
                JSONObject responseJson = new JSONObject(result);
                String ip = responseJson.getString("ip");
                Log.d(MainActivity.class.getSimpleName(), ip);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
        private String downloadIpInfo(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";
            try {
                URL url = new URL(address);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setReadTimeout(100000);
                connection.setConnectTimeout(100000);
                connection.setRequestMethod("GET");
                connection.setInstanceFollowRedirects(true);
                connection.setUseCaches(false);
                connection.setDoInput(true);
                int responseCode = connection.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) { // 200 OK
                    inputStream = connection.getInputStream();
                    ByteArrayOutputStream bos = new ByteArrayOutputStream();
                    int read = 0;
                    while ((read = inputStream.read()) != -1) {
                        bos.write(read);
                    }
                    byte[] result = bos.toByteArray();
                    bos.close();
                    data = new String(result);
                } else {
                    data = connection.getResponseMessage() + " . Error Code : " + responseCode;
                }
                connection.disconnect();
                // return data;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }
            }
            return data;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        tv = findViewById(R.id.textView);
    }

    public void onClick(View view) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = null;
        if (connectivityManager != null) {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkinfo != null && networkinfo.isConnected()) {
            new DownloadPageTask().execute(url); // запускаем в новом потоке
        } else {
            Toast.makeText(this, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }
}