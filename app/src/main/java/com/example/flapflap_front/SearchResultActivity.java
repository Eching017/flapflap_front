package com.example.flapflap_front;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flapflap_front.adapter.GameAdapter;
import com.example.flapflap_front.model.Game;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class SearchResultActivity extends AppCompatActivity {

    private List<Game> gameList;
    private OkHttpClient client;
    private String searchQuery;
    private GameAdapter gameAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // 初始化游戏数据
        initializeGameData();

        // 设置 RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        gameAdapter = new GameAdapter(gameList);
        recyclerView.setAdapter(gameAdapter);

        // 处理返回按钮点击事件
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(v -> finish());

        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到搜索页面
                Intent intent = new Intent(SearchResultActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // 获取传递的搜索查询
        searchQuery = getIntent().getStringExtra("query");

        // 初始化 OkHttpClient
        client = new OkHttpClient();

        // 发送搜索查询到后端
        postSearchQuery(searchQuery);
    }

    private void initializeGameData() {
        // 初始化游戏数据
        gameList = new ArrayList<>();
    }

    private void postSearchQuery(String query) {
        // 创建请求体
        RequestBody formBody = new FormBody.Builder()
                .add("id", "2")
                .build();

        // 创建请求
        Request request = new Request.Builder()
                .url("http://127.0.0.1:1207/server/gameinfo/searchGame?name=" + query)
                .post(formBody)
                .build();

        // 异步发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // 请求失败处理
                runOnUiThread(() -> Toast.makeText(SearchResultActivity.this, "Failed to send search query", Toast.LENGTH_SHORT).show());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // 请求成功处理
                    final String responseData = response.body().string();
                    runOnUiThread(() -> {
                        // 在这里处理服务器响应，例如显示搜索结果
                        Toast.makeText(SearchResultActivity.this, "Search successful", Toast.LENGTH_SHORT).show();
                        parseAndDisplayGames(responseData);
                    });
                } else {
                    // 服务器返回错误处理
                    runOnUiThread(() -> Toast.makeText(SearchResultActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show());
                }
            }
        });
    }

    private void parseAndDisplayGames(String jsonData) {
        Gson gson = new Gson();
        Type gameListType = new TypeToken<List<Game>>(){}.getType();
        List<Game> games = gson.fromJson(jsonData, gameListType);

        // 更新游戏列表和通知适配器刷新数据
        gameList.clear();
        gameList.addAll(games);
        gameAdapter.notifyDataSetChanged();
    }
}
