package org.test.darkstoriesai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnStartFearPlace extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_start_fear_place);

        LinearLayout hospital = findViewById(R.id.hospital);
        LinearLayout graveyard = findViewById(R.id.graveyard);
        LinearLayout church = findViewById(R.id.church);
        LinearLayout house = findViewById(R.id.house);

        hospital.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

            finish();
        });

        graveyard.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

            finish();
        });

        church.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

            finish();;
        });

        house.setOnClickListener(view -> {
            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

            finish();
        });


    }
}