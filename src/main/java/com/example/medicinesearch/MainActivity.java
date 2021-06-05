package com.example.medicinesearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.view.View;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    private Button btn_search, btn_find, btn_news, btn_calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
}