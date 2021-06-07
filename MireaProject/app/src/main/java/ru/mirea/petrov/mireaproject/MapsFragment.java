package ru.mirea.petrov.mireaproject;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Formatter;
import java.util.Locale;

public class MapsFragment extends Fragment
                          implements OnMapReadyCallback, GoogleMap.OnMapClickListener{

    private GoogleMap gMap;
    private final String TAG = MapsFragment.class.getSimpleName();
    private final int REQUEST_CODE = 123;
    private FragmentActivity fa;
    private ArrayList<Marker> points = new ArrayList<>();
    private BitmapDescriptor blueMarkerIcon;
    private Polyline polyline = null;
    private TextView tw;
    private String defaultAreaValue = "Площадь поверхности:\n0 метров";
    private final String[][] data = {
            {"55.670114179386225", "37.480332421808534", "МГТУ МИРЭА", "Главный корпус, 1947, 55.67011 37.48033"},
            {"55.7954692193415", "37.7028740389673", "МГУПИ", "Корпус на Стромынке, 1936, 55.79546 37.70287"},
            {"55.661664453919464", "37.47773638552249", "МИТХТ", "Корпус на Проспекте Вернадского, 2004, 55.66166 37.47773"},
            {"55.729021139501896", "37.5730980419378", "ИПК Минобрнауки России", "Корпус на Фрунзенской, 1908, 55.72902 37.57309"},
            {"55.78269177550625", "37.627817312059655", "ФГБУ РосНИИ ИТиАП", "Корпус на Щепкина, 1994, 55.78269 37.62781"},
            {"55.83358702514091", "37.62163747548318", "ВНИИТЭ", "Корпус на Проспекте Мира, 1962, 55.83358 37.62163"},
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        fa = getActivity();
        tw = fa.findViewById(R.id.areaDisplay);

        SupportMapFragment mapFragment = (SupportMapFragment)this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        } else {
            Log.wtf(TAG, "this is null");
        }

        blueMarkerIcon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);

        Button wipeButton = fa.findViewById(R.id.buttonWipe);
        wipeButton.setOnClickListener(v -> onWipeClick(wipeButton));
    }

    private static boolean isNotRightTurn(LatLng a, LatLng b, LatLng c) {
        double cross = (a.latitude - b.latitude) * (c.longitude - b.longitude)
                - (a.longitude - b.longitude) * (c.latitude - b.latitude);
        double dot = (a.latitude - b.latitude) * (c.latitude - b.latitude)
                + (a.longitude - b.longitude) * (c.longitude - b.longitude);
        return cross < 0 || cross == 0 && dot <= 0;
    }

    private LatLng[] updateHull() {
        Collections.sort(points, ((Comparator<Marker>) (o1, o2) -> {
            if (o1.getPosition().latitude != o1.getPosition().latitude)
                return Double.compare((o1.getPosition().latitude), o2.getPosition().latitude);
            return Double.compare(o1.getPosition().longitude, o2.getPosition().longitude);
        }));
        int n = points.size();
        LatLng[] hull = new LatLng[n + 1];
        int cnt = 0;
        for(int i = 0; i < 2 * n - 1; ++i) {
            int j = i < n ? i : 2 * n - 2 - i;
            while (cnt >= 2 && isNotRightTurn(hull[cnt - 2], hull[cnt - 1], points.get(j).getPosition()))
                --cnt;
            hull[cnt++] = points.get(j).getPosition();
        }
        return Arrays.copyOf(hull, cnt);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        points.add(gMap.addMarker(new MarkerOptions()
                .position(latLng)
                .icon(blueMarkerIcon)
        ));
        if (points.size() < 3)
            return;

        LatLng[] hull = updateHull();
        if (polyline != null) {
            polyline.remove();
        }
        PolylineOptions polylineOptions = new PolylineOptions();
        polylineOptions.addAll(Arrays.asList(hull));
        polylineOptions.add(hull[0]);
        double square = SphericalUtil.computeArea(Arrays.asList(hull));
        final double THRESHOLD = 1e6;
        String measurementUnits = " метров";
        if (square > THRESHOLD) {
            square /= 1000;
            measurementUnits = " километров";
        }
        String area = new Formatter(Locale.US).format("%.3f", square).toString();
        tw.setText("Площать поверхности:\n" + area + measurementUnits);
        polyline = gMap.addPolyline(polylineOptions);

    }

    public void onWipeClick(View v) {
        for(Marker marker : points)
            marker.remove();
        points.clear();
        if (polyline != null) {
            polyline.remove();
            polyline = null;
            tw.setText(defaultAreaValue);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG, "map is ready");
        if (ActivityCompat.checkSelfPermission(fa, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(fa, "Недостаточно прав для этой функции!", Toast.LENGTH_LONG).show();
            ActivityCompat.requestPermissions(fa,
                    new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
            Navigation.findNavController(fa, R.id.nav_host_fragment_content_main)
                    .navigate(R.id.nav_home);
            return;
        }
        gMap = googleMap;
        gMap.setOnMapClickListener(this);
        for (String row[] : data) {
            LatLng position = new LatLng(Double.parseDouble(row[0]),
                    Double.parseDouble(row[1]));
            String title = row[2];
            String description = row[3];
            gMap.addMarker(new MarkerOptions()
                    .position(position)
                    .title(title)
                    .snippet(description)
            );
        }
        LatLng moscow = new LatLng(55.758790045130745, 37.616895237956435);
        gMap.moveCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder().target(moscow).zoom(10).build()
        ));
    }
}