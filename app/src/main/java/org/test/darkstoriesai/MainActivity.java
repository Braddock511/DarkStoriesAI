package org.test.darkstoriesai;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
//    SplashActivity loading = new SplashActivity();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Przy ładowaniu danych
//        Intent loadingIntent = new Intent(MainActivity.this, SplashActivity.class);
//        startActivity(loadingIntent);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Story
        ListView listView = findViewById(R.id.historyListView);
        List<ListItem> loadItemsList = new ArrayList<>();
        AdapterListItems adapter = new AdapterListItems(this, loadItemsList); // TODO load data from database
        listView.setAdapter(adapter);

        String storyPrompt = "Give ONLY dark story and topic story without any explanations. According to this instruction:\n" +
                "TOPIC\n" +
                "STORY";

        CompletableFuture.supplyAsync(() -> {
            // loading
            adapter.add(new ListItem(true));
            adapter.notifyDataSetChanged();

            API api = new API();
            return api.sendRequest(storyPrompt);
        }).thenAcceptAsync(result -> {
            runOnUiThread(() -> {
                adapter.remove(adapter.getItem(0));  // Remove the loading item
                adapter.add(new ListItem("STORY TELLER", result));
                adapter.notifyDataSetChanged();
            });
        });

        // User ask
        EditText editText = findViewById(R.id.inputText);
        Button sendButton = findViewById(R.id.sendButton);
        AtomicInteger loadingIndex = new AtomicInteger(1);

        sendButton.setOnClickListener(v -> {
            String text = editText.getText().toString();

            adapter.add(new ListItem("ASKER", text));
            editText.getText().clear();

            CompletableFuture.supplyAsync(() -> {
                // loading
                adapter.add(new ListItem(true));
                adapter.notifyDataSetChanged();

                API api = new API();
                return api.sendRequest(String.format("%s. Answer with yes or no.", text));
            }).thenAcceptAsync(result -> {
                runOnUiThread(() -> {
                    adapter.remove(adapter.getItem(loadingIndex.get()));
                    loadingIndex.addAndGet(2);
                    adapter.add(new ListItem("STORY TELLER", result));
                    adapter.notifyDataSetChanged();
                });
            });
        });

    }

    // Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Handle menu item clicks here
        if (id == R.id.how_to_play_item) {
            // Create an Intent to start a new activity
            Intent intent = new Intent(MainActivity.this, HowToPlay.class);
            intent.putExtra("welcomeMessage", "asd");

            // Start the new activity
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}