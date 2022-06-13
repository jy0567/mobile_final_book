package com.inhatc.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Detail extends AppCompatActivity implements View.OnClickListener {
    SQLiteDatabase myDB;// Database object
    private TextView txtTitle, txtWriter, txtContent;
    private Button btnBack;
    private String bookTitle, bookWriter, bookContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        txtTitle = (TextView)findViewById(R.id.txtTitle);
        txtWriter = (TextView)findViewById(R.id.txtWriter);
        txtContent = (TextView)findViewById(R.id.txtContent);
        btnBack = (Button)findViewById(R.id.btnBack);

        //전달받은 책 아이디 값 저장
        Intent DetailIntent = getIntent();
        int bookID = Integer.parseInt(DetailIntent.getExtras().getString("bookID")); //선택한 책의 id값

        //Open DB (DB Name: final)
        myDB = this.openOrCreateDatabase ("final", MODE_PRIVATE, null);

        //Get record Data from members table, (select *)
        Cursor allRCD = myDB.query("books", null,
                null, null, null, null, null, null);

        //선택한 책의 정보를 가져옴
        allRCD.moveToFirst();
        do{
            if(allRCD.getInt(0)==bookID){
                bookTitle = allRCD.getString(1);
                bookWriter = allRCD.getString(2);
                bookContent = allRCD.getString(3);
                break;
            }
        }while (allRCD.moveToNext());

        //책 제목, 저자, 내용 세팅
        txtTitle.setText(bookTitle);
        txtWriter.setText(bookWriter);
        txtContent.setText(bookContent);

        btnBack.setOnClickListener(this); //돌아가기 버튼 클릭 시
    }

    @Override
    public void onClick(View view) {
        if(view==btnBack){
            finish();
        }
    }
}