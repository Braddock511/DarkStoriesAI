package org.test.darkstoriesai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class OnStartFearMonster extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_start_fear_monster);
        SharedPreferences sharedPreferences = getSharedPreferences("FIRST-LOADING", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        LinearLayout vampire = findViewById(R.id.vampire);
        LinearLayout werewolf = findViewById(R.id.werewolf);
        LinearLayout zombie = findViewById(R.id.zombie);
        LinearLayout skeleton = findViewById(R.id.skeleton);

        vampire.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearPlace.class);
            startActivity(fearIntent);
            editor.putString("monster", "vampire");
            editor.apply();

            finish();
        });

        werewolf.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearPlace.class);
            startActivity(fearIntent);
            editor.putString("monster", "werewolf");
            editor.apply();

            finish();
        });

        zombie.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearPlace.class);
            startActivity(fearIntent);
            editor.putString("monster", "zombie");
            editor.apply();

            finish();;
        });

        skeleton.setOnClickListener(view -> {
            Intent fearIntent = new Intent(this, OnStartFearPlace.class);
            startActivity(fearIntent);
            editor.putString("monster", "skeleton");
            editor.apply();

            finish();
        });


    }
}