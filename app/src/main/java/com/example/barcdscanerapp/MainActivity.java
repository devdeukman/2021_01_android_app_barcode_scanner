package com.example.barcdscanerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import static com.example.barcdscanerapp.utils.LangConfigUtils.setLocale;

/**
* 메인 화면
* @author kwonym
* @version 1.0.0
* @since 2021-01-11 오후 1:41
**/

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        Button btnLang = findViewById(R.id.btnLang);
        btnLang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setMessage("언어 선택");
                builder.setTitle("언어 설정")
                        .setCancelable(true)
                        .setPositiveButton("한국어", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setLocale(MainActivity.this, "ko");
                            }
                        })
                        .setNegativeButton("English", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                setLocale(MainActivity.this, "en");
                            }
                        });
                AlertDialog alert = builder.create();
                alert.setTitle("언어 선택");
                alert.show();
            }
        });

        Button btnHistory = findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),HistoryActivity.class);
                startActivity(intent);
            }
        });

        Button btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLocale(MainActivity.this, "ko");
            }
        });

    }


}