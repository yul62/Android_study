package com.example.administrator.lifelogger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class viewStatistics extends FragmentActivity implements View.OnClickListener {
    private Spinner spinner2;
    private Button searchBtn;
    private Button lifeBtn;
    private Button delBtn;
    private TextView textview3=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_statistics);

        spinner2= (Spinner)findViewById(R.id.spinner2);
        textview3 = (TextView) findViewById(R.id.textView3);
        textview3.setMovementMethod(new ScrollingMovementMethod());

        searchBtn = (Button)findViewById(R.id.searchBtn);
        searchBtn.setOnClickListener(this);

        lifeBtn = (Button)findViewById(R.id.lifeBtn);
        lifeBtn.setOnClickListener(this);

        delBtn = (Button)findViewById(R.id.delBtn);
        delBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        SQLiteDatabase db;
        db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.lifelogger/myloggerDB",
                null, SQLiteDatabase.CREATE_IF_NECESSARY);

        String searchStr = spinner2.getSelectedItem().toString();
        int dd = (int) spinner2.getSelectedItemId();

        switch (v.getId()) {
            case R.id.searchBtn:
                String str = "";

                try {
                    Cursor cursor = db.rawQuery("select * from logger", null);
                    while (cursor.moveToNext()) {
                        if (cursor.getString(5).equals(searchStr) == true) {
                            str +=
                                    //    + " : 위도 "+ cursor.getDouble(1)
                                    //  + ", 경도 " + cursor.getDouble(2)
                                    cursor.getString(4)
                                    + "\n " + cursor.getString(3)
                                    + "\n 제목 " + cursor.getString(6)
                                    + "\n 내용 " + cursor.getString(7)
                                    + "\n\n";
                        }
                    }
                    textview3.setText(str);
                } catch (Exception e) {
                    textview3.setText(" ");
                    Toast.makeText(getBaseContext(), "데이터베이스가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lifeBtn:
                Toast.makeText(getBaseContext(),searchStr,Toast.LENGTH_SHORT).show();

                Intent lifeIntent = new Intent( getApplicationContext(),LifeMap.class);
                lifeIntent.putExtra("category",searchStr);
                startActivity(lifeIntent);

                break;
            case R.id.delBtn:
                db.execSQL("drop table logger");
                Toast.makeText(getBaseContext(),"데이터베이스를 삭제했습니다.",Toast.LENGTH_SHORT).show();

        }
    }
}
