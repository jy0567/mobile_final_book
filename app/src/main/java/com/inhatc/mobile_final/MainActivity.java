package com.inhatc.mobile_final;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    private Button btnLogin, btnJoin;
    SQLiteDatabase myDB;// Database object

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnJoin = (Button) findViewById(R.id.btnJoin);

        createDB(); //db 초기 생성
        
        btnLogin.setOnClickListener(this); //로그인 버튼 클릭
        btnJoin.setOnClickListener(this); //회원가입 버튼 클릭


    }

    @Override
    public void onClick(View view) {
        if(view == btnLogin){ //로그인 버튼 클릭 시 로그인 창으로 이동
            Intent loginIntent = new Intent(MainActivity.this, Login.class);
            startActivity(loginIntent);
        } else if(view == btnJoin){ //회원가입 버튼 클릭 시 회원가입 창으로 이동
            Intent joinIntent = new Intent(MainActivity.this, Join.class);
            startActivity(joinIntent);
        }
    }

    public void createDB(){
        //Open DB (DB Name: final)
        myDB = this.openOrCreateDatabase ("final", MODE_PRIVATE, null);

        //회원테이블==================================================================
        myDB.execSQL ("Drop table if exists members");
        myDB.execSQL ("Create table members (" +
                "id text primary key," + "pw text not null," +
                "Name text not null, " + "email text not null);" );
        myDB.execSQL ("Insert into members " +
                "values ('1', '1', '최산', 'san@gmail.com');" );
        //회원테이블==================================================================

        //책테이블==================================================================
        myDB.execSQL ("Drop table if exists books");
        myDB.execSQL ("Create table books (" +
                "id integer primary key autoincrement," + "title text not null, "+
                "writer text not null, " + "content text not null);" );

        ContentValues insertValue = new ContentValues();
        insertValue.put("title", "당신은 결국 무엇이든 해내는 사람");
        insertValue.put("writer", "김상현");
        insertValue.put("content", "더 나아가 나 자신을 제대로 바라볼 수 있는 힘을, " +
                "수많은 시행착오에도 불구하고 우리는 무엇이든 이루어낼 수 있는 사람임을, " +
                "희망과 믿음의 문장들로 담아냈다.  삶에 아무 것도 남지 않은 것만 같을 때, " +
                "무엇을 해야 할지 알 수 없어 막막할 때, 이 책이 당신을 한 걸음 더 나아가게 하는 " +
                "희망의 메시지가 되어 줄 것이다.");
        myDB.insert("books", null, insertValue);

        myDB.execSQL ("Insert into books " +
                "(title, writer, content) values ('다채로운 일상', '다채롬', " +
                "'트랜스여성 다채롬은 시스젠더(트랜스젠더가 아닌 사람들)가 이해할 수 없을지도 " +
                "모르는 이야기와 감정들을 400쪽이 넘치게 가득 담았다. 서로 다른 존재들이 서로의 " +
                "마음을 알면 조금 더 가까워질 수 있을지도, 서로 더 존중할 수 있을지도 모른다는 바람이 " +
                "이야기의 기원이 되었다. 다채롬이 힘겹게 지난 이야기들을 풀어낸 것은 트랜스젠더에게는 " +
                "정보와 공감을 주고, 시스젠더인 사람들에게서는 편견과 선입견을 덜어주고 싶어서다.');" );
        //책테이블==================================================================

        //리뷰테이블==================================================================
        myDB.execSQL ("Drop table if exists reviews");
        myDB.execSQL ("Create table reviews (" +
                "id integer primary key autoincrement," + "memberID text not null, "+
                "title text not null, "+ "writer text not null, " + "review text not null);" );
        myDB.execSQL ("Insert into reviews(memberid, title, writer, review) values ('1', 't', 'd','dd');");
        //리뷰테이블==================================================================
        if(myDB != null) myDB.close(); //Close DB
    }
}