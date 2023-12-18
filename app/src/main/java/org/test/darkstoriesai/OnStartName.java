package org.test.darkstoriesai;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class OnStartName extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.on_start_name);
        SharedPreferences sharedPreferences = getSharedPreferences("FIRST-LOADING", Context.MODE_PRIVATE);


        LinearLayout languages = findViewById(R.id.languages);
        EditText nameInput = findViewById(R.id.nameInput);
        Button next = findViewById(R.id.nextButton);

        languages.setOnClickListener(view -> {
            Intent languagesIntent = new Intent(this, Languages.class);
            startActivity(languagesIntent);
        });

        next.setOnClickListener(view -> {
            String name = nameInput.getText().toString();
            String userId = UUID.randomUUID().toString();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("name", name);
            editor.putBoolean("loadingFlag", true);
            editor.putString("userId", userId);
            editor.apply();

            setUser(name, userId);

            Intent mainIntent = new Intent(this, MainActivity.class);
            startActivity(mainIntent);

            finish();
        });
    }

    private void setUser(String name, String userId) {
        API api = new API();

        CompletableFuture.supplyAsync(() -> api.setUser(name, userId)).thenAcceptAsync(result -> runOnUiThread(() -> System.out.println(result.toString())));
    }
}