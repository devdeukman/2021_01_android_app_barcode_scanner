package com.example.barcdscanerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class HistoryActivity extends AppCompatActivity {

    /**
    * 이전까지 조회했던 기록 화면
     * - 내장DB(SQLite)
    * @author kwonym
    * @version 1.0.0
    * @since 2021-01-11 오후 5:08
    **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
    }
}