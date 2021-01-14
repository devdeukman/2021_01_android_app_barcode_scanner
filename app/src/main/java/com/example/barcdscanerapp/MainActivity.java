package com.example.barcdscanerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.barcdscanerapp.utils.DBHelper;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import android.widget.TextView;
import android.widget.Toast;


import java.text.SimpleDateFormat;
import java.util.Date;

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
    TextView txtRawData;
    IntentIntegrator qrScan;
    DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mContext = MainActivity.this;

        dbHelper = new DBHelper(getApplicationContext(), "scanHistory", null, 1);

        txtRawData = findViewById(R.id.rawBarCd);

        // 스캔 모듈
        qrScan = new IntentIntegrator(this);

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
                startActivityForResult(intent, 12345);
            }
        });

        Button btnScan = findViewById(R.id.btnScan);
        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 스캔 옵션
                qrScan.setPrompt("Scanning...");
                qrScan.setOrientationLocked(false);
                qrScan.initiateScan();
            }
        });

    }

    // 결과 받아오기
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // Scan 받아오기
        //  requestCode = 49374
        if (requestCode == 49374) {
            IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
            if (result != null) {
                //qrcode 가 없으면
                if (result.getContents() == null) {
                    Toast.makeText(MainActivity.this, "Cancel", Toast.LENGTH_SHORT).show();
                } else {
                    //qrcode 결과가 있으면

                    // 기존 내용 clear
                    clearTextViews();

                    Toast.makeText(MainActivity.this, "Scanned", Toast.LENGTH_SHORT).show();

                    // QR 인식한 문자열
                    String strRaw = result.getContents();

                    // 분할 규칙 적용하기
                    setTextByBusinessRules(strRaw);

                    // 내장DB에 인식된 코드 저장하기
                    SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date time = new Date();
                    String ymd = format1.format(time);
                    dbHelper.insert(strRaw, ymd);

                }

            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        } else if (requestCode == 12345) { // 기록에서 받아오기
            if (resultCode == RESULT_OK) {
                // 정보를 받아서
                String strHist = data.getStringExtra("QRCODE");
                // 표에서 다시 보여주기
                setTextByBusinessRules(strHist);
            }
        }

    }

    private void setTextByBusinessRules(String strRaw){
        try {

            /*
             * 해당 앱 안에서 볼 수 있는 ASCII -> 문자열
             * 4 -> EOT(End Of Transmission)
             * 29 -> GS(Group Separator)
             * 30 -> RS(Record Separator)
             * */

            // ASCII 코드는 안드로이드 화면에서 볼 수 없음 -> 임의 변경 처리 해줘야함
            // 바꿔야할 문자의 ASCII 코드 및 문자형
            int asciiEot = (char) 4;
            int asciiGs = (char) 29;
            int asciiRs = (char) 30;
            String chEot = Character.toString((char)asciiEot);
            String chGs = Character.toString((char)asciiGs);
            String chRs = Character.toString((char)asciiRs);

            // 바꾸게 될 문자
            String strEot = getResources().getString(R.string.endSep);
            String strGs = getResources().getString(R.string.grpSep);
            String strRs = getResources().getString(R.string.recSep);
            String strOk = getResources().getString(R.string.ok);
            String strNg = getResources().getString(R.string.ng);


            // 화면에서 보일 수 있게 ASCII 문자 변경
            strRaw = strRaw.replaceAll(chEot,strEot).replaceAll(chGs,strGs).replaceAll(chRs,strRs);

            // 바코드내용 칸에 뿌려주기
            txtRawData.setText(Html.fromHtml(strRaw));

            // GS를 기준으로 쪼개기 => 비즈니스 로직에 맞춰서 칸에 넣어주기 위해서
            String[] arrRaw = strRaw.split(strGs);

            // 다음 규칙은 해당 비즈니스에만 해당되는 사항임 (즉, 목적에 따라 유동적으로 변경될 수 있음)
            //  - header 규칙: 1) "[)>RS~~" 으로 시작, 2) 앞의 단어 포함 뒤에 2자리 더 나옴
            //  - footer 규칙: 1) "RSEOT"임
            String strHeaderRule = "[)>"+strRs;
            String strFooterRule = strRs+strEot;

            // 첫번째 분절과 마지막 분절이 규칙에 맞는지 확인 (전체 형식 확인)
            if ( isHeaderRight(arrRaw[0],strHeaderRule) && isFooterRight(arrRaw[arrRaw.length -1], strFooterRule)){
                // 전체 분절이 7개가 맞는지 확인
                if (arrRaw.length != 7) {
                    // 7개가 아닐때 => NG 반환
                    for (int i = 0; i < 10; i++) {

                        setTextToTextViewByIdNo(strNg, "result", i);

                    }
                } else {
                    // 분절 각각의 규칙 확인 (세부 내용 확인)
                    for (int i = 0; i < arrRaw.length; i++) {

                        String[] results = new String[2];

                        switch (i){
                            case 1:
                                results = getResultByFirstChar(arrRaw[i],"V");
                                break;
                            case 2:
                                results = getResultByFirstChar(arrRaw[i],"P");
                                break;
                            case 3:
                                results = getResultByFirstChar(arrRaw[i],"S");
                                break;
                            case 4:
                                results = getResultByFirstChar(arrRaw[i],"E");
                                break;
                            case 5:
                                results = getResultByFirstCharAndLen(arrRaw[i],"T", 19);
                                break;
                            default: //
                                results[0] = strOk;
                                results[1] = arrRaw[i];
                                break;

                        }

                        // 5번째 분절에 2차 규칙 적용되므로 5번 이후 값 따로 처리 (i 숫자가 각 textview id숫자와 맞지 않게 되므로)
                        if (i < 5){
                            // 해당 id를 갖는 TextView를 찾아서 Text 삽입

                            setTextToTextViewByIdNo(results[0], "result", i);
                            setTextToTextViewByIdNo(results[1], "data", i);


                        } else {
                            switch (i) {
                                case 5:
                                    int[] arrSepPoint = {0,6,10,11, results[1].length()};
                                    for (int j = 0; j < 4; j++) {

                                        // 5번째 분절의 2차 규칙 적용
                                        //  - 현재는 규칙없이 자르기만 진행
                                        String strSecChunk = results[1].substring(arrSepPoint[j],arrSepPoint[j+1]);
                                        setTextToTextViewByIdNo(results[0], "result", i+j);
                                        setTextToTextViewByIdNo(strSecChunk, "data", i+j);

                                    }
                                    break;
                                case 6:
                                    setTextToTextViewByIdNo(results[0], "result", i+3);
                                    setTextToTextViewByIdNo(results[1], "data", i+3);
                                    break;
                            }
                        }
                    }
                }

            } else {
                // 전체 형식이 맞지 않을 때 => 모든값을 NG 처리
                for (int i = 0; i < 10; i++) {

                    setTextToTextViewByIdNo(strNg,"result",i);

                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    // Header 규칙에 상응하는지 확인 후 결과 리턴
    private boolean isHeaderRight(String strScan, String strHeaderRule){
        if ( (strScan.startsWith(strHeaderRule)) && (strScan.length() == strHeaderRule.length()+2) ) {
            return true;
        } else {
            return false;
        }
    }

    // Header 규칙에 상응하는지 확인 후 결과 리턴
    private boolean isFooterRight(String strScan, String strFooterRule){
        if (strScan.equals(strFooterRule)){
            return true;
        } else {
            return false;
        }
    }

    // 시작 글자에 맞는 규칙 적용한 후 결과 리턴
    private String[] getResultByFirstChar(String arrChunk,String firstChar){
        String[] results = new String[2];
        if (arrChunk.substring(0,1).equals(firstChar)){
            results[0] = getResources().getString(R.string.ok);
            results[1] = arrChunk.substring(1);
        } else {
            results[0] = getResources().getString(R.string.ng);
            results[1] = arrChunk;
        }
        return results;
    }

    // 시작 글자에 맞는 규칙, 글자 길이 검사 적용한 후 결과 리턴
    private String[] getResultByFirstCharAndLen(String arrChunk,String firstChar, int len){
        String[] results = new String[2];
        if ( (arrChunk.substring(0,1).equals(firstChar)) && (arrChunk.length() >= len) ){
            results[0] = getResources().getString(R.string.ok);
            results[1] = arrChunk.substring(1);
        } else {
            results[0] = getResources().getString(R.string.ng);
            results[1] = arrChunk;
        }
        return results;
    }

    // (숫자가 증가하며 반복되는) 아이디 찾아서 Text 넣어주는 메소드
    private void setTextToTextViewByIdNo(String text, String id_name, int iter_num){

        // 해당 id를 가지는 리소스 찾기
        int resID = getResources().getIdentifier(id_name+iter_num,"id", getPackageName());

        // 위에서 찾은 리소스에 값 변경해주기
        ((TextView)findViewById(resID)).setText(Html.fromHtml(text));
    }

    // TextView Text 초기화
    private void clearTextViews(){
        for (int i = 0; i < 10; i++) {

            setTextToTextViewByIdNo("", "result", i);
            setTextToTextViewByIdNo("", "data", i);

        }
    }

}