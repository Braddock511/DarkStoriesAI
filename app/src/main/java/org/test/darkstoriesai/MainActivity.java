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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicInteger;

public class MainActivity extends AppCompatActivity {
    private String sessionId;
    private ListAnswers adapter;
    private AtomicInteger loadingIndex;
    private final API api = new API();
    private String lang = "english";
    private String storyFormatPrompt;
    private String solved = "false";
    private Snackbar snackbar;
    private List<Map<String, Object>> stories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View actualView = findViewById(android.R.id.content);

        TextView languageTextView = findViewById(R.id.language);
        lang = languageTextView.getText().toString();
        storyFormatPrompt = format("Write dark story, in %s language", lang);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Story
        ListView listView = findViewById(R.id.historyListView);
        Intent intent = getIntent();

        if (intent != null && intent.hasExtra("story")) {
            stories = (List<Map<String, Object>>) intent.getSerializableExtra("story");
            List<Answer> answersList = new ArrayList<>();

            for (Map<String, Object> story : stories) {
                List<Map<String, Object>> answers = (List<Map<String, Object>>) story.get("answers");

                for (Map<String, Object> answer : answers) {
                    String answerText = (String) answer.get("answer");
                    double index = (double) answer.get("index");
                    String topic = story.get("topic").toString();
                    String header = getHeaderFromIndex(index, topic);

                    answersList.add(new Answer(header, answerText));
                }
                sessionId = (String) story.get("session_id");
            }

            adapter = new ListAnswers(this, answersList);
            listView.setAdapter(adapter);
        }
        else {
            getStories().join();

            if (stories.isEmpty()){
                List<Answer> loadItemsList = new ArrayList<>();
                adapter = new ListAnswers(this, loadItemsList);
                listView.setAdapter(adapter);

                sessionId = UUID.randomUUID().toString();
                createStory(storyFormatPrompt, sessionId);
            }
            else{
                List<Answer> answersList = new ArrayList<>();
                Map<String, Object> lastStory = stories.get(stories.size() - 1);

                List<Map<String, Object>> answers = (List<Map<String, Object>>) lastStory.get("answers");
                sessionId = (String) lastStory.get("session_id");

                for (Map<String, Object> answer : answers) {
                    String answerText = (String) answer.get("answer");
                    double index = (double) answer.get("index");
                    String header = getHeaderFromIndex(index, lastStory.get("topic").toString());

                    answersList.add(new Answer(header, answerText));
                }

                adapter = new ListAnswers(this, answersList);
                listView.setAdapter(adapter);
            }
        }

        // User ask
        EditText questionPrompt = findViewById(R.id.questionPrompt);
        Button askButton = findViewById(R.id.askButton);
        loadingIndex = new AtomicInteger(2); //Start on 2, because 0. Story 1. Question

        askButton.setOnClickListener(v -> {
            if (solved.equals("false")) {
                String text = questionPrompt.getText().toString();

                adapter.add(new Answer("ASKER", text));
                questionPrompt.getText().clear();

                answer(text);
            }
            else {
                snackbar = Snackbar.make(actualView, "You already solved this story!", Snackbar.LENGTH_SHORT);
                snackbar.show();
            }
        });

        // Buttons
        Button solutionButton = findViewById(R.id.solutionButton);
        Button newStoryButton = findViewById(R.id.newStoryButton);


        solutionButton.setOnClickListener(v -> {
            String questionSolution = questionPrompt.getText().toString();
            if (solved.equals("false")) {
                if (!questionSolution.equals("")) {
                    adapter.add(new Answer("ASKER", questionSolution));
                    questionPrompt.getText().clear();

                    lang = languageTextView.getText().toString();
                    String solutionPrompt = format("%s, answer in %s language", questionSolution, lang);
                    solution(solutionPrompt);
                }
                else {
                    snackbar = Snackbar.make(actualView, "Write solution in input!", Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });

        newStoryButton.setOnClickListener(v -> {
            lang = languageTextView.getText().toString();
            storyFormatPrompt = format("Write dark story, in %s language", lang);
            loadingIndex.set(0);
            adapter.clear();

            sessionId = UUID.randomUUID().toString();
            createStory(storyFormatPrompt, sessionId);
        });
    }

    // Story functions
    public void createStory(String storyFormatPrompt, String newSessionId) {
        CompletableFuture.supplyAsync(() -> {
            // Loading
            runOnUiThread(() -> {
                loadingIndex = new AtomicInteger(2); //Start on 2, because 0. Story 1. Question
                adapter.add(new Answer(true));
                adapter.notifyDataSetChanged();
            });
            return api.createStoryRequest(storyFormatPrompt, newSessionId);
        }).thenAcceptAsync(result -> runOnUiThread(() -> {
            adapter.remove(adapter.getItem(0));  // Remove the loading item
            try {
                String topic = result.get("topic").toString();
                adapter.add(new Answer(topic, result.getString(("story"))));
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
                adapter.add(new Answer(true));
                adapter.notifyDataSetChanged();
            });
            return api.ask(format("%s, answer in %s language", askPrompt, lang), sessionId);
        }).thenAcceptAsync(result -> runOnUiThread(() -> {
            adapter.remove(adapter.getItem(loadingIndex.get()));  // Remove the loading item
            loadingIndex.addAndGet(2);
            try {
                adapter.add(new Answer("STORY TELLER", result.getString("answer")));
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
                adapter.add(new Answer(true));
                adapter.notifyDataSetChanged();
            });
            return api.sendSolution(solutionPrompt, sessionId);
        }).thenAcceptAsync(result -> runOnUiThread(() -> {
            adapter.remove(adapter.getItem(loadingIndex.get()));  // Remove the loading item
            loadingIndex.addAndGet(2);
            try {
                adapter.add(new Answer("STORY TELLER", result.getString("answer")));
                solved = result.getString("solved");
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
            adapter.notifyDataSetChanged();
        }));
    }

    public CompletableFuture<Void> getStories() {
        return CompletableFuture.supplyAsync(api::storiesRequest)
                .thenAcceptAsync(result -> stories = result);
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
        } else if (id == R.id.saved_stories) {
            Intent listStoriesIntent = new Intent(MainActivity.this, SavedStories.class);
            listStoriesIntent.putExtra("stories", (Serializable) stories);
            startActivity(listStoriesIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Other functions
    private String getHeaderFromIndex(double index, String topic) {
        if (index == 0.0){
            return topic;
        }
        else if (index % 2 == 1.0) {
            return "STORY TELLER";
        } else {
            return "ASKER";
        }
    }
}