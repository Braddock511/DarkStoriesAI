package org.test.darkstoriesai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnStartLanguages extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_start_languages);

        LanguagesManager langManager = new LanguagesManager(this);
        ImageView enImage = findViewById(R.id.imageViewEnglish);
        ImageView plImage = findViewById(R.id.imageViewPolish);
        ImageView esImage = findViewById(R.id.imageViewSpanish);

        enImage.setOnClickListener(view -> {
            finish();
            langManager.updateResource("en");
            Intent nameIntent = new Intent(this, OnStartName.class);
            startActivity(nameIntent);
        });

        plImage.setOnClickListener(view -> {
            finish();
            langManager.updateResource("pl");
            Intent nameIntent = new Intent(this, OnStartName.class);
            startActivity(nameIntent);
        });

        esImage.setOnClickListener(view -> {
            finish();
            langManager.updateResource("es");
            Intent nameIntent = new Intent(this, OnStartName.class);
            startActivity(nameIntent);
        });

    }
}