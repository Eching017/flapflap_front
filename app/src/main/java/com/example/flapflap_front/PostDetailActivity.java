package com.example.flapflap_front;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class PostDetailActivity extends AppCompatActivity {
    private int communityId;
    private int postId;
    private int likes;
    private int reply_likes;
    private int commentCount;
    private TextView likesTextView;

    private ImageButton backButton;
    private TextView titleTextView;
    private ImageView userAvatarImageView;
    private TextView usernameTextView, postTimeTextView, postTitleTextView, postContentTextView;
    private ImageView postImageView;
    private ImageView likeButton, commentButton, reply_likeButton;
    private TextView likeCountTextView, commentCountTextView, reply_likeCount;
    private RecyclerView commentList;
    private EditText commentEditText;
    private Button sendCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // 获取传递的社区ID和帖子ID
        communityId = getIntent().getIntExtra("COMMUNITY_ID", -1);
        postId = getIntent().getIntExtra("POST_ID", -1);

        getPostDetail(postId, communityId);

        // 设置返回按钮的点击事件
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // 返回上一页
            }
        });

        ImageButton searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 跳转到搜索页面
                Intent intent = new Intent(PostDetailActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });

        likesTextView = findViewById(R.id.like_count);
        Button likeButton = findViewById(R.id.btn_like);
        // 示例：设置点赞按钮的点击事件
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点赞逻辑
                Toast.makeText(PostDetailActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                // 更新点赞数
                likePost(postId);
            }
        });

        // 示例：设置评论按钮的点击事件
        commentEditText = findViewById(R.id.comment_edit_text);
        sendCommentButton = findViewById(R.id.send_comment_button);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示评论输入框
                commentEditText.requestFocus();
                String commentContent = commentEditText.getText().toString().trim();
                if (!commentContent.isEmpty()) {
                    addComment(postId, 1, commentContent); // 假设评论者的ID是1
                }
            }
        });

        sendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 发送评论逻辑
                Toast.makeText(PostDetailActivity.this, "评论已发送", Toast.LENGTH_SHORT).show();
                // 清空输入框
                commentEditText.setText("");
                // 更新评论列表
            }
        });

        // 示例：设置评论列表的 RecyclerView
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        commentList.setLayoutManager(layoutManager);
    }

    private void getPostDetail(int postId, int communityId) {
        String url = "http://127.0.0.1:1207/server/post/postInfo?id=" + postId;

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(communityId)) // 添加社区ID到FormData
                .build();

        // 创建请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 发起请求
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理错误
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    runOnUiThread(() -> {
                        // 解析并显示数据
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            displayPostDetail(jsonObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void displayPostDetail(JSONObject postDetail) {
        // 解析 JSON 并显示数据
        ImageView userAvatarImageView = findViewById(R.id.user_avatar);
        TextView usernameTextView = findViewById(R.id.username);
        TextView postTimeTextView = findViewById(R.id.post_time);
        TextView titleTextView = findViewById(R.id.post_title);
        TextView contentTextView = findViewById(R.id.post_content);
        TextView likesTextView = findViewById(R.id.like_count);
        TextView commentCountTextView = findViewById(R.id.comment_count);
        LinearLayout imagesContainer = findViewById(R.id.imagesContainer);

        try {
            String userAvatarBase64 = postDetail.getString("userAvatar");
            String username = postDetail.getString("username");
            String postTime = postDetail.getString("postTime");
            String title = postDetail.getString("title");
            String content = postDetail.getString("content");
            likes = postDetail.getInt("likes");
            commentCount = postDetail.getInt("commentCount");
            JSONArray imagesArray = postDetail.getJSONArray("images");

            // Decode Base64 avatar
            byte[] decodedString = Base64.decode(userAvatarBase64, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            userAvatarImageView.setImageBitmap(decodedByte);

            usernameTextView.setText(username);
            postTimeTextView.setText(postTime);
            titleTextView.setText(title);
            contentTextView.setText(content);
            likesTextView.setText(String.valueOf(likes));
            commentCountTextView.setText(String.valueOf(commentCount));

            // Load images
            imagesContainer.removeAllViews();
            for (int i = 0; i < imagesArray.length(); i++) {
                String imageUrl = imagesArray.getString(i);
                ImageView imageView = new ImageView(this);
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(8, 8, 8, 8);
                imageView.setLayoutParams(layoutParams);
                Picasso.get().load(imageUrl).into(imageView);
                imagesContainer.addView(imageView);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void likePost(int postId) {
        String url = "http://127.0.0.1:1207/server/post/like";

        // 构建请求体
        RequestBody requestBody = new FormBody.Builder()
                .add("id", String.valueOf(postId))
                .build();

        // 创建请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 发起请求
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理错误
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    runOnUiThread(() -> {
                        try {
                            boolean success = Boolean.parseBoolean(responseData.trim());
                            if (success) {
                                likes++;
                                likesTextView.setText(String.valueOf(likes));
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        });
    }

    private void addComment(int postId, int commenterId, String content) {
        String url = "http://127.0.0.1:1207/server/comment/addComment";

        // 构建JSON请求体
        JSONObject json = new JSONObject();
        try {
            json.put("postId", postId);
            json.put("commenter", commenterId);
            json.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // 创建请求体
        RequestBody requestBody = RequestBody.create(
                MediaType.parse("application/json; charset=utf-8"), json.toString());

        // 创建请求
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        // 发起请求
        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // 处理错误
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    String responseData = response.body().string();
                    runOnUiThread(() -> {
                        if (Boolean.parseBoolean(responseData.trim())) {
                            // 评论成功，清空输入框内容
                            commentEditText.setText("");
                        }
                    });
                }
            }
        });
    }
}