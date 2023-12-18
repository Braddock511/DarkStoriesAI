package org.test.darkstoriesai;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

public class LanguagesManager {
    private final Context ct;
    private final SharedPreferences languagePreferences;
    public LanguagesManager(Context ctx){
        ct = ctx;
        languagePreferences = ct.getSharedPreferences("FIRST-LOADING", Context.MODE_PRIVATE);
    }

    public void updateResource(String code){
        Locale locale = new Locale(code);
        Locale.setDefault(locale);
        Resources resources = ct.getResources();
        Configuration configuration = resources.getConfiguration();
        configuration.setLocale(locale);
        resources.updateConfiguration(configuration, resources.getDisplayMetrics());
        ct.getResources().updateConfiguration(configuration, resources.getDisplayMetrics());
        setLanguage(code);
    }

    public void setLanguage(String code){
        switch (code) {
            case "en":
                code = "english";
                break;
            case "pl":
                code = "polish";
                break;
            case ("es"):
                code = "spanish";
                break;
        }
        SharedPreferences.Editor editor = languagePreferences.edit();

        editor.putString("language", code);
        editor.apply();
    }

    public String getSavedLanguage() {
        return languagePreferences.getString("language", "english");
    }

    public Locale loadLanguage() {
        String languageCode = this.getSavedLanguage();
        Locale newLocale;

        switch (languageCode) {
            case "english":
                newLocale = new Locale("en");
                break;
            case "polish":
                newLocale = new Locale("pl");
                break;
            case "spanish":
                newLocale = new Locale("es");
                break;
            default:
                newLocale = Locale.ENGLISH;
        }
        return newLocale;
    }
}
