package org.test.darkstoriesai;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class HowToPlay extends AppCompatActivity {

    @Override
    protected void attachBaseContext(Context newBase) {
        LanguagesManager languagesManager = new LanguagesManager(newBase);
        Locale newLocale = languagesManager.loadLanguage();
        Configuration config = new Configuration(newBase.getResources().getConfiguration());
        config.setLocale(newLocale);
        Context context = newBase.createConfigurationContext(config);
        super.attachBaseContext(context);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.how_to_play);

        LinearLayout back = findViewById(R.id.back);

        back.setOnClickListener(view -> finish());

    }
}
