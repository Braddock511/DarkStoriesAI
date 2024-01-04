package org.test.darkstoriesai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnStartFearAnimal extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_start_fear_animal);
        SharedPreferences sharedPreferences = getSharedPreferences("FIRST-LOADING", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        LinearLayout spiders = findViewById(R.id.spiders);
        LinearLayout bugs = findViewById(R.id.bugs);
        LinearLayout snakes = findViewById(R.id.snakes);
        LinearLayout sharks = findViewById(R.id.sharks);

        spiders.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearMonster.class);
            startActivity(fearIntent);
            editor.putString("animal", "spiders");
            editor.apply();

            finish();
        });

        bugs.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearMonster.class);
            startActivity(fearIntent);
            editor.putString("animal", "bugs");
            editor.apply();

            finish();
        });

        snakes.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearMonster.class);
            startActivity(fearIntent);
            editor.putString("animal", "snakes");
            editor.apply();

            finish();
        });

        sharks.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearMonster.class);
            startActivity(fearIntent);
            editor.putString("animal", "sharks");
            editor.apply();

            finish();
        });


    }
}