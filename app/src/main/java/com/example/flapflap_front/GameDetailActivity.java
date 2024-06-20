package com.example.flapflap_front;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.example.flapflap_front.adapter.GameScreenshotsAdapter;
import com.example.flapflap_front.model.Game;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class GameDetailActivity extends AppCompatActivity {

    private OkHttpClient client;
    private int gameId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        client = new OkHttpClient();

        Intent intent = getIntent();
        gameId = intent.getIntExtra("gameId", 0);

        fetchGameDetails(gameId);
    }

    private void fetchGameDetails(int gameId) {
        String url = "http://127.0.0.1:1207/server/gameinfo/getInfo"; // Replace with your API endpoint

        RequestBody formBody = new FormBody.Builder()
                .add("id", String.valueOf(gameId))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(formBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                // Handle failure, e.g., show error message to the user
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Unexpected code " + response);
                }

                try {
                    // Parse JSON response
                    JSONObject jsonObject = new JSONObject(response.body().string());
                    String gameName = jsonObject.getString("gameName");
                    String gameIconUrl = jsonObject.getString("gameIcon");
                    String version = jsonObject.getString("version");
                    String fileSize = jsonObject.getString("fileSize");
                    String type = jsonObject.getString("type");
                    int downloadCount = jsonObject.getInt("downloadCount");
                    String description = jsonObject.getString("description");
                    JSONArray imagesArray = jsonObject.getJSONArray("images");
                    List<String> imageUrls = new ArrayList<>();
                    for (int i = 0; i < imagesArray.length(); i++) {
                        imageUrls.add(imagesArray.getString(i));
                    }

                    // Update UI on the main thread
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Update UI with game details
                            TextView gameNameTextView = findViewById(R.id.game_name);
                            gameNameTextView.setText(gameName);

                            ImageView gameIconImageView = findViewById(R.id.game_icon);
                            Glide.with(GameDetailActivity.this).load(gameIconUrl).into(gameIconImageView);

                            TextView gameVersionTextView = findViewById(R.id.game_version);
                            gameVersionTextView.setText("Version: " + version);

                            TextView gameMemorySizeTextView = findViewById(R.id.game_memory_size);
                            gameMemorySizeTextView.setText("Memory Size: " + fileSize);

                            TextView gameTypeTextView = findViewById(R.id.game_type);
                            gameTypeTextView.setText("Type: " + type);

                            TextView downloadCountTextView = findViewById(R.id.download_count);
                            downloadCountTextView.setText("已有" + downloadCount + "人下载");

                            TextView gameDescriptionTextView = findViewById(R.id.game_description);
                            gameDescriptionTextView.setText(description);

                            // Set up ViewPager2 for game screenshots
                            ViewPager2 viewPager = findViewById(R.id.view_pager);
                            GameScreenshotsAdapter adapter = new GameScreenshotsAdapter(imageUrls);
                            viewPager.setAdapter(adapter);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
