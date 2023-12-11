package org.test.darkstoriesai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.CompletableFuture;


public class SavedStories  extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.saved_stories);

        setStories();

        LinearLayout back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();

            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
        });
    }

    public void setStories() {
        API api = new API();
        CompletableFuture.supplyAsync(api::storiesRequest).thenAcceptAsync(result -> runOnUiThread(() -> {
            ListView listView = findViewById(R.id.storiesListView);
            ArrayList<Map<String, Object>> emptyList = new ArrayList<>();
            ListStories adapter = new ListStories(this, this, emptyList);
            listView.setAdapter(adapter);

            for (Map<String, Object> story: result) {
                adapter.add(story);
            }
        }));
    }
}
