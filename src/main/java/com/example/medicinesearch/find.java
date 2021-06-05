package com.example.medicinesearch;

import android.content.Intent;
import android.graphics.PointF;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.InfoWindow;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.Overlay;
import com.naver.maps.map.util.FusedLocationSource;

import java.util.ArrayList;


public class find extends AppCompatActivity implements NaverMap.OnMapClickListener, Overlay.OnClickListener, OnMapReadyCallback{


    private static final int ACCESS_LOCATION_PERMISSION_REQUEST_CODE = 100;
    private FusedLocationSource locationSource;
    private NaverMap naverMap;
    private InfoWindow infoWindow;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);

        MapFragment mapFragment = (MapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        back = findViewById(R.id.back);
        //뒤로가기 버튼
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(find.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;

        locationSource = new FusedLocationSource(this, ACCESS_LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationSource(locationSource);
        UiSettings uiSettings = naverMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        naverMap.setOnMapClickListener(this);

        naverMap.setMinZoom(10.0);
        naverMap.setMaxZoom(16.0);

        LatLng mapCenter = naverMap.getCameraPosition().target;
        PharmParser.Ypos = mapCenter.latitude;
        PharmParser.Xpos = mapCenter.longitude;
        PharmParser.radius = 2000;
        setUpMap();

        infoWindow = new InfoWindow();
        infoWindow.setAdapter(new InfoWindow.DefaultViewAdapter(this) {
            @NonNull
            @Override
            protected View getContentView(@NonNull InfoWindow infoWindow) {
                Marker marker = infoWindow.getMarker();
                PharmDTO entity = (PharmDTO) marker.getTag();
                View view = View.inflate(find.this, R.layout.view_info_window,null);
                ((TextView) view.findViewById(R.id.name)).setText(entity.getName());
                ((TextView) view.findViewById(R.id.addr)).setText(entity.getAddr());
                ((TextView) view.findViewById(R.id.telno)).setText(entity.getTelno());

                return view;
            }
        });

    }


    @Override
    public  boolean onClick(@NonNull Overlay overlay){
        if (overlay instanceof Marker) {
            Marker marker = (Marker) overlay;
            if (marker.getInfoWindow() != null) {
                infoWindow.close();
            } else {
                infoWindow.open(marker);
            }
            return true;
        }
        return false;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case ACCESS_LOCATION_PERMISSION_REQUEST_CODE:
                locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults);
                return;
        }
    }

    @Override
    public void onMapClick(@NonNull PointF pointF, @NonNull LatLng latLng) {
        if (infoWindow.getMarker() != null) {
            infoWindow.close();
        }
    }


    private void setUpMap() {

        PharmParser parser = new PharmParser();
        ArrayList<PharmDTO> list = new ArrayList<PharmDTO>();
        try {
            list = parser.apiParserSearch();
        } catch (Exception e){
            e.printStackTrace();
        }

        for (int i = 0; i < list.size(); i++)
        {
            for(PharmDTO entity : list){
                Marker marker = new Marker();
                marker.setTag(entity);
                marker.setPosition(new LatLng(entity.getYpos(),entity.getXpos()));
                marker.setMap(naverMap);
                marker.setAnchor(new PointF(0.5f,1.0f));
                marker.setCaptionText(entity.getName());
                marker.setWidth(50);
                marker.setHeight(80);
                marker.setOnClickListener(this);
            }
        }
    }



}