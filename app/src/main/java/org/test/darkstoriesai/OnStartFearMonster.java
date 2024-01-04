package org.test.darkstoriesai;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnStartFearMonster extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_start_fear_monster);

        LinearLayout vampire = findViewById(R.id.vampire);
        LinearLayout werewolf = findViewById(R.id.werewolf);
        LinearLayout zombie = findViewById(R.id.zombie);
        LinearLayout skeleton = findViewById(R.id.skeleton);

        vampire.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearPlace.class);
            startActivity(fearIntent);

            finish();
        });

        werewolf.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearPlace.class);
            startActivity(fearIntent);

            finish();
        });

        zombie.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearPlace.class);
            startActivity(fearIntent);

            finish();;
        });

        skeleton.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearPlace.class);
            startActivity(fearIntent);

            finish();
        });


    }
}