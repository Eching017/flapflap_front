package com.example.flapflap_front;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PostDetailActivity extends AppCompatActivity {

    private ImageButton backButton;
    private TextView titleTextView;
    private ImageView userAvatarImageView;
    private TextView usernameTextView, postTimeTextView, postTitleTextView, postContentTextView;
    private ImageView postImageView;
    private ImageView likeButton, commentButton;
    private TextView likeCountTextView, commentCountTextView;
    private RecyclerView commentList;
    private EditText commentEditText;
    private Button sendCommentButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        backButton = findViewById(R.id.btn_back);
        titleTextView = findViewById(R.id.tv_title);
        userAvatarImageView = findViewById(R.id.user_avatar);
        usernameTextView = findViewById(R.id.username);
        postTimeTextView = findViewById(R.id.post_time);
        postTitleTextView = findViewById(R.id.post_title);
        postContentTextView = findViewById(R.id.post_content);
        postImageView = findViewById(R.id.post_image);
        likeButton = findViewById(R.id.btn_like);
        commentButton = findViewById(R.id.btn_comment);
        likeCountTextView = findViewById(R.id.like_count);
        commentCountTextView = findViewById(R.id.comment_count);
        commentList = findViewById(R.id.comment_list);
        commentEditText = findViewById(R.id.comment_edit_text);
        sendCommentButton = findViewById(R.id.send_comment_button);

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

        // 示例：设置点赞按钮的点击事件
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 点赞逻辑
                Toast.makeText(PostDetailActivity.this, "点赞成功", Toast.LENGTH_SHORT).show();
                // 更新点赞数
            }
        });

        // 示例：设置评论按钮的点击事件
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 显示评论输入框
                commentEditText.requestFocus();
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
        // 设置适配器等评论列表相关操作
        // CommentAdapter commentAdapter = new CommentAdapter(commentData);
        // commentList.setAdapter(commentAdapter);

        // 从 Intent 中获取帖子数据并显示
        // String postId = getIntent().getStringExtra("POST_ID");
        // 加载帖子数据并更新 UI
    }
}