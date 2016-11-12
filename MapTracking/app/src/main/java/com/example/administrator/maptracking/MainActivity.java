package com.example.administrator.maptracking;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.database.sqlite.*;

public class MainActivity extends Activity implements OnClickListener {
    //데이터베이스
    SQLiteDatabase db;

    private LocationManager locationManager = null; // 위치 정보 프로바이더
    private LocationListener locationListener = null; //위치 정보가 업데이트시 동작

    private Button btnGetLocation, btnGpsStop, btnGetInfo, btndel, btnmap;

    private static final String TAG = "debug";
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        try {
            db = SQLiteDatabase.openDatabase( "/data/data/com.example.administrator.maptracking/myloggerDB",
                    null, SQLiteDatabase.CREATE_IF_NECESSARY);
            db.execSQL("create table logger ( recID integer PRIMARY KEY autoincrement,  lat  REAL,  lon REAL, location STRING, mdate STRING );  "    );
            //   db.close();
        } catch (SQLiteException e) {
            //Toast.makeText(this, e.getMessage(),  Toast.LENGTH_SHORT).show();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        btnGetInfo = (Button) findViewById(R.id.btnShow);
        btnGetInfo.setOnClickListener(this);

        btnGetLocation = (Button) findViewById(R.id.btnLocation);
        btnGetLocation.setOnClickListener(this);

        btnGpsStop = (Button) findViewById(R.id.btnStop);
        btnGpsStop.setOnClickListener(this);

        btndel = (Button) findViewById(R.id.btndel);
        btndel.setOnClickListener(this);

        btnmap = (Button) findViewById(R.id.btnmap);
        btnmap.setOnClickListener(this);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnmap:
                Intent intent = new Intent(
                        getApplicationContext(),
                        MapsActivity.class);
                startActivity(intent);
                break;

            case R.id.btnShow:
                Intent intent2 = new Intent(
                        getApplicationContext(),
                        datalist.class);
                startActivity(intent2);
                break;
            case R.id.btnStop:
                // 위치 정보 업데이트 중단
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                locationManager.removeUpdates(locationListener);
                Toast.makeText(getBaseContext(), "기록을 중지합니다.", Toast.LENGTH_SHORT).show();
                break;

            case R.id.btnLocation:
                Toast.makeText(getBaseContext(), "위치 정보를 기록합니다.", Toast.LENGTH_SHORT).show();
                //GPS_PROVIDER: GPS를 통해 위치를 알려줌
                isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                //NETWORK_PROVIDER: WI-FI 네트워크나 통신사의 기지국 정보를 통해 위치를 알려줌
                isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

                if(isGPSEnabled && isNetworkEnabled){ //둘다 받아오면
                    locationListener = new MyLocationListener();

                    //선택된 프로바이더를 사용해 위치정보를 업데이트
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, locationListener);
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, locationListener);
                }else{
                    alertbox("gps 상태!!", "당신의 gps 상태 : off");
                }
                break;

            case R.id.btndel:
                    db.execSQL("drop table logger");  //테이블을 삭제하고 테이블 틀을 다시 만들어줌
                    db.execSQL("create table logger ( recID integer PRIMARY KEY autoincrement,  lat  REAL,  lon REAL, location STRING,mdate STRING);  "    );
                    Toast.makeText(getBaseContext(), "데이터베이스를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    //gps 안켜져있으면 키도록 유도함
    protected void alertbox(String title, String mymessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("your device's gps is disable")
                .setCancelable(false)
                .setTitle("**gps status**")
                .setPositiveButton("gps on", new DialogInterface.OnClickListener() {

                    //  폰 위치 설정 페이지로 넘어감
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
    //현재 위치 정보를 받기위해 선택한 프로바이더에 위치 업데이터 요청! requestLocationUpdates()메소드를 사용함.
    private class MyLocationListener implements LocationListener {

        @Override
        //LocationListener을 이용해서 위치정보가 업데이트 되었을때 동작 구현
        public void onLocationChanged(Location loc) {

            //뷰에 출력하기 위해 스트링으로 저장
            String longitude = "위도: " + loc.getLongitude();
            Log.d(TAG, longitude);
            String latitude = "경도: " + loc.getLatitude();
            Log.d(TAG, latitude);

            // 도시명 구하기
            String cityName1 = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try{
                addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
                cityName1 = addresses.get(0).getAddressLine(0).toString();
                System.out.println(cityName1);
            }catch(IOException e){
                e.printStackTrace();
            }
            // 현재 시간을 msec으로 구한다.
            long now = System.currentTimeMillis();
            // 현재 시간을 저장 한다.
            Date date = new Date(now);
            // 시간 포맷으로 만든다.
            SimpleDateFormat sdfNow = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            String strNow = sdfNow.format(date);

            db.execSQL( "insert into logger(lat, lon,location,mdate) values ('"+loc.getLatitude()+"', '"+ loc.getLongitude()+"','"+ cityName1+ "','"+strNow+"' );"  );
            //좌표 정보 얻어 토스트메세지 출력
            Toast.makeText(getBaseContext(), "위도: " + loc.getLatitude() + " 경도: " + loc.getLongitude()+" 저장됨", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }
    }
}
