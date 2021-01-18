package com.example.barcdscanerapp.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        db.execSQL("CREATE TABLE IF NOT EXISTS T_SCAN (code TEXT NOT NULL, date DATETIME NOT NULL);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String code, String date) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO T_SCAN (code,date) VALUES('" + code + "', '" + date + "');");
        db.close();
    }

    public void delete(String code, String date) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행 삭제
        db.execSQL("DELETE FROM T_SCAN WHERE code='" + code + "' AND date='" + date + "';" );
        db.close();
    }


    public ArrayList<HashMap<String,Object>> getHistories() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<HashMap<String,Object>> results = new ArrayList<>();

        try {
            // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
            Cursor cursor = db.rawQuery("SELECT code, date FROM T_SCAN  ORDER BY date DESC;", null);
            while (cursor.moveToNext()) {

                HashMap<String,Object> result = new HashMap<>();
                result.put("QRCODE",cursor.getString(0));
                result.put("DATE", cursor.getString(1));

                results.add(result);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        db.close();
        return results;
    }

}
