package com.example.flapflap_front.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flapflap_front.R;
import com.example.flapflap_front.model.Comment;
import com.example.flapflap_front.model.User;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {
    private List<Comment> commentList;
    private Context context;

    public CommentAdapter(List<Comment> commentList, Context context) {
        this.commentList = commentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        User user = comment.getUser();

        holder.usernameTextView.setText(user.getNickname());
        holder.timestampTextView.setText(formatTimestamp(comment.getTimestamp()));
        holder.contentTextView.setText(comment.getContent());
        holder.likesTextView.setText(String.valueOf(comment.getLikes()));
        holder.replyCountTextView.setText(String.valueOf(comment.getReplies().size()));

        // 加载用户头像
        Glide.with(context)
                .load(user.getAvatar())
                .placeholder(R.drawable.insert_picture_icon)
                .error(R.drawable.insert_picture_icon)
                .circleCrop()
                .into(holder.avatarImageView);

        // 处理楼中楼回复
        ReplyAdapter replyAdapter = new ReplyAdapter(comment.getReplies(), context);
        holder.repliesRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.repliesRecyclerView.setAdapter(replyAdapter);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    private String formatTimestamp(String timestamp) {
        // TODO: 格式化时间戳为合适的格式
        return timestamp;
    }

    static class CommentViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView usernameTextView;
        TextView timestampTextView;
        TextView contentTextView;
        TextView likesTextView;
        TextView replyCountTextView;
        RecyclerView repliesRecyclerView;

        CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.comm_user_avatar);
            usernameTextView = itemView.findViewById(R.id.comm_user_name);
            timestampTextView = itemView.findViewById(R.id.comm_time);
            contentTextView = itemView.findViewById(R.id.comm_content);
            likesTextView = itemView.findViewById(R.id.comm_likes);
            replyCountTextView = itemView.findViewById(R.id.reply_count);
            repliesRecyclerView = itemView.findViewById(R.id.replyRecyclerView);
        }
    }
}

