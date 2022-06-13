package com.inhatc.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class Join extends AppCompatActivity implements View.OnClickListener{
    SQLiteDatabase myDB;// Database object
    private EditText joinName, joinEmail, joinID, joinPW;
    private Button btnJoin, btnJoinCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        btnJoin = (Button) findViewById(R.id.btnJoinOK);
        btnJoinCancel = (Button)findViewById(R.id.btnJoinCancel);

        //회원가입 버튼 클릭
        btnJoin.setOnClickListener(this);

        //취소 버튼 클릭
        btnJoinCancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view == btnJoinCancel){ //취소 버튼 클릭 시
            finish();
        }else{ //회원가입 버튼 클릭 시
            joinName = (EditText) findViewById(R.id.editTxtJoinName);
            joinEmail = (EditText) findViewById(R.id.editTxtJoinEmail);
            joinID = (EditText) findViewById(R.id.editTxtJoinID);
            joinPW = (EditText) findViewById(R.id.editTxtJoinPW);
            String id = joinID.getText().toString();
            String pw = joinPW.getText().toString();
            String name = joinName.getText().toString();
            String email = joinEmail.getText().toString();

            if(id.trim().equals("") || pw.trim().equals("") ||
                    name.trim().equals("") || email.trim().equals("")) //입력 안된 부분이 있을 떄
                Toast.makeText(this, "입력이 안 된 부분이 있습니다.", Toast.LENGTH_LONG).show();
            else if(!email.contains("@")) //이메일 형식이 옳지 않을 때
                Toast.makeText(this, "올바른 이메일 형식이 아닙니다.", Toast.LENGTH_LONG).show();
            else{ //모두 입력됐을 때
                String memberID, memberEmail;
                boolean idCheck = false, emailCheck = false;

                //Open DB (DB Name: final)
                myDB = this.openOrCreateDatabase ("final", MODE_PRIVATE, null);

                //Get record Data from members table, (select *)
                Cursor allRCD = myDB.query("members", null,
                        null, null, null, null, null, null);

                //아이디, 이메일 중복확인
                if (allRCD != null) {
                    if (allRCD.moveToFirst()) { //첫번째 레코드로 가서
                        do{ //반복
                            memberID = allRCD.getString(0);
                            memberEmail = allRCD.getString(3);
                            if(memberID.equals(id)) //아이디 중복일 때
                                idCheck = true;
                            if(memberEmail.equals(pw)) //이메일 중복일 때
                                emailCheck = true;

                        }while (allRCD.moveToNext()); //다음 레코드로 감
                    }
                }

                //아이디, 이메일 중복 시 알림창 출력
                if(idCheck == true){
                    Toast.makeText(this, "아이디가 중복됩니다.", Toast.LENGTH_LONG).show();
                }else if(emailCheck == true){
                    Toast.makeText(this, "이메일이 중복됩니다.", Toast.LENGTH_LONG).show();
                }else{ //중복 아니면 값을 디비에 저장
                    //Insert Data to members table
                    ContentValues insertValue = new ContentValues();
                    insertValue.put("id", id);
                    insertValue.put("pw", pw);
                    insertValue.put("name", name);
                    insertValue.put("email", email);
                    myDB.insert("members", null, insertValue);
                    if(myDB != null) myDB.close(); //Close DB
                    
                    //메인화면으로 이동
                    Toast.makeText(this, "회원가입이 완료되었습니다.", Toast.LENGTH_LONG).show();
                    finish();
                }
                if(myDB != null) myDB.close(); //Close DB
            }
        }
    }
}