package ru.mirea.petrov.mireaproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.CancellationToken;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnTokenCanceledListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InternetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InternetFragment extends Fragment implements LocationListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentActivity fa;
    private SharedPreferences preferences;
    private final String keyTime = "time";
    private final String keyForecast = "forecast";
    private final String keyTemperature = "temperature";
    private final String keyHumidity = "humidity";
    private final String keyPressure = "pressure";
    private final String keyWind = "wind";
    private final int FIELDS = 6;
    private final int REQUEST_CODE = 123;
    private final double hPaMult = 1.0 / 1.33322390232;
    private final String URL = "https://api.openweathermap.org/data/2.5/forecast?";
    private final String APIKey = "39d8e4fc47b580660263e6047287d16d";
    private TextView[] values = new TextView[FIELDS];
    private Location curLocation;
    private long lastCallTime;

    private final long tenMinutesInMillis = 10 * 60 * 1000;

    @Override
    public void onLocationChanged(@NonNull Location location) {
        curLocation = location;
    }

    public class DownloadPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected String doInBackground(String... urls) {
            try {
                return downloadIpInfo(urls[0]);
            } catch (IOException e) {
                return "error";
            }
        }
        @Override
        protected void onPostExecute(String result) {
            try {
                JSONObject responseJson = new JSONObject(result).getJSONArray("list").getJSONObject(0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong(keyTime, new Date().getTime());
                String description = responseJson.getJSONArray("weather").getJSONObject(0).getString("description");
                editor.putString(keyForecast, description);
                JSONObject main = responseJson.getJSONObject("main");
                editor.putFloat(keyTemperature, (float)main.getDouble("temp"));
                editor.putInt(keyHumidity, main.getInt("humidity"));
                editor.putInt(keyPressure, main.getInt("pressure"));
                editor.putFloat(keyWind, (float)responseJson.getJSONObject("wind").getDouble("speed"));
                editor.apply();
                loadData();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(result);
        }
        private String downloadIpInfo(String address) throws IOException {
            InputStream inputStream = null;
            String data = "";
            try {
                java.net.URL url = new URL(address);
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

    public InternetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InternetFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InternetFragment newInstance(String param1, String param2) {
        InternetFragment fragment = new InternetFragment();
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
    }

    @Override
    public void onStart() {
        super.onStart();
        Button call = fa.findViewById(R.id.buttonAPICall);
        call.setOnClickListener(this::onClick);
        preferences = fa.getPreferences(Context.MODE_PRIVATE);

        values[0] = fa.findViewById(R.id.valueTime);
        values[1] = fa.findViewById(R.id.valueForecast);
        values[2] = fa.findViewById(R.id.valueTemperature);
        values[3] = fa.findViewById(R.id.valueHumidity);
        values[4] = fa.findViewById(R.id.valuePressure);
        values[5] = fa.findViewById(R.id.valueWind);

        loadData();
    }

    private void loadData() {
        lastCallTime = preferences.getLong(keyTime, 0);
        String forecast = preferences.getString(keyForecast, "");
        float temperature = preferences.getFloat(keyTemperature, 0.0f);
        int humidity = preferences.getInt(keyHumidity, 0);
        int pressure = preferences.getInt(keyPressure, 0);
        float wind = preferences.getFloat(keyWind, 0.0f);

        String[] data = {
                android.text.format.DateFormat.format("yyyy-MM-dd kk:mm", new Date(lastCallTime)).toString(),
                forecast,
                String.format("%.2f", temperature) + "°C",
                String.valueOf(humidity) + "%",
                String.valueOf((int)(pressure * hPaMult)) + "мм рт. ст.",
                String.format("%.2f", wind) + "м/с"
        };

        for (int i = 0; i < FIELDS; ++i)
            values[i].setText(data[i]);
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(fa, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(fa, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(fa, "У приложения недостаточно прав для определения геолокации", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(fa, new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            return false;
        }
        return true;
    }

    @SuppressLint("MissingPermission")
    private void postJob() {
        FusedLocationProviderClient locationProviderClient = new FusedLocationProviderClient(fa);
        locationProviderClient.flushLocations();
        locationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY, new CancellationToken() {
            @Override
            public boolean isCancellationRequested() {
                return false;
            }

            @NonNull
            @NotNull
            @Override
            public CancellationToken onCanceledRequested(@NonNull @NotNull OnTokenCanceledListener onTokenCanceledListener) {
                return null;
            }
        }).addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Location> task) {
                sendRequest(task.getResult());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(fa,"Не удалось определить местоположение", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getApiCallString(Location location) {
        List<Pair<String, String>> params = new ArrayList<>();
        params.add(new Pair<>("lat", String.valueOf(location.getLatitude())));
        params.add(new Pair<>("lon", String.valueOf(location.getLongitude())));
        params.add(new Pair<>("appid", APIKey));
        params.add(new Pair<>("units", "metric"));
        params.add(new Pair<>("lang", "ru"));

        StringBuilder request = new StringBuilder();
        request.append(URL);
        for(int i = 0; i < params.size(); ++i) {
            request.append(i > 0 ? "&" : "");
            request.append(params.get(i).first);
            request.append('=');
            request.append(params.get(i).second);
        }
        return request.toString();
    }

    private void sendRequest(Location location) {
        if (location == null) {
            Toast.makeText(fa, "Не удалось получить геопозицию", Toast.LENGTH_SHORT).show();
            return;
        }
        String request = getApiCallString(location);

        ConnectivityManager connectivityManager =
                (ConnectivityManager) fa.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkinfo = null;
        if (connectivityManager != null) {
            networkinfo = connectivityManager.getActiveNetworkInfo();
        }
        if (networkinfo != null && networkinfo.isConnected()) {
            new DownloadPageTask().execute(request); // запускаем в новом потоке
        } else {
            Toast.makeText(fa, "Нет интернета", Toast.LENGTH_SHORT).show();
        }
    }


    private void onClick(View view) {
        long timePassed = (new Date()).getTime() - lastCallTime;
        if (timePassed > tenMinutesInMillis) {
            if (!checkPermissions())
                return;
            postJob();
        } else {
            Toast.makeText(getContext(),
                    "Обновлять информацию можно не чаще, чем раз в 10 минут",
                    Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_internet, container, false);
    }
}