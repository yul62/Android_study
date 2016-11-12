package com.example.administrator.maptracking;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import android.widget.Toast;
/**
 * Created by Administrator on 2016-11-01.
 */
public class datalist extends Activity {
    private TextView textView2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.datalayout);  // layout xml 과 자바파일을 연결

        textView2 = (TextView) findViewById(R.id.textView2);
        textView2.setMovementMethod(new ScrollingMovementMethod());

        String str = "";
        SQLiteDatabase db;
        db = SQLiteDatabase.openDatabase("/data/data/com.example.administrator.maptracking/myloggerDB",
                null, SQLiteDatabase.CREATE_IF_NECESSARY);

        try {
            Cursor cursor = db.rawQuery("select * from logger", null);
            while (cursor.moveToNext()) {

                str += " "+cursor.getInt(0)
                        + " : 위도 "
                        + cursor.getDouble(1)
                        + ", 경도 "
                        + cursor.getDouble(2)
                        +"\n "
                        + cursor.getString(3)
                        + "\n "
                        +cursor.getString(4)
                        +"\n\n";
            }
            textView2.setText(str);
        }catch (Exception e){
            textView2.setText(" ");
            Toast.makeText(getBaseContext(), "데이터베이스가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
        }
    }
}
