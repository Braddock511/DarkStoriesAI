package org.test.darkstoriesai;

import static java.lang.String.format;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
//    SplashActivity splashLoading = new SplashActivity();
    String sessionId = UUID.randomUUID().toString();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View actualView = findViewById(android.R.id.content);

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

        String storyPrompt = "Write dark story and topic story. According to this instruction:\nTOPIC: \nSTORY: ";

//        processPrompt(adapter, storyPrompt, "STORY TELLER", new AtomicInteger(0));

        // User ask
        EditText questionPrompt = findViewById(R.id.questionPrompt);
        Button askButton = findViewById(R.id.askButton);
        AtomicInteger loadingIndex = new AtomicInteger(2); //Start on 2, because 0. Story 1. Question

        askButton.setOnClickListener(v -> {
            String text = questionPrompt.getText().toString();

            adapter.add(new ListItem("ASKER", text));
            questionPrompt.getText().clear();

//            processPrompt(adapter, format("Answer for this - %s", text), "STORY TELLER", loadingIndex);
        });

        // Buttons
        Button solutionButton = findViewById(R.id.solutionButton);
        Button newStoryButton = findViewById(R.id.newStoryButton);


        solutionButton.setOnClickListener(v -> {
            String questionSolution = questionPrompt.getText().toString();
            if (!questionSolution.equals("")) {
                questionPrompt.getText().clear();
                String solutionPrompt = format("This is solution? - %s", questionSolution);
                processPrompt(adapter, solutionPrompt, "STORY TELLER", loadingIndex);
            }
            else{
                Snackbar snackbar = Snackbar.make(actualView, "Write solution in input!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        newStoryButton.setOnClickListener(v -> {
            CompletableFuture.supplyAsync(() -> {
                API api = new API();
                return api.clearRedis(sessionId);
            }).thenAcceptAsync(result -> runOnUiThread(() -> {
                loadingIndex.set(0);
                adapter.clear();
                adapter.notifyDataSetChanged();
            }));

            processPrompt(adapter, storyPrompt, "STORY TELLER", new AtomicInteger(0));
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

            // Start the new activity
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void processPrompt(AdapterListItems adapter, String storyPrompt, String header, AtomicInteger position) {
        CompletableFuture.supplyAsync(() -> {
            // Loading
            adapter.add(new ListItem(true));
            adapter.notifyDataSetChanged();

            API api = new API();
            return api.sendPrompt(storyPrompt, sessionId);
        }).thenAcceptAsync(result -> runOnUiThread(() -> {
            adapter.remove(adapter.getItem(position.get()));  // Remove the loading item
            position.addAndGet(2);
            adapter.add(new ListItem(header, result));
            adapter.notifyDataSetChanged();
        }));
    }
}