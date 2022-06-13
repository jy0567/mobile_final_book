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
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class Login extends AppCompatActivity implements View.OnClickListener{
    SQLiteDatabase myDB;// Database object
    private EditText loginID, loginPW;
    private Button btnLogin, btnLoginCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btnLoginOK);
        btnLoginCancel = (Button)findViewById(R.id.btnLoginCancel);

        //로그인 버튼 클릭
        btnLogin.setOnClickListener(this);

        //취소 버튼 클릭
        btnLoginCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnLoginCancel){ //취소 버튼 클릭 시
            finish();
        }else{ //로그인 버튼 클릭 시
            loginID = (EditText)findViewById(R.id.editTxtID);
            loginPW = (EditText)findViewById(R.id.editTxtPW);
            String id = loginID.getText().toString();
            String pw = loginPW.getText().toString();

            if(id.trim().equals("") || pw.trim().equals("")) //입력 안 된 부분 있을 때
                Toast.makeText(this, "입력이 안 된 부분이 있습니다.", Toast.LENGTH_LONG).show();
            else{ //모두 입력 되었을 때
                String memberID, memberPW;
                boolean check=false;

                //Open DB (DB Name: final)
                myDB = this.openOrCreateDatabase ("final", MODE_PRIVATE, null);

                //Get record Data from members table, (select *)
                Cursor allRCD = myDB.query("members", null,
                        null, null, null, null, null, null);

                //아이디, 비밀번호 확인
                if (allRCD != null) {
                    if (allRCD.moveToFirst()) { //첫번째 레코드로 가서
                        do{ //반복
                            memberID = allRCD.getString(0);
                            memberPW = allRCD.getString(1);

                            if(memberID.equals(id) && memberPW.equals(pw))
                                check = true;

                        }while (allRCD.moveToNext()); //다음 레코드로 감
                    }
                }

                if(check == false){ //아이디 비밀번호 틀렸을 시 알림창 출력
                    Toast.makeText(this, "아이디 혹은 비밀번호가 틀렸습니다.", Toast.LENGTH_LONG).show();
                }else{ //로그인 성공 시
                    Toast.makeText(this, "로그인에 성공했습니다.", Toast.LENGTH_LONG).show();
                    if(myDB != null) myDB.close(); //Close DB
                    Intent mainPageIntent = new Intent(Login.this, MainPage.class);
                    mainPageIntent.putExtra("id",id); //id값 전달
                    startActivity(mainPageIntent);
                }
                if(myDB != null) myDB.close(); //Close DB
            }


        }
    }
}