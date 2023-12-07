package org.test.darkstoriesai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

public class Languages extends AppCompatActivity {

    private LanguagesManager langManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.languages);
        langManager = new LanguagesManager(this);

        Button englishButton = findViewById(R.id.englishButton);
        englishButton.setOnClickListener(view -> {
            langManager.updateResource("en");
            recreate();
        });

        Button polishButton = findViewById(R.id.polishButton);
        polishButton.setOnClickListener(view -> {
            langManager.updateResource("pl");
            recreate();
        });

        Button spanishButton = findViewById(R.id.spanishButton);
        spanishButton.setOnClickListener(view -> {
            langManager.updateResource("es");
            recreate();
        });

        LinearLayout back = findViewById(R.id.back);
        back.setOnClickListener(view -> {
            finish();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });
    }
}