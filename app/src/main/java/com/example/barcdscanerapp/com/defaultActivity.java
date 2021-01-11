package com.example.barcdscanerapp.com;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class defaultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setAppLanguage(getApplicationContext());

    }

    public static void setAppLanguage(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        
//        if (language.equals("Chinese")) {
//            configuration.locale = Locale.CHINESE;
//        } else if (language.equals("Japanese")) {
//            configuration.locale = Locale.JAPANESE;
//        } else if (language.equals("Malay")) {
//            configuration.locale = new Locale("ms");
//        } else if (language.equals("Vietnam")) {
//            configuration.locale = new Locale("vi");
//        } else if (language.equals("Danish")) {
//            configuration.locale = new Locale("da");
//        } else if (language.equals("French")) {
//            configuration.locale = new Locale("fr");
//        } else if (language.equals("Hungarian")) {
//            configuration.locale = new Locale("hu");
//        } else if (language.equals("Swedish")) {
//            configuration.locale = new Locale("sv");
//        } else if (language.equals("Korean")) {
//            configuration.locale = new Locale("ko");
//        } else if (language.equals("Russian")) {
//            configuration.locale = new Locale("ru");
//        } else if (language.equals("Spanish")) {
//            configuration.locale = new Locale("es");
//        } else {
//            configuration.locale = Locale.ENGLISH;
//        }
        resources.updateConfiguration(configuration, displayMetrics);
    }
}
