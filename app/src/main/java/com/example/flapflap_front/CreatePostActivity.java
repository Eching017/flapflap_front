package com.example.flapflap_front;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CreatePostActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_PICK = 1;

    private EditText postEditText;
    private int communityId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        // 获取传递过来的社区 ID
        communityId = getIntent().getIntExtra("COMMUNITY_ID", -1);

        // 初始化视图
        postEditText = findViewById(R.id.edit_text_post);
        Button btnPublish = findViewById(R.id.btn_publish);
        ImageView insertPictureButton = findViewById(R.id.insert_picture_button);
        ImageView atFriendsButton = findViewById(R.id.at_friends_button);
        ImageView selectTopicButton = findViewById(R.id.select_topic_button);

        // 设置返回按钮点击事件
        findViewById(R.id.btn_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到搜索页面
                Intent intent = new Intent(CreatePostActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        // 设置发布按钮点击事件
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int userId = 1; //默认id
                String postText = postEditText.getText().toString();

                addPost(communityId, userId, postText);
            }
        });

        // 设置插入图片按钮点击事件
        insertPictureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });

        // 设置艾特好友按钮点击事件
        atFriendsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAtFriendsDialog();
            }
        });

        // 设置选择话题按钮点击事件
        selectTopicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openSelectTopicDialog();
            }
        });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_IMAGE_PICK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == RESULT_OK && data != null) {
            // 处理选择的图片
            // Uri selectedImage = data.getData();
            // 可以在这里把选中的图片插入到编辑框中
        }
    }

    private void openAtFriendsDialog() {
        // 打开一个对话框或新活动，让用户选择或输入要@的好友
    }

    private void openSelectTopicDialog() {
        // 打开一个对话框或新活动，让用户选择或输入话题
    }

    private void addPost(int communityId, int userId, String content) {
        String url = "http://127.0.0.1:1207/server/post/addPost";

        OkHttpClient client = new OkHttpClient();

        // 创建请求体
        MediaType mediaType = MediaType.parse("application/json; charset=utf-8");
        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("communityId", communityId);
            requestBody.put("poster", userId);
            requestBody.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(mediaType, requestBody.toString());

        // 创建请求
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        // 发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseData = response.body().string();
                try {
                    JSONObject jsonResponse = new JSONObject(responseData);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CreatePostActivity.this, "帖子发布成功", Toast.LENGTH_SHORT).show();
                                // 帖子发布成功后的处理，如关闭页面或刷新帖子列表等
                                finish();
                            }
                        });
                    } else {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(CreatePostActivity.this, "帖子发布失败", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CreatePostActivity.this, "网络错误，帖子发布失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
