package com.example.medicinesearch;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.browser.customtabs.CustomTabsCallback;
import androidx.browser.customtabs.CustomTabsClient;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;


public class NewsActivity extends AppCompatActivity {

    EditText searchEt;
    ImageView searchIb;
    Spinner newsSpinner;



    String category = "";
    String[] spinnerArray = new String[]{"카테고리를 선택해주세요.","news", "book", "cafearticle", "kin"};
    String[] koreaArray = new String[]{"카테고리를 선택해주세요.","뉴스", "책", "네이버 카페", "지식 IN"};

    StringBuffer response;
    String naverHtml;

    RecyclerView mRecylerView = null;
    RecyclerNewsSearchAdapter mAdapter = null;
    private ArrayList<NewsRecyclerItem> mlist = new ArrayList<NewsRecyclerItem>();

    int position = -1;
    int newsSize = 0;

    SharedPreferences pref;
    SharedPreferences.Editor editor;


    Handler handler = new Handler(Looper.myLooper()) {
        public void handleMessage(Message msg) {
            Bundle bun = msg.getData();
            naverHtml = bun.getString("NAVER_HTML");

            naverHtml = naverHtml.replaceAll("<b>","");
            naverHtml = naverHtml.replaceAll("</b>","");
            naverHtml = naverHtml.replaceAll("<", "<");
            naverHtml = naverHtml.replaceAll(">", ">");
            naverHtml = naverHtml.replaceAll("&", "&");

            try {
                JSONObject jsonObject = new JSONObject(naverHtml);

                JSONArray newsArray = jsonObject.getJSONArray("items");

                mlist.clear();

                for(int i = 0; i < newsArray.length(); i++)
                {
                    JSONObject newsObject = newsArray.getJSONObject(i);

                    NewsRecyclerItem item = new NewsRecyclerItem();

                    item.setTitle(newsObject.getString("title"));
                    item.setDescription(newsObject.getString("description"));
                    item.setLink(newsObject.getString("link"));


                    mlist.add(item);
                }

                Log.v("main mAdapter","notifyDataSetChanged 호출");
                mAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        mRecylerView = findViewById(R.id.newsRecyclerView);
        Toolbar tb = findViewById(R.id.app_toolbar);
        searchEt = findViewById(R.id.searchEt);
        searchIb = findViewById(R.id.searchIb);
        newsSpinner = findViewById(R.id.newsSpinner);

        setSupportActionBar(tb);

        ActionBar ab = getSupportActionBar();

        ab.setTitle(" 검색 ");

        mAdapter = new RecyclerNewsSearchAdapter(mlist);
        mRecylerView.setAdapter(mAdapter);
        // 리사이클러뷰에 LinearLayoutManager 지정. (미지정: vertical)
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));


        Log.v("main mAdapter","notifyDataSetChanged 호출");
        mAdapter.notifyDataSetChanged();
        mAdapter.setOnItemClickListener((v, pos) -> {
            try {
                CustomTabsIntent.Builder customTabsIntent = new CustomTabsIntent.Builder();
                customTabsIntent.build()
                        .launchUrl(NewsActivity.this, Uri.parse(mlist.get(pos).getLink()));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "페이지 링크가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
            }

        });
        //크롬탭 호출


        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(NewsActivity.this, android.R.layout.simple_spinner_dropdown_item, koreaArray);

        newsSpinner.setAdapter(myAdapter);
        newsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                if(!koreaArray[position].equals("카테고리를 선택해주세요."))
                {
                    Toast.makeText(getApplicationContext(), koreaArray[position] + " 선택",
                            Toast.LENGTH_SHORT).show();

                    category = spinnerArray[position];

                }
                else
                {
                    Toast.makeText(getApplicationContext(), "카테고리를 선택해주세요.",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //검색
        searchIb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SearchNews(category, String.valueOf(searchEt.getText()));
            }
        });


    }


    private void SearchNews(final String _category, final String searchWord)
    {

        if(_category.equals("") && _category != null)
        {
            Toast.makeText(getApplicationContext(), "카테고리를 선택해주세요.",
                    Toast.LENGTH_SHORT).show();
        }
        else if(searchWord.equals("") && searchWord != null)
        {
            Toast.makeText(getApplicationContext(), "검색어를 입력해주세요.",
                    Toast.LENGTH_SHORT).show();
        }
        else
        {
            new Thread() {
                @Override
                public void run() {

                    String clientId = "kKEm9fUU_fdyKeSumgl7";//애플리케이션 클라이언트 아이디값";
                    String clientSecret = "YTHrPT9FOX";//애플리케이션 클라이언트 시크릿값";
                    try {
                        String text = URLEncoder.encode(searchWord, "UTF-8");
                        String apiURL = "https://openapi.naver.com/v1/search/" + _category + "?query=" + text +"&display=20"; // json 결과
                        //String apiURL = "https://openapi.naver.com/v1/search/blog.xml?query="+ text; // xml 결과
                        URL url = new URL(apiURL);
                        HttpURLConnection con = (HttpURLConnection) url.openConnection();
                        con.setRequestMethod("GET");
                        con.setRequestProperty("X-Naver-Client-Id", clientId);
                        con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                        int responseCode = con.getResponseCode();
                        BufferedReader br;
                        if (responseCode == 200) { // 정상 호출
                            br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                        } else {  // 에러 발생
                            br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                        }
                        String inputLine;
                        response = new StringBuffer();
                        while ((inputLine = br.readLine()) != null) {
                            response.append(inputLine);
                            response.append("\n");
                        }
                        br.close();

                        String naverHtml = response.toString();

                        Bundle bun = new Bundle();
                        bun.putString("NAVER_HTML", naverHtml);
                        Message msg = handler.obtainMessage();
                        msg.setData(bun);
                        handler.sendMessage(msg);

                        //testText.setText(response.toString());
                        //System.out.println(response.toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }.start();
        }

    }


}
