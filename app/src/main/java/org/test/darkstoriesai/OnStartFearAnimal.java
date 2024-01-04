package org.test.darkstoriesai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnStartFearAnimal extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_start_fear_animal);

        LinearLayout spiders = findViewById(R.id.spiders);
        LinearLayout bugs = findViewById(R.id.bugs);
        LinearLayout snakes = findViewById(R.id.snakes);
        LinearLayout sharks = findViewById(R.id.sharks);

        spiders.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearMonster.class);
            startActivity(fearIntent);

            finish();
        });

        bugs.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearMonster.class);
            startActivity(fearIntent);

            finish();
        });

        snakes.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearMonster.class);
            startActivity(fearIntent);

            finish();;
        });

        sharks.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearMonster.class);
            startActivity(fearIntent);

            finish();
        });


    }
}