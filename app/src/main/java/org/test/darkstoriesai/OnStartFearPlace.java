package org.test.darkstoriesai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnStartFearPlace extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_start_fear_place);
        SharedPreferences sharedPreferences = getSharedPreferences("FIRST-LOADING", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        LinearLayout hospital = findViewById(R.id.hospital);
        LinearLayout graveyard = findViewById(R.id.graveyard);
        LinearLayout church = findViewById(R.id.church);
        LinearLayout house = findViewById(R.id.house);

        hospital.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            editor.putString("place", "abandoned hospital");
            editor.apply();

            finish();
        });

        graveyard.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            editor.putString("place", "graveyard");
            editor.apply();

            finish();
        });

        church.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            editor.putString("place", "church");
            editor.apply();

            finish();;
        });

        house.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);
            editor.putString("place", "abandoned house");
            editor.apply();

            finish();
        });


    }
}