package com.inhatc.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class MainPage extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    SQLiteDatabase myDB;// Database object
    ArrayList<String> aryBList;// ArrayList object, 레코드를 만들기 위한 리스트
    ArrayAdapter<String> adtBooks;// ArrayAdapter object, array 값을 공급해주는 어뎁터
    ListView lstView;// ListView object
    String strRecord = null;// Record data
    String memberID, selectItem="";

    private Button btnLogout, btnReview, btnDetail, btnReviewList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_page);

        btnLogout = (Button)findViewById(R.id.btnLogout);
        btnReview = (Button)findViewById(R.id.btnReview);
        btnDetail = (Button)findViewById(R.id.btnDetail);
        btnReviewList = (Button)findViewById(R.id.btnReviewList);

        Intent mainPageIntent = getIntent();
        memberID = mainPageIntent.getExtras().getString("id"); //로그인한 회원의 id값

        //Create DB (DB Name: books)
        myDB = this.openOrCreateDatabase ("final", MODE_PRIVATE, null);

        //Get record Data from members table, (select *)
        Cursor allRCD = myDB.query("books", null,
                null, null, null, null, null, null);

        //Create ArrayList
        aryBList = new ArrayList<String>();
        if (allRCD != null) {
            if (allRCD.moveToFirst()) { //첫번째 레코드로 가서
                do{ //반복
                    strRecord = allRCD.getInt(0)+"\t\t제목: "+allRCD.getString(1)+"\t\t지은이: "+allRCD.getString(2);
                    aryBList.add(strRecord);
                }while (allRCD.moveToNext()); //다음 레코드로 감
            }
        }
        adtBooks = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_single_choice, aryBList); //하나씩 선택 가능
        //Create ListView
        lstView = (ListView) findViewById(R.id.lstBooks);
        lstView.setAdapter (adtBooks); //데이터 공급
        lstView.setChoiceMode (ListView.CHOICE_MODE_SINGLE); //하나만 선택하게끔

        if(myDB != null) myDB.close(); //Close DB

        lstView.setOnItemClickListener(this); //리스트 아이템 클릭 시

        btnLogout.setOnClickListener(this); //로그아웃 버튼 클릭 시
        btnReviewList.setOnClickListener(this); //내감상평 버튼 클릭 시
        btnReview.setOnClickListener(this); //감상평쓰기 버튼 클릭 시
        btnDetail.setOnClickListener(this); //상세보기 버튼 클릭 시

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Object clickItemObj = adapterView.getAdapter().getItem(position);
        selectItem = clickItemObj.toString().substring(0,3).trim(); //선택한 book의 id 값이 저장됨
    }

    @Override
    public void onClick(View view) {
        if(view == btnLogout){ //메인 화면으로 이동
            Intent mainIntent = new Intent(MainPage.this, MainActivity.class);
            startActivity(mainIntent);
        }else if(view == btnReviewList){ //내감상평 버튼 클릭 시
            Intent reviewListIntent = new Intent(MainPage.this, ReviewList.class);
            reviewListIntent.putExtra("memberID",memberID); //회원 id값 전달
            startActivity(reviewListIntent);
        }else if(view == btnReview){ //감상평쓰기 버튼 클릭 시
            Intent reviewIntent = new Intent(MainPage.this, Review.class);
            reviewIntent.putExtra("memberID",memberID); //회원 id값 전달
            startActivity(reviewIntent);
        }else if(view == btnDetail){ //상세보기 버튼 클릭 시
            if(selectItem.equals("")){ //아무 아이템도 선택 안했으면
                Toast.makeText(this, "책을 선택해주세요.", Toast.LENGTH_LONG).show();
            }else{ //선택한 책의 상세 정보 조회 페이지로 이동
                Intent detailIntent = new Intent(MainPage.this, Detail.class);
                detailIntent.putExtra("bookID",selectItem); //책 id값 전달
                startActivity(detailIntent);
            }
            
        }
    }
}