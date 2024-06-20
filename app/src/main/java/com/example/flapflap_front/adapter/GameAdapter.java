package com.example.flapflap_front.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.flapflap_front.GameDetailActivity;
import com.example.flapflap_front.R;
import com.example.flapflap_front.model.Game;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private final List<Game> gameList;

    public GameAdapter(List<Game> gameList) {
        this.gameList = gameList;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_game, parent, false);
        return new GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameViewHolder holder, int position) {
        Game game = gameList.get(position);
        holder.gameIcon.setImageResource(game.getIcon());
        holder.gameName.setText(game.getName());
        holder.gameDescription.setText(game.getDescription());

        // 设置点击事件监听器
        holder.itemView.setOnClickListener(v -> {
            Context context = v.getContext();
            Intent intent = new Intent(context, GameDetailActivity.class);
            intent.putExtra("gameId", game.getId()); // 将选中的游戏对象传递到详情页面
            context.startActivity(intent);
        });

        holder.downloadButton.setOnClickListener(v -> {
            // 处理下载按钮点击事件
//            Context context = v.getContext();
//            Intent intent = new Intent(context, GameDownloadActivity.class);
//            intent.putExtra("gameId", game.getId()); // 将选中的游戏对象传递到下载页面
//            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return gameList.size();
    }

    static class GameViewHolder extends RecyclerView.ViewHolder {
        ImageView gameIcon;
        TextView gameName;
        TextView gameDescription;
        Button downloadButton;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);
            gameIcon = itemView.findViewById(R.id.game_icon);
            gameName = itemView.findViewById(R.id.game_name);
            gameDescription = itemView.findViewById(R.id.game_description);
            downloadButton = itemView.findViewById(R.id.download_button);
        }
    }
}

