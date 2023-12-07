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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
//    SplashActivity splashLoading = new SplashActivity();
    private String sessionId = UUID.randomUUID().toString();
    private AdapterListItems adapter;
    private AtomicInteger loadingIndex;
    private final API api = new API();
    private String lang = "english";
    private String storyFormatPrompt;

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
        adapter = new AdapterListItems(this, loadItemsList); // TODO load data from database
        listView.setAdapter(adapter);

        TextView languageTextView = findViewById(R.id.language);
        lang = languageTextView.getText().toString();
        storyFormatPrompt = String.format("Write dark story, in %s language", lang);
//        createStory(storyFormatPrompt);

        // User ask
        EditText questionPrompt = findViewById(R.id.questionPrompt);
        Button askButton = findViewById(R.id.askButton);
        loadingIndex = new AtomicInteger(2); //Start on 2, because 0. Story 1. Question

        askButton.setOnClickListener(v -> {
            String text = questionPrompt.getText().toString();

            adapter.add(new ListItem("ASKER", text));
            questionPrompt.getText().clear();

            answer(text);
        });

        // Buttons
        Button solutionButton = findViewById(R.id.solutionButton);
        Button newStoryButton = findViewById(R.id.newStoryButton);


        solutionButton.setOnClickListener(v -> {
            String questionSolution = questionPrompt.getText().toString();
            if (!questionSolution.equals("")) {
                adapter.add(new ListItem("ASKER", questionSolution));
                questionPrompt.getText().clear();
                String solutionPrompt = format("This is solution? - %s", questionSolution);
                solution(solutionPrompt);
            }
            else{
                Snackbar snackbar = Snackbar.make(actualView, "Write solution in input!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        newStoryButton.setOnClickListener(v -> {
            lang = languageTextView.getText().toString();
            storyFormatPrompt = String.format("Write dark story, in %s language", lang);
            loadingIndex.set(0);
            adapter.clear();
            createStory(storyFormatPrompt);
        });
    }

    // Main func
    public void createStory(String storyFormatPrompt) {
        CompletableFuture.supplyAsync(() -> {
            // Loading
            runOnUiThread(() -> {
                loadingIndex = new AtomicInteger(2); //Start on 2, because 0. Story 1. Question
                adapter.add(new ListItem(true));
                adapter.notifyDataSetChanged();
            });
            return api.createStoryRequest(storyFormatPrompt, sessionId);
        }).thenAcceptAsync(result -> runOnUiThread(() -> {
            adapter.remove(adapter.getItem(0));  // Remove the loading item
            try {
                adapter.add(new ListItem("STORY TELLER", result.getString(("story"))));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            adapter.notifyDataSetChanged();
        }));
    }

    public void answer(String askPrompt) {
        CompletableFuture.supplyAsync(() -> {
            // Loading
            runOnUiThread(() -> {
                adapter.add(new ListItem(true));
                adapter.notifyDataSetChanged();
            });
            return api.ask(format("%s, asnwer in %s language", askPrompt, lang), sessionId);
        }).thenAcceptAsync(result -> runOnUiThread(() -> {
            adapter.remove(adapter.getItem(loadingIndex.get()));  // Remove the loading item
            loadingIndex.addAndGet(2);
            try {
                adapter.add(new ListItem("STORY TELLER", result.getString("answer")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            adapter.notifyDataSetChanged();
        }));
    }

    public void solution(String solutionPrompt) {
        CompletableFuture.supplyAsync(() -> {
            // Loading
            runOnUiThread(() -> {
                adapter.add(new ListItem(true));
                adapter.notifyDataSetChanged();
            });
            return api.sendSolution(solutionPrompt, sessionId);
        }).thenAcceptAsync(result -> runOnUiThread(() -> {
            adapter.remove(adapter.getItem(loadingIndex.get()));  // Remove the loading item
            loadingIndex.addAndGet(2);
            try {
                adapter.add(new ListItem("STORY TELLER", result.getString("answer")));
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            adapter.notifyDataSetChanged();
        }));
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

        if (id == R.id.how_to_play_item) {
            Intent howToPlayIntent = new Intent(MainActivity.this, HowToPlay.class);
            startActivity(howToPlayIntent);
            return true;
        } else if (id == R.id.languages) {
            Intent languagesIntent = new Intent(MainActivity.this, Languages.class);
            startActivity(languagesIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}