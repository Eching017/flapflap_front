package com.example.flapflap_front;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CommunityActivity extends AppCompatActivity {

    private ImageButton backButton;
    private Button btnAll, btnLatestPosts, btnLatestComments;
    private TextView communityNameTextView;
    private ImageView communityIconImageView;
    private RecyclerView postList;
    private FloatingActionButton fabCreatePost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        backButton = findViewById(R.id.back_button);
        btnAll = findViewById(R.id.btn_all);
        btnLatestPosts = findViewById(R.id.btn_latest_posts);
        btnLatestComments = findViewById(R.id.btn_latest_comments);
        communityNameTextView = findViewById(R.id.community_name);
        communityIconImageView = findViewById(R.id.community_icon);
        postList = findViewById(R.id.post_list);
        fabCreatePost = findViewById(R.id.fab_create_post);

        if (backButton != null) {
            backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish(); // 返回上一页
                }
            });
        }

        TextView switchButton = findViewById(R.id.switch_btn);
        switchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CommunityActivity.this, AllCommunityActivity.class);
                startActivity(intent);
            }
        });

        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到搜索页面
                Intent intent = new Intent(CommunityActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        fabCreatePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到发布帖子编辑页面
                Intent intent = new Intent(CommunityActivity.this, CreatePostActivity.class);
                startActivity(intent);
            }
        });

        // 示例：点击事件示例
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommunityActivity.this, "显示全部帖子", Toast.LENGTH_SHORT).show();
                // 更新帖子列表显示全部帖子
            }
        });

        btnLatestPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommunityActivity.this, "显示最新发帖", Toast.LENGTH_SHORT).show();
                // 更新帖子列表显示最新发帖
            }
        });

        btnLatestComments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(CommunityActivity.this, "显示最新评论", Toast.LENGTH_SHORT).show();
                // 更新帖子列表显示最新评论
            }
        });

        // 获取从上一个活动传递过来的社区 ID
        int communityId = getIntent().getIntExtra("COMMUNITY_ID", -1);
        if (communityId != -1) {
            fetchCommunityInfo(communityId);
        }
    }

    private void fetchCommunityInfo(int communityId) {
        OkHttpClient client = new OkHttpClient();

        RequestBody formBody = new FormBody.Builder()
                .add("id", String.valueOf(communityId))
                .build();

        Request request = new Request.Builder()
                .url("http://127.0.0.1:1207/server/community/getInfo")
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.e(TAG, "Failed to fetch community info: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    Log.d(TAG, "Response: " + responseData);

                    try {
                        JSONObject jsonObject = new JSONObject(responseData);
                        String gameName = jsonObject.getString("gameName");
                        String iconUrl = jsonObject.getString("icon");

                        // 在 UI 线程更新社区名称和图标
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                communityNameTextView.setText(gameName);
                                Glide.with(CommunityActivity.this)
                                        .load(iconUrl)
                                        .into(communityIconImageView);
                            }
                        });

                        // 根据需要处理其他信息，比如帖子列表的加载等

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e(TAG, "JSON parsing error: " + e.getMessage());
                    }

                } else {
                    Log.e(TAG, "Failed to fetch community info: " + response.code());
                }
            }
        });
    }
}


