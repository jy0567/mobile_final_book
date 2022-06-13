package com.inhatc.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReviewList extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{
    SQLiteDatabase myDB;// Database object
    ArrayList<String> aryRList;// ArrayList object, 레코드를 만들기 위한 리스트
    ArrayAdapter<String> adtReviews;// ArrayAdapter object, array 값을 공급해주는 어뎁터
    ListView lstView;// ListView object
    String strRecord = null;// Record data
    String memberID, selectItem="";

    private Button btnDetail, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_list);

        btnDetail = (Button)findViewById(R.id.btnDetail2);
        btnBack = (Button)findViewById(R.id.btnBackToMainPage2);

        Intent reviewListIntent = getIntent();
        memberID = reviewListIntent.getExtras().getString("memberID"); //로그인한 회원의 id값

        //Open DB (DB Name: final)
        myDB = this.openOrCreateDatabase ("final", MODE_PRIVATE, null);

        //Get record Data from members table, (select *)
        Cursor allRCD = myDB.query("reviews", null,
                null, null, null, null, null, null);

        //Create ArrayList
        aryRList = new ArrayList<String>();
        if (allRCD != null) {
            if (allRCD.moveToFirst()) { //첫번째 레코드로 가서
                do{ //반복
                    if(allRCD.getString(1).equals(memberID)){
                        strRecord = allRCD.getInt(0)+"\t\t제목: "+allRCD.getString(2)+"\t\t지은이: "+allRCD.getString(3);
                        aryRList.add(strRecord);
                    }
                }while (allRCD.moveToNext()); //다음 레코드로 감
            }
        }
        if(myDB != null) myDB.close(); //Close DB

        adtReviews = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, aryRList); //하나씩 선택 가능
        //Create ListView
        lstView = (ListView) findViewById(R.id.lstReviews);
        lstView.setAdapter (adtReviews); //데이터 공급
        lstView.setChoiceMode (ListView.CHOICE_MODE_SINGLE); //하나만 선택하게끔


        lstView.setOnItemClickListener(this); //리스트 아이템 클릭 시

        btnBack.setOnClickListener(this); //돌아가기 버튼 클릭 시
        btnDetail.setOnClickListener(this); //상세보기 버튼 클릭 시
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Object clickItemObj = adapterView.getAdapter().getItem(position);
        selectItem = clickItemObj.toString().substring(0,3).trim(); //선택한 review의 id 값이 저장됨
    }

    @Override
    public void onClick(View view) {
        if (view == btnBack) { //돌아가기 버튼 클릭 시
            finish();
        } else if (view == btnDetail) { //상세보기 버튼 클릭 시
            if (selectItem.equals("")) { //아무 아이템도 선택 안했으면
                Toast.makeText(this, "감상평을 선택해주세요.", Toast.LENGTH_LONG).show();
            } else { //선택한 감상평의 상세 정보 조회 페이지로 이동
                Intent reviewDetailIntent = new Intent(ReviewList.this, ReviewDetail.class);
                reviewDetailIntent.putExtra("reviewID", selectItem); //review의 id값 전달
                startActivity(reviewDetailIntent);
            }
        }
    }
}