package com.example.medicinesearch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private Button btn_search, btn_find, btn_news, btn_calendar;
    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        settingGPS();
        Location userLocation = getMyLocation();


        double latitude = userLocation.getLatitude();
        double longitude = userLocation.getLongitude();

        PharmParser.Xpos = longitude;
        PharmParser.Ypos = latitude;


        btn_search = findViewById(R.id.btn_search);    //의약품 검색
        btn_find = findViewById(R.id.btn_find);        //내 주변 약국 검색
        btn_news = findViewById(R.id.btn_news);         //의약품 뉴스
        btn_calendar = findViewById(R.id.btn_calendar); //복용일지


        btn_search.setOnClickListener(new View.OnClickListener() {           //의약품 검색창으로 이동
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, search.class);
                startActivity(intent);
            }
        });

        btn_find.setOnClickListener(new View.OnClickListener() {     //내 주변 약국 검색창으로 이동
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, find.class);
                startActivity(intent);
            }
        });

        btn_news.setOnClickListener(new View.OnClickListener() {     //의약품 뉴스창으로 이동
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });

        btn_calendar.setOnClickListener(new View.OnClickListener() {  //의약품 복용일지창으로 이동
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, calendar.class);
                startActivity(intent);
            }
        });

    }

    private Location getMyLocation(){
        int rqCode = 1004;
        Location currentLocation = null;

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, rqCode);
        }
        else {
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);


            String locationProvider = LocationManager.GPS_PROVIDER;
            currentLocation = locationManager.getLastKnownLocation(locationProvider);
            if (currentLocation != null) {
                double lng = currentLocation.getLongitude();
                double lat = currentLocation.getLatitude();
                Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
            }
        }
        return currentLocation;
    }

    private void settingGPS() {

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double latitude = location.getLatitude();
                double longitude = location.getLongitude();

                // TODO 위도, 경도로 하고 싶은 것
                PharmParser.Xpos = longitude;
                PharmParser.Ypos = latitude;


            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
            }
        };
    }

    boolean canReadLocation = false;

}