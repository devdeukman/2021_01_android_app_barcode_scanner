package com.example.barcdscanerapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.barcdscanerapp.utils.DBHelper;
import com.example.barcdscanerapp.utils.RecyclerAdapter;
import com.example.barcdscanerapp.utils.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;

public class HistoryActivity extends AppCompatActivity {

    /**
    * 이전까지 조회했던 기록 화면
     * - 내장DB(SQLite)
    * @author kwonym
    * @version 1.0.0
    * @since 2021-01-11 오후 5:08
    **/

    DBHelper dbHelper;
    ArrayList<HashMap<String,Object>> arrQuery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        // DB 정의
        dbHelper = new DBHelper(getApplicationContext(), "scanHistory", null, 1);
        // 내장 DB에서 데이터 받아올 객체 정의(ArrayList)
        arrQuery = new ArrayList<>();
        arrQuery = dbHelper.getHistories();

        // 리사이클러뷰 정의
        RecyclerView mRecyclerView = findViewById(R.id.listHist);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        RecyclerAdapter mAdapter = new RecyclerAdapter(arrQuery);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new RecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 클릭한 레이아웃의 필요 정보 받아오기
                String strCode = StringUtils.noNull(arrQuery.get(position).get("QRCODE"));
                Intent intent = getIntent();
                intent.putExtra("QRCODE",strCode);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }
}