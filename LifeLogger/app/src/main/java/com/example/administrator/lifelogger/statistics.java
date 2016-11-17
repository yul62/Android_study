package com.example.administrator.lifelogger;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class statistics extends FragmentActivity  {

    SQLiteDatabase db;

    private TextView textview =null;
    private TextView textview2 =null;
    private EditText editTitle;
    private EditText editText;
    private Button savebtn;
    private Spinner spinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            db = SQLiteDatabase.openDatabase( "/data/data/com.example.administrator.lifelogger/myloggerDB",
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table logger ( recID integer PRIMARY KEY autoincrement,  lat  REAL,  lon REAL, location STRING, mdate STRING, category STRING ,title STRING ,content STRING);  "    );
            //   db.close();
        } catch (SQLiteException e) {
            //Toast.makeText(this, e.getMessage(),  Toast.LENGTH_SHORT).show();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);

        textview = (TextView)findViewById(R.id.textView);
        textview2 = (TextView)findViewById(R.id.textView2);
        editText = (EditText)findViewById(R.id.editText);
        editTitle = (EditText)findViewById(R.id.editTitle);
        spinner = (Spinner)findViewById(R.id.spinner);

        findViewById(R.id.savebtn).setOnClickListener(
                new Button.OnClickListener(){
                    public  void onClick(View v){

                    }
                });

        Intent intent = getIntent();
        final double lat = intent.getDoubleExtra("lat",0);
        final double lon = intent.getDoubleExtra("long",0);

        String cityName1 = null;
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = gcd.getFromLocation(lat, lon, 1);
            cityName1 = addresses.get(0).getAddressLine(0).toString();
            System.out.println(cityName1);
        }catch(IOException e){
            e.printStackTrace();
        }
        textview.setText(cityName1);

        long now = System.currentTimeMillis();
        // 현재 시간을 저장 한다.
        Date date = new Date(now);
        // 시간 포맷으로 만든다.
        SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        String strNow=null;
        strNow = sdfNow.format(date);

        textview2.setText(strNow);

        final String finalCityName = cityName1.substring(5);
        final String finalStrNow = strNow;

        findViewById(R.id.savebtn).setOnClickListener(
                new Button.OnClickListener(){
                    public  void onClick(View v){
                        String str =spinner.getSelectedItem().toString();

                        System.out.println("위도 "+lat+" 경도 "+lon);
                        System.out.println("위치 "+ finalCityName+" 카테고리 "+str);
                        System.out.println(" 날짜 "+ finalStrNow + " 제목 "+editTitle.getText().toString()+" 내용 "+editText.getText().toString());

                        if(editTitle.getText().toString().equals("")==false) {
                            db.execSQL("insert into logger(lat, lon, location, mdate, category, title, content) values (" +
                                    "'" + lat + "', '" + lon + "','" + finalCityName + "','" + finalStrNow + "','" + str + "','" + editTitle.getText().toString() + "','" + editText.getText().toString() + "' );");
                            Toast.makeText(getBaseContext(),"일지를 저장했습니다.",Toast.LENGTH_SHORT).show();
                        }
                        editText.setText("");
                        editTitle.setText("");

                    }
                });
    }

}
