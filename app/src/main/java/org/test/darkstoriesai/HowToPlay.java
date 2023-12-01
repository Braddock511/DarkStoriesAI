package org.test.darkstoriesai;

import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HowToPlay extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_play);

        LinearLayout back = findViewById(R.id.back);

        back.setOnClickListener(view -> finish());

    }
}
