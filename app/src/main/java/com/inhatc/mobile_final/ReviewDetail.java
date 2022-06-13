package com.inhatc.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReviewDetail extends AppCompatActivity implements View.OnClickListener{
    SQLiteDatabase myDB;// Database object
    private EditText editTxtTitle, editTxtWriter, editTxtReview;
    private int reviewID;
    private Button btnUpdate, btnBack, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_detail);

        String title="", writer="", review="";
        editTxtTitle = (EditText)findViewById(R.id.editTxtTitle2) ;
        editTxtWriter= (EditText)findViewById(R.id.editTxtWriter2) ;
        editTxtReview= (EditText)findViewById(R.id.editTxtReview2) ;

        btnUpdate = (Button)findViewById(R.id.btnUpdate);
        btnBack = (Button)findViewById(R.id.btnBackToMainPage3);
        btnDelete = (Button)findViewById(R.id.btnDelete);

        Intent reviewIntent = getIntent();
        reviewID = Integer.parseInt(reviewIntent.getExtras().getString("reviewID")); //review의 id값

        //Open DB (DB Name: final)
        myDB = this.openOrCreateDatabase ("final", MODE_PRIVATE, null);

        //Get record Data from reviews table, (select *)
        Cursor allRCD = myDB.query("reviews", null,
                null, null, null, null, null, null);

        //선택한 감상평의 정보를 가져옴
        allRCD.moveToFirst();
        do{
            if(allRCD.getInt(0)==reviewID){
                title = allRCD.getString(2);
                writer = allRCD.getString(3);
                review = allRCD.getString(4);
                break;
            }
        }while (allRCD.moveToNext());
        if(myDB != null) myDB.close(); //Close DB

        editTxtTitle.setText(title);
        editTxtWriter.setText(writer);
        editTxtReview.setText(review);

        btnUpdate.setOnClickListener(this); //저장하기 버튼 클릭 시
        btnBack.setOnClickListener(this); //돌아가기 버튼 클릭 시
        btnDelete.setOnClickListener(this); //삭제 버튼 클릭 시
    }

    @Override
    public void onClick(View view) {
        if(view==btnBack){
            finish();
        }else if(view == btnUpdate){
            //Open DB (DB Name: final)
            myDB = this.openOrCreateDatabase ("final", MODE_PRIVATE, null);

            String utitle = editTxtTitle.getText().toString();
            String uwriter = editTxtWriter.getText().toString();
            String ureview = editTxtReview.getText().toString();

            if(utitle.trim().equals("") || uwriter.trim().equals("") || ureview.trim().equals("")) //입력 안된 부분이 있을 떄
                Toast.makeText(this, "입력이 안 된 부분이 있습니다.", Toast.LENGTH_LONG).show();
            else{ //모두 입력됐을 때
                String strSQL = "update reviews set title='"+utitle+"', writer='"+uwriter+
                        "', review='"+ureview+"' where id = "+reviewID+";";
                myDB.execSQL(strSQL);
                if(myDB != null) myDB.close(); //Close DB

                //감상평목록으로 이동
                Toast.makeText(this, "수정이 완료되었습니다.", Toast.LENGTH_LONG).show();
                finish();
            }
        }else if(view == btnDelete){
            //Open DB (DB Name: final)
            myDB = this.openOrCreateDatabase ("final", MODE_PRIVATE, null);
            String strSQL = "delete from reviews where id="+reviewID+";";
            myDB.execSQL(strSQL);
            if(myDB != null) myDB.close(); //Close DB

            //감상평목록으로 이동
            Toast.makeText(this, "삭제가 완료되었습니다.", Toast.LENGTH_LONG).show();
            finish();
        }
    }
}