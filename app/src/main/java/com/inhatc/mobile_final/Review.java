package com.inhatc.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Review extends AppCompatActivity implements View.OnClickListener {
    private String memberID;
    private Button btnSave, btnBack;
    private EditText editTxtTitle, editTxtWriter, editTxtReview;
    SQLiteDatabase myDB;// Database object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        
        btnSave = (Button)findViewById(R.id.btnSave);
        btnBack = (Button)findViewById(R.id.btnBackToMainPage);

        Intent reviewIntent = getIntent();
        memberID = reviewIntent.getExtras().getString("memberID"); //로그인한 회원의 id값

        btnSave.setOnClickListener(this); //저장하기 버튼 클릭 시
        btnBack.setOnClickListener(this); //돌아가기 버튼 클릭 시
    }

    @Override
    public void onClick(View view) {
        if(view==btnBack){
            finish();
        }else if(view == btnSave){
            editTxtTitle = (EditText)findViewById(R.id.editTxtTitle);
            editTxtWriter = (EditText)findViewById(R.id.editTxtWriter);
            editTxtReview = (EditText)findViewById(R.id.editTxtReview);
            String title = editTxtTitle.getText().toString();
            String writer = editTxtWriter.getText().toString();
            String review = editTxtReview.getText().toString();

            if(title.trim().equals("") || writer.trim().equals("") || review.trim().equals("")) //입력 안된 부분이 있을 떄
                Toast.makeText(this, "입력이 안 된 부분이 있습니다.", Toast.LENGTH_LONG).show();
            else{ //모두 입력됐을 때
                //Open DB (DB Name: final)
                myDB = this.openOrCreateDatabase ("final", MODE_PRIVATE, null);

                ContentValues insertValue = new ContentValues();
                insertValue.put("memberID", memberID);
                insertValue.put("title", title);
                insertValue.put("writer", writer);
                insertValue.put("review", review);
                myDB.insert("reviews", null, insertValue);

                if(myDB != null) myDB.close(); //Close DB

                //메인화면으로 이동
                Toast.makeText(this, "작성이 완료되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }
}