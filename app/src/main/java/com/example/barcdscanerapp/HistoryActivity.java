package com.example.barcdscanerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.barcdscanerapp.utils.DBHelper;
import com.example.barcdscanerapp.utils.ListAdapter;
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

        // 리스트뷰 정의
        ListView mListView = findViewById(R.id.listHist);

        // 내장 DB에서 데이터 받아올 객체 정의(ArrayList)
        arrQuery = new ArrayList<>();
        arrQuery = dbHelper.getHistories();

        // 리스트뷰 옵션 정의
        HashMap<String,Object> list_options = new HashMap<String,Object>();
        String[] ROWS_ID= {"QRCODE", "DATE"};
        String[] WEIGHT = {"2","1"};
        String[] TEXT_SIZE = {"20", "20"};

        list_options.put("ROWS_ID" ,ROWS_ID);
        list_options.put("WEIGHT" ,WEIGHT);
        list_options.put("TEXT_SIZE", TEXT_SIZE);

        ListAdapter oAdapter = new ListAdapter(arrQuery , list_options );
        mListView.setAdapter(oAdapter);
        
        oAdapter.setOnItemClickListener(new ListAdapter.OnItemClickListener() {
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
        
//        Intent intent = getIntent();
//        intent.putExtra("test", 764536);
//        setResult(RESULT_OK, intent);
//        finish();

    }
}